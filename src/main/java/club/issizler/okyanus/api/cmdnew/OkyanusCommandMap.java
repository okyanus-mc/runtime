package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.runtime.SomeGlobals;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class OkyanusCommandMap {

    private final Map<String, ICommand> knowCommands = new HashMap<>();

    private final Server server = Okyanus.getServer();
    private final Logger logger;

    public OkyanusCommandMap(Logger logger) {
        this.logger = logger;
    }

    public void registerAll() {
        for (ICommand command : server.getCommandRegistry().getCommands()) {
            if (command.getLabel().equals("")) {
                logger.error("Command which has id '" + command.getId() + "' can't registered because of the label didn't defined!");
                continue;
            }

            register(command.getLabel(), command);

            LiteralArgumentBuilder<ServerCommandSource> builder = literal(command.getLabel());
            builder = registerBuilder(command, 0, builder);

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

    private LiteralArgumentBuilder<ServerCommandSource> registerBuilder(
        ICommand command,
        int location,
        LiteralArgumentBuilder<ServerCommandSource> builder
    ) {

        // ALGORITHM
        literal("foo")
            .then(
                argument("bar", integer())
                    .executes(c -> {
                        System.out.println("Bar is " + getInteger(c, "bar"));
                        return 1;
                    })
            )
            .executes(c -> {
                System.out.println("Called foo with no arguments");
                return 1;
            });

        CommandNode<ServerCommandSource> overwriteCommand = SomeGlobals.commandDispatcher.getRoot().getChild(command.getLabel());
        if (overwriteCommand != null) {
            logger.info("Okyanus: Overwriting a command (/" + command.getLabel() + "). Just letting you know");
            SomeGlobals.commandDispatcher.getRoot().getChildren().remove(overwriteCommand);
        }

        return builder;
    }
}