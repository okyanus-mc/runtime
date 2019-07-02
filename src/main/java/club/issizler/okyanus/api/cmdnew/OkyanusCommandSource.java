package club.issizler.okyanus.api.cmdnew;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class OkyanusCommandSource implements CommandSource {

    private final CommandContext<ServerCommandSource> context;

    public OkyanusCommandSource(CommandContext<ServerCommandSource> context) {
        this.context = context;
    }

    @Override
    public void sendMessage(String text) {
        context.getSource().sendFeedback(new LiteralText(text), false);
    }

    @Override
    public String getName() {
        return context.getSource().getName();
    }

}
