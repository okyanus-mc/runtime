package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.cmdnew.mck.MckCommandRunnable;
import club.issizler.okyanus.api.cmdnew.req.AndReq;
import club.issizler.okyanus.runtime.SomeGlobals;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class OkyanusCommandMap {

    private final Map<String, ICommand> knowCommands = new HashMap<>();

    private final Server server = Okyanus.getServer();
    private final Logger logger;

    public OkyanusCommandMap(Logger logger) {
        this.logger = logger;
    }

    public void registerAll() {
        for (ICommand command : server.getCommandRegistry().getCommands()) {
            if (command.getLabel().isEmpty()) {
                logger.error("Command which has id '" + command.getId() + "' can't registered because of the label didn't defined!");
                continue;
            }

            register(command.getLabel(), command);

            LiteralArgumentBuilder<ServerCommandSource> builder =
                (LiteralArgumentBuilder<ServerCommandSource>) registerBuilder(command, 0);

            SomeGlobals.commandDispatcher.register(builder);
        }
    }

    private void register(String label, ICommand command) {
        label = label.toLowerCase(Locale.ENGLISH).trim();
        register(label, command, false);

        command.getAliases().removeIf(alias -> !register(alias, command, true));
    }

    private synchronized boolean register(String label, ICommand command, boolean isAlias) {
        knowCommands.put(label, command);
        if (isAlias && knowCommands.containsKey(label))
            return false;

        ICommand conflict = knowCommands.get(label);
        if (conflict != null && conflict.getLabel().equals(label))
            return false;

        knowCommands.put(label, command);
        return true;
    }

    private ArgumentBuilder registerBuilder(
        ICommand command,
        int location
    ) {

        // Defining
        final String id = command.getId();
        final String label = command.getLabel();
        final boolean isArg = label.equals("") || label.equals(" ") || label.isEmpty();
        final CommandRunnable run = command.getRunnable();
        final List<Requirement> requirements = command.getRequirements();
        final List<ICommand> subCommands = command.getSubCommands();
        final com.mojang.brigadier.arguments.ArgumentType type;
        switch (command.getType()) {
            case PLAYER:
                type = EntityArgumentType.players();
                break;
            case TEXT:
            case NONE:
            default:
                type = StringArgumentType.string();
                break;
        }
        final ArgumentBuilder finalBuilder = isArg ? argument(id, type) : literal(label);
        final Command<ServerCommandSource> cmd = context -> {
            final CommandSource commandSource = new OkyanusCommandSource(context);
            final String[] inputs = context.getInput().split(" ");
            finalBuilder.requires(
                new AndReq(requirements, commandSource, inputs, location)
            );
            return run.run(commandSource);
        };
        // Defining

        if (!(run instanceof MckCommandRunnable))
            finalBuilder.executes(cmd);

        subCommands.forEach(iCommand -> {
            finalBuilder.then(
                registerBuilder(iCommand, location + 1)
            );
        });

        CommandNode<ServerCommandSource> overwriteCommand = SomeGlobals.commandDispatcher.getRoot().getChild(command.getLabel());
        if (overwriteCommand != null) {
            logger.warn("Okyanus: Overwriting a command (/" + command.getLabel() + "). Just letting you know");
            SomeGlobals.commandDispatcher.getRoot().getChildren().remove(overwriteCommand);
        }

        return finalBuilder;
    }
}
