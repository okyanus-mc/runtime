package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.entity.mck.MckPlayer;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class CommandSourceImpl implements CommandSource {

    private final CommandContext<ServerCommandSource> context;
    private final String arg;

    public CommandSourceImpl(CommandContext<ServerCommandSource> context, String arg) {
        this.context = context;
        this.arg = arg;
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
    public String getArgText() {
        try {
            return StringArgumentType.getString(context, arg);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    @Override
    public Player getArgPlayer() {
        try {
            return new PlayerImpl(EntityArgumentType.getPlayer(context, arg));
        } catch (CommandSyntaxException e) {
            sendMessage(e.toString());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return new MckPlayer();
        }
    }

}
