package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
    public Optional<Player> getPlayer() {
        try {
            return Optional.of(new PlayerImpl(context.getSource().getPlayer()));
        } catch (CommandSyntaxException e) {
            sendMessage(e.toString());
            return Optional.empty();
        }
    }

    @Override
    public void sendMessage(String text) {
        context.getSource().sendFeedback(new LiteralText(text), false);
    }

}
