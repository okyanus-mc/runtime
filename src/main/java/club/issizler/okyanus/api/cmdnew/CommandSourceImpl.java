package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.entity.EntityImpl;
import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.entity.mck.MckPlayer;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

public class CommandSourceImpl implements CommandSource {

    private final CommandContext<ServerCommandSource> context;
    private final String arg;

    CommandSourceImpl(@NotNull final CommandContext<ServerCommandSource> context,
                      @NotNull final String arg) {
        this.context = context;
        this.arg = arg;
    }

    @NotNull
    @Override
    public String getArgText() {
        return getArgText(arg);
    }

    @NotNull
    @Override
    public Player getArgPlayer() {
        return getArgPlayer(arg);
    }

    @NotNull
    @Override
    public String getArgText(@NotNull String commandId) {
        try {
            return StringArgumentType.getString(context, commandId);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    @NotNull
    @Override
    public Player getArgPlayer(@NotNull String commandId) {
        try {
            return new PlayerImpl(EntityArgumentType.getPlayer(context, commandId));
        } catch (CommandSyntaxException e) {
            getCommandSender().sendMessage(e.toString());
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return new MckPlayer();
        }
    }

    @NotNull
    @Override
    public CommandSender getCommandSender() {
        if (context.getSource().getEntity() == null)
            return Okyanus.getServer().getConsoleSender();

        try {
            return new PlayerImpl(context.getSource().getPlayer());
        } catch (CommandSyntaxException e) {
            // Ignore...
        }

        return new EntityImpl(context.getSource().getEntity());
    }

}
