package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmdnew.CommandRunnable;
import club.issizler.okyanus.api.cmdnew.CommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ModDetailCommand implements CommandRunnable {

    @Override
    public int run(@NotNull CommandSource source) {
        final String modId = source.getArgText();

        Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

        if (!mod.isPresent()) {
            mod = FabricLoader.getInstance().getAllMods().stream().filter(c -> c.getMetadata().getName().equals(modId)).findFirst();

            if (!mod.isPresent()) {
                source.sendMessage("§cMod not found!");
                return -1;
            }
        }

        ModMetadata metadata = mod.get().getMetadata();

        source.sendMessage("§a" + metadata.getName() + "§r " + metadata.getVersion());
        source.sendMessage(metadata.getDescription());
        return 1;
    }

}
