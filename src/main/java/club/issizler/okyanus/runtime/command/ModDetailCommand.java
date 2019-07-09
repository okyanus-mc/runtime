package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmdnew.CommandRunnable;
import club.issizler.okyanus.api.cmdnew.CommandSender;
import club.issizler.okyanus.api.cmdnew.CommandSource;
import club.issizler.okyanus.api.cmdnew.ConsoleCommandSender;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ModDetailCommand implements CommandRunnable {

    @Override
    public int run(@NotNull CommandSource source) {
        CommandSender sender = source.getCommandSender();
        final String modId = source.getArgText();

        Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);

        if (!mod.isPresent()) {
            mod = FabricLoader.getInstance().getAllMods().stream().filter(c -> c.getMetadata().getName().equals(modId)).findFirst();

            if (!mod.isPresent()) {
                sender.sendMessage("§cMod not found!");
                return -1;
            }
        }

        ModMetadata metadata = mod.get().getMetadata();

        sender.sendMessage("§a" + metadata.getName() + "§r " + metadata.getVersion());
        sender.sendMessage(metadata.getDescription());
        return 1;
    }

}
