package club.issizler.okyanus.api.cmd;

import club.issizler.okyanus.api.Player;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.TextArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class CommandSource {

    private CommandContext<ServerCommandSource> context;

    public CommandSource(CommandContext<ServerCommandSource> context) {
        this.context = context;
    }

    public boolean isConsole() {
        return context.getSource().getEntity() instanceof ServerPlayerEntity;
    }

    public void send(String string) {
        context.getSource().sendFeedback(new LiteralText(string), false);
    }

    public String getArgText(String arg) {
        return StringArgumentType.getString(context, arg);
    }

    public Player getArgPlayer(String arg) {
        try {
            ServerPlayerEntity e = EntityArgumentType.getPlayer(context, arg);
            return new Player(e);
        } catch (CommandSyntaxException e) {
            send(e.toString());
            throw new RuntimeException(e);
        }
    }

}
