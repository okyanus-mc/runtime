package club.issizler.okyanus.api.cmd;

import com.mojang.brigadier.context.CommandContext;
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

}
