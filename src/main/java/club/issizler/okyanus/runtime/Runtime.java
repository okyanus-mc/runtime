package club.issizler.okyanus.runtime;

import club.issizler.okyanus.api.Mod;
import club.issizler.okyanus.api.chat.ChatColor;
import club.issizler.okyanus.api.cmd.ArgumentType;
import club.issizler.okyanus.api.cmdnew.CommandOf;
import club.issizler.okyanus.api.cmdnew.req.OnlinePlayerReq;
import club.issizler.okyanus.api.cmdnew.req.OpReq;
import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.runtime.command.ModDetailCommand;
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

import java.io.File;

import static club.issizler.okyanus.api.cmdnew.CommandOptions.*;

public class Runtime extends Mod {

    public static boolean DEBUG;
    public static FileConfig config;
    private Logger logger = LogManager.getLogger();

    @Override
    public void init() {
        config = CommentedFileConfig.builder("okyanus.toml").defaultResource("/config.toml").autosave().build();
        config.load();

        DEBUG = config.get("debug");
        if (DEBUG) {
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            Configuration config = ctx.getConfiguration();
            LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
            loggerConfig.setLevel(Level.TRACE);
            ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.

            logger.debug("Okyanus: Debugging enabled");
        }

        File configFolder = new File("./config/");
        if (!configFolder.exists())
            configFolder.mkdir();

        registerCommand(
            new CommandOf(
                "title-command-id",
                label("totle"),
                subCommands(
                    new CommandOf(
                        "player-id",
                        type(ArgumentType.PLAYER),
                        requirements(
                            new OnlinePlayerReq()
                        ),
                        subCommands(
                            new CommandOf(
                                "title-message",
                                type(ArgumentType.GREEDY_TEXT),
                                run(source -> {
                                    Player target = source.getArgPlayer("player-id");

                                    target.sendTitle(source.getArgText(), "", 20, 20, 20);

                                    return 1;
                                })
                            )
                        )
                    )
                )
            )
        );

        registerCommand(
            new CommandOf(
                "tps",
                label("tps"),
                requirements(
                    new OpReq()
                ),
                run(new TPSCommand())
            )
        );

        registerCommand(
            new CommandOf(
                "okyanus",
                label("okyanus"),
                requirements(
                    new OpReq()
                ),
                run(new OkyanusCommand()),
                subCommands(
                    new CommandOf(
                        "mods",
                        label("mods"),
                        run(new ModsCommand()),
                        subCommands(
                            new CommandOf(
                                "modId",
                                type(ArgumentType.GREEDY_TEXT),
                                run(new ModDetailCommand())
                            )
                        )
                    )
                )
            )
        );
    }

}