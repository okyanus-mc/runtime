package club.issizler.okyanus.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static club.issizler.okyanus.ServerStatus.recentTps;

public class TPSCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tps")
                .requires(source -> source.hasPermissionLevel(3))
                .executes(context -> {
                    boolean isConsole = context.getSource().getEntity() instanceof ServerPlayerEntity;
                    StringBuilder tps = new StringBuilder();

                    if (isConsole)
                        tps.append("§a");

                    tps.append("TPS (1m/5m/10m): ");

                    if (isConsole)
                        tps.append("§r");

                    tps.append(String.format("%.02f", recentTps[0])).append(", ");
                    tps.append(String.format("%.02f", recentTps[1])).append(", ");
                    tps.append(String.format("%.02f", recentTps[2]));

                    context.getSource().sendFeedback(new TextComponent(tps.toString()), false);
                    return 1;
                }));
    }

}
