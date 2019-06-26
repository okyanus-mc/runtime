package club.issizler.okyanus.runtime;

import club.issizler.okyanus.api.Mod;
import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandManager;
import club.issizler.okyanus.runtime.command.ModsCommand;
import club.issizler.okyanus.runtime.command.OkyanusCommand;
import club.issizler.okyanus.runtime.command.TPSCommand;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class Runtime implements Mod {

    private Logger logger = LogManager.getLogger();

    public static boolean USE_FAST_REDSTONE;
    public static boolean USE_FAST_EXPLOSIONS;

    public static FileConfig config;

    @Override
    public void init() {
        config = CommentedFileConfig.builder("okyanus.toml").defaultResource("/config.toml").autosave().build();
        config.load();

        USE_FAST_REDSTONE = Runtime.config.get("optimizations.fastRedstone");
        USE_FAST_EXPLOSIONS = Runtime.config.get("optimizations.fastExplosions");

        if (USE_FAST_REDSTONE)
            logger.warn("Okyanus: Fast redstone is currently experimental! Disable it from okyanus.toml if you have any redstone issues!");

        CommandManager.INSTANCE.register(
                new CommandBuilder()
                        .name("mods")
                        .opOnly()
                        .run(new ModsCommand())
        );

        CommandManager.INSTANCE.register(
                new CommandBuilder()
                        .name("tps")
                        .opOnly()
                        .run(new TPSCommand())
        );

        CommandManager.INSTANCE.register(
                new CommandBuilder()
                        .name("okyanus")
                        .opOnly()
                        .run(new OkyanusCommand())
        );
    }

}
