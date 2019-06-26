package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmd.CommandRunnable;
import club.issizler.okyanus.api.cmd.CommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class OkyanusCommand implements CommandRunnable {

    @Override
    public int run(CommandSource source) {
        boolean isConsole = source.isConsole();
        FabricLoader loader = FabricLoader.getInstance();

        StringBuilder okyanus = new StringBuilder();

        if (isConsole)
            okyanus.append("§a");

        okyanus.append("Okyanus Loader ");

        if (isConsole)
            okyanus.append("§r");

        okyanus.append(loader.getModContainer("okyanus").get().getMetadata().getVersion().getFriendlyString());

        source.send(okyanus.toString());
        okyanus = new StringBuilder();

        if (isConsole)
            okyanus.append("§a");

        okyanus.append("Okyanus Runtime ");

        if (isConsole)
            okyanus.append("§r");

        okyanus.append(loader.getModContainer("okyanus-runtime").get().getMetadata().getVersion().getFriendlyString());

        source.send(okyanus.toString());
        return 1;
    }

}