package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmdnew.CommandRunnable;
import club.issizler.okyanus.api.cmdnew.CommandSender;
import club.issizler.okyanus.api.cmdnew.CommandSource;
import club.issizler.okyanus.api.cmdnew.ConsoleCommandSender;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;

public class ModsCommand implements CommandRunnable {

    @Override
    public int run(@NotNull CommandSource source) {
        CommandSender sender = source.getCommandSender();
        boolean isConsole = sender instanceof ConsoleCommandSender;

        StringBuilder mods = new StringBuilder("Mods: ");

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            if (!isConsole)
                mods.append("§a");

            mods.append(mod.getMetadata().getName());

            if (!isConsole)
                mods.append("§r");

            mods.append(", ");
        }

        sender.sendMessage(mods.toString());
        return 1;
    }

}
