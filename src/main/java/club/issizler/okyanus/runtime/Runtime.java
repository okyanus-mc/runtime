package club.issizler.okyanus.runtime;

import club.issizler.okyanus.api.Mod;
import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandManager;
import club.issizler.okyanus.runtime.command.ModsCommand;
import club.issizler.okyanus.runtime.command.OkyanusCommand;
import club.issizler.okyanus.runtime.command.TPSCommand;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;

@SuppressWarnings("unused")
public class Runtime implements Mod {

    public static FileConfig config;

    @Override
    public void init() {
        config = CommentedFileConfig.builder("okyanus.toml").defaultResource("/config.toml").autosave().build();
        config.load();

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
