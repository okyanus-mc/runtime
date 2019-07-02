package club.issizler.okyanus.api.cmd;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.Optional;

import static club.issizler.okyanus.tests.Tests.RUN_TESTS;
import static club.issizler.okyanus.tests.Tests.tests;

public class CommandSourceImpl {

    private CommandContext<ServerCommandSource> context;

    public CommandSourceImpl(CommandContext<ServerCommandSource> context) {
        this.context = context;
    }

    public boolean isConsole() {
        return context.getSource().getEntity() == null;
    }

    public void send(String string) {
        context.getSource().sendFeedback(new LiteralText(string), false);
    }

    public Optional<Player> getPlayer() {
        try {
            return Optional.of(new PlayerImpl(context.getSource().getPlayer()));
        } catch (CommandSyntaxException e) {
            send(e.toString());
            return Optional.empty();
        }
    }

    public Optional<String> getArgText(String arg) {
        try {
            return Optional.ofNullable(StringArgumentType.getString(context, arg));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<Player> getArgPlayer(String arg) {
        try {
            return Optional.of(
                new PlayerImpl(
                    EntityArgumentType.getPlayer(context, arg)
                )
            );
        } catch (CommandSyntaxException e) {
            send(e.toString());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }



}
