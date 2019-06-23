package club.issizler.okyanus.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PluginsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("mods").executes(context -> {
            boolean isConsole = context.getSource().getEntity() instanceof ServerPlayerEntity;

            StringBuilder mods = new StringBuilder("Mods: ");

            for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
                if (isConsole)
                    mods.append("§a");

                mods.append(mod.getMetadata().getName());

                if (isConsole)
                    mods.append("§r");

                mods.append(", ");
            }

            context.getSource().sendFeedback(new TextComponent(mods.toString()), false);
            return 1;
        }));
    }

}
