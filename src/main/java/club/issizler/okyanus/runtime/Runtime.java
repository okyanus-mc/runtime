package club.issizler.okyanus.runtime;

import club.issizler.okyanus.api.Mod;
import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandManager;
import club.issizler.okyanus.runtime.command.ModsCommand;
import club.issizler.okyanus.runtime.command.OkyanusCommand;
import club.issizler.okyanus.runtime.command.TPSCommand;

@SuppressWarnings("unused")
public class Runtime implements Mod {

    @Override
    public void init() {
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
        );    }

}
