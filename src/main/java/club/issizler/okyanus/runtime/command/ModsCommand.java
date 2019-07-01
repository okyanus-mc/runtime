package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmd.CommandRunnable;
import club.issizler.okyanus.api.cmd.CommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.Optional;

public class ModsCommand implements CommandRunnable {

    @Override
    public int run(CommandSource source) {
        boolean isConsole = source.isConsole();

        return source.getArgText("modId")
                .map(id -> singleMod(id, source, isConsole))
                .orElseGet(() -> allMods(source, isConsole));
    }

    private int singleMod(String modId, CommandSource source, boolean isConsole) { // TODO: Ignore color codes in console
        Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

        if (!mod.isPresent()) {
            mod = FabricLoader.getInstance().getAllMods().stream().filter(c -> c.getMetadata().getName().equals(modId)).findFirst();

            if (!mod.isPresent()) {
                source.send("§cMod not found!");
                return -1;
            }
        }

        ModMetadata metadata = mod.get().getMetadata();

        source.send("§a" + metadata.getName() + "§r " + metadata.getVersion());
        source.send(metadata.getDescription());
        return 1;
    }

    private int allMods(CommandSource source, boolean isConsole) {
        StringBuilder mods = new StringBuilder("Mods: ");

        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            if (!isConsole)
                mods.append("§a");

            mods.append(mod.getMetadata().getName());

            if (!isConsole)
                mods.append("§r");

            mods.append(", ");
        }

        source.send(mods.toString());
        return 1;
    }

}
