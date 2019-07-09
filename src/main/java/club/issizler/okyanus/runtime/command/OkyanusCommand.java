package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmdnew.CommandRunnable;
import club.issizler.okyanus.api.cmdnew.CommandSender;
import club.issizler.okyanus.api.cmdnew.CommandSource;
import club.issizler.okyanus.api.cmdnew.ConsoleCommandSender;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

public class OkyanusCommand implements CommandRunnable {

    @Override
    public int run(@NotNull CommandSource source) {
        CommandSender sender = source.getCommandSender();
        boolean isConsole = sender instanceof ConsoleCommandSender;
        FabricLoader loader = FabricLoader.getInstance();

        StringBuilder okyanus = new StringBuilder();

        if (!isConsole)
            okyanus.append("§a");

        okyanus.append("Okyanus ");

        if (!isConsole)
            okyanus.append("§r");

        okyanus.append(loader.getModContainer("okyanus").get().getMetadata().getVersion().getFriendlyString());

        sender.sendMessage(okyanus.toString());
        return 1;
    }

}
