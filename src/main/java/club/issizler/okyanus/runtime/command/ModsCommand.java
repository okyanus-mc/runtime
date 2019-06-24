package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmd.CommandRunnable;
import club.issizler.okyanus.api.cmd.CommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class ModsCommand implements CommandRunnable {

    @Override
    public int run(CommandSource source) {
        boolean isConsole = source.isConsole();

        StringBuilder mods = new StringBuilder("Mods: ");

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            if (isConsole)
                mods.append("§a");

            mods.append(mod.getMetadata().getName());

            if (isConsole)
                mods.append("§r");

            mods.append(", ");
        }

        source.send(mods.toString());
        return 1;
    }

}
