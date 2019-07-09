package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.cmdnew.mck.MckCommand;
import club.issizler.okyanus.api.cmdnew.mck.MckCommandRunnable;
import club.issizler.okyanus.api.cmdnew.req.AndReq;
import club.issizler.okyanus.api.cmdnew.req.Requirement;
import club.issizler.okyanus.runtime.SomeGlobals;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Logger;
import org.cactoos.list.ListOf;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class OkyanusCommandMap {

    private final Map<String, Command> knownCommands = new HashMap<>();
    private final Logger logger;

    private boolean started = false;

    public OkyanusCommandMap(Logger logger) {
        this.logger = logger;
    }

    public void registerAll() {
        for (Command command : knownCommands.values()) {
            if (command.getLabel().isEmpty()) {
                logger.error("Command with id '" + command.getId() + "' can't registered because the label isn't defined!");
                continue;
            }

            register(command);

            LiteralArgumentBuilder<ServerCommandSource> builder =
                (LiteralArgumentBuilder<ServerCommandSource>) registerBuilder(command, 0);

            SomeGlobals.commandDispatcher.register(builder);
        }
        started = true;
    }

    void register(Command command) {
        if (started) {
            LiteralArgumentBuilder<ServerCommandSource> builder =
                (LiteralArgumentBuilder<ServerCommandSource>) registerBuilder(command, 0);

            SomeGlobals.commandDispatcher.register(builder);
        }

        String label = command.getLabel().toLowerCase(Locale.ENGLISH).trim();
        register(label, command, false);

        command.getAliases().removeIf(alias -> !register(alias, command, true));
    }

    Command getCommand(String label) {
        return knownCommands.getOrDefault(label, new MckCommand());
    }

    List<Command> getCommands() {
        return new ListOf<>(knownCommands.values());
    }

    void unregister(Command command) {
        command.setActive(false);
        knownCommands.remove(command.getLabel());
    }

    private synchronized boolean register(String label, Command command, boolean isAlias) {
        if (isAlias && knownCommands.containsKey(label))
            return false;

        Command conflict = knownCommands.get(label);
        if (conflict != null && conflict.getLabel().equals(label))
            return false;

        knownCommands.put(label, command);
        return true;
    }

    private ArgumentBuilder registerBuilder(
        Command command,
        int location
    ) {

        // Defining
        final String id = command.getId();
        final String label = command.getLabel();
        final boolean isArg = label.equals("") || label.equals(" ") || label.isEmpty();
        final CommandRunnable run = command.getRunnable();
        final List<Requirement> requirements = command.getRequirements();
        final List<Command> subCommands = command.getSubCommands();
        final com.mojang.brigadier.arguments.ArgumentType type;
        switch (command.getType()) {
            case PLAYER:
                type = EntityArgumentType.players();
                break;
            case GREEDY_TEXT:
                type = StringArgumentType.greedyString();
                break;
            case TEXT:
            case NONE:
            default:
                type = StringArgumentType.string();
                break;
        }
        final ArgumentBuilder finalBuilder = isArg ? argument(id, type) : literal(label);
        final com.mojang.brigadier.Command<ServerCommandSource> cmd = context -> {
            final String[] inputs = context.getInput().split(" ");
            final CommandSource commandSource = new CommandSourceImpl(context, command.getId());
            finalBuilder.requires(
                o -> command.isActive() &&
                    new AndReq(requirements, commandSource.getCommandSender(), inputs, location).test((ServerCommandSource) o)
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
            logger.info("Okyanus: Overwriting a command (/" + command.getLabel() + "). Just letting you know");
            SomeGlobals.commandDispatcher.getRoot().getChildren().remove(overwriteCommand);
        }

        return finalBuilder;
    }
}
