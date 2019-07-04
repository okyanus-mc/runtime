package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.Optional;

public class OkyanusCommandSource implements CommandSource {

    private final CommandContext<ServerCommandSource> context;

    public OkyanusCommandSource(CommandContext<ServerCommandSource> context) {
        this.context = context;
    }

    @Override
    public boolean isConsole() {
        return context.getSource().getEntity() == null;
    }

    @Override
    public void sendMessage(String text) {
        context.getSource().sendFeedback(new LiteralText(text), false);
    }

    @Override
    public String getName() {
        return context.getSource().getName();
    }

    @Override
    public Optional<String> getArgText(String arg) {
        try {
            return Optional.ofNullable(StringArgumentType.getString(context, arg));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Player> getArgPlayer(String arg) {
        try {
            return Optional.of(
                new PlayerImpl(
                    EntityArgumentType.getPlayer(context, arg)
                )
            );
        } catch (CommandSyntaxException e) {
            sendMessage(e.toString());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
