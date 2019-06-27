package club.issizler.okyanus.runtime;

import club.issizler.okyanus.api.Mod;
import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandManager;
import club.issizler.okyanus.runtime.command.ModsCommand;
import club.issizler.okyanus.runtime.command.OkyanusCommand;
import club.issizler.okyanus.runtime.command.TPSCommand;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

@SuppressWarnings("unused")
public class Runtime implements Mod {

    private Logger logger = LogManager.getLogger();

    public static boolean DEBUG;

    public static boolean USE_FAST_REDSTONE;
    public static boolean USE_FAST_EXPLOSIONS;

    public static int PACKET_RATE_LIMIT;
    public static double PACKET_RATE_LIMIT_INTERVAL;

    public static FileConfig config;

    @Override
    public void init() {
        config = CommentedFileConfig.builder("okyanus.toml").defaultResource("/config.toml").autosave().build();
        config.load();

        USE_FAST_REDSTONE = config.get("optimizations.fastRedstone");
        USE_FAST_EXPLOSIONS = config.get("optimizations.fastExplosions");

        PACKET_RATE_LIMIT = config.get("limits.packetRateLimit");
        PACKET_RATE_LIMIT_INTERVAL = config.get("limits.packetRateLimitInterval");

        DEBUG = config.get("debug");
        if (DEBUG) {
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            Configuration config = ctx.getConfiguration();
            LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
            loggerConfig.setLevel(Level.TRACE);

            ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.

            logger.debug("Okyanus: Debugging enabled");
        }

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
