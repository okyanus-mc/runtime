package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.runtime.SomeGlobals;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cactoos.Scalar;
import org.cactoos.scalar.And;

import java.util.Collections;
import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class OkyanusCommandRegistrar {

    private static Logger LOGGER = LogManager.getLogger();

    public static void register() {
        Server s = Okyanus.getServer();
        for (ICommand command : s.getCommandRegistry().getCommandList()) {

            LiteralArgumentBuilder<ServerCommandSource> builder = literal(command.getLabel());

            builder = registerCommand(command, 0, builder);
            SomeGlobals.commandDispatcher.register(builder);
        }
    }

    private static LiteralArgumentBuilder<ServerCommandSource> registerCommand(ICommand command, int location, LiteralArgumentBuilder<ServerCommandSource> builder) {
        ArgumentBuilder argumentBuilder = null;

        List<ICommand> subCommands = command.getSubCommands();

        final LiteralArgumentBuilder<ServerCommandSource> finalBuilder = builder;
        Command<ServerCommandSource> cmd = context -> {
            final CommandSource commandSource = new OkyanusCommandSource(context);
            final String[] inputs = context.getInput().split(" ");
            try {
                finalBuilder.requires(source -> {
                    final Scalar<Boolean> and = new And(
                        requirement -> {
                            return requirement.control(commandSource, inputs, location);
                        },
                        command.getRequirements()
                    );
                    try {
                        return and.value();
                    } catch (Exception e) {
                        return false;
                    }
                });
                return command.getRunnable().run(commandSource);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };

        builder = finalBuilder;

        boolean wasPreviousOptional = false;

        for (ICommand subcommand : subCommands)
            builder.then(registerCommand(subcommand, location + 1, literal(subcommand.getLabel())));

        Collections.reverse(subCommands);
        for (ICommand arg : subCommands) {
            com.mojang.brigadier.arguments.ArgumentType type;

            // TODO Improve with all ArgumentTypes
            switch (arg.getType()) {
                case PLAYER:
                    type = EntityArgumentType.players();
                    break;
                case TEXT:
                default:
                    type = StringArgumentType.string();
                    break;
            }

            ArgumentBuilder argument = CommandManager.argument(arg.getLabel(), type);

            if (argumentBuilder == null) {
                argumentBuilder = argument.executes(cmd);
            } else {
                if (wasPreviousOptional)
                    argument = argument.executes(cmd);

                argumentBuilder = argument.then(argumentBuilder);
            }

            wasPreviousOptional = arg.isOptional();
        }

        if (argumentBuilder != null) {
            builder = builder.then(argumentBuilder);

            if (wasPreviousOptional)
                builder = builder.executes(cmd);
        } else
            builder = builder.executes(cmd);

        CommandNode<ServerCommandSource> overwriteCommand = SomeGlobals.commandDispatcher.getRoot().getChild(command.getLabel());
        if (overwriteCommand != null) {
            LOGGER.info("Okyanus: Overwriting a command (/" + command.getLabel() + "). Just letting you know");
            SomeGlobals.commandDispatcher.getRoot().getChildren().remove(overwriteCommand);
        }
        return builder;
    }

}
