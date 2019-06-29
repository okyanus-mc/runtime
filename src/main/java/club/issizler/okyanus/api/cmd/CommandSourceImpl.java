package club.issizler.okyanus.api.cmd;

import club.issizler.okyanus.api.entity.EntityImpl;
import club.issizler.okyanus.api.entity.PlayerImpl;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static club.issizler.okyanus.tests.Tests.RUN_TESTS;
import static club.issizler.okyanus.tests.Tests.tests;

public class CommandSourceImpl implements CommandSource {

    private CommandContext<ServerCommandSource> context;

    public CommandSourceImpl(CommandContext<ServerCommandSource> context) {
        if (RUN_TESTS && !tests.get("Command execution"))
            tests.put("Command execution", true);

        this.context = context;
    }

    public boolean isConsole() {
        return context.getSource().getEntity() == null;
    }

    public void send(String string) {
        context.getSource().sendFeedback(new LiteralText(string), false);
    }

    public PlayerImpl getPlayer() {
        try {
            return new PlayerImpl(
                context.getSource().getPlayer(),
                new EntityImpl(context.getSource().getPlayer())
            );
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getArgText(String arg) {
        try {
            return StringArgumentType.getString(context, arg);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public PlayerImpl getArgPlayer(String arg) {
        try {
            ServerPlayerEntity e = EntityArgumentType.getPlayer(context, arg);
            return new PlayerImpl(
                e,
                new EntityImpl(e)
            );
        } catch (CommandSyntaxException e) {
            send(e.toString());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
