package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.entity.Entity;
import club.issizler.okyanus.api.entity.EntityImpl;
import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.entity.mck.MckEntity;
import club.issizler.okyanus.api.entity.mck.MckPlayer;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class CommandSourceImpl implements CommandSource {

    private final CommandContext<ServerCommandSource> context;
    private final String arg;

    public CommandSourceImpl(CommandContext<ServerCommandSource> context, String arg) {
        this.context = context;
        this.arg = arg;
    }

    @Override
    public Entity getEntity() {
        final net.minecraft.entity.Entity entity = context.getSource().getEntity();

        if (entity == null)
            return new MckEntity();

        return new EntityImpl(context.getSource().getEntity());
    }

    @Override
    public Player getPlayer() {
        final net.minecraft.entity.Entity entity = context.getSource().getEntity();

        if (entity == null)
            return new MckPlayer();

        if (!(entity instanceof ServerPlayerEntity))
            return new MckPlayer();

        return new PlayerImpl((ServerPlayerEntity) entity);
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
        return getArgText(arg);
    }

    @Override
    public Player getArgPlayer() {
        return getArgPlayer(arg);
    }

    @Override
    public String getArgText(String commandId) {
        try {
            return StringArgumentType.getString(context, commandId);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    @Override
    public Player getArgPlayer(String commandId) {
        try {
            return new PlayerImpl(EntityArgumentType.getPlayer(context, commandId));
        } catch (CommandSyntaxException e) {
            sendMessage(e.toString());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return new MckPlayer();
        }
    }
}
