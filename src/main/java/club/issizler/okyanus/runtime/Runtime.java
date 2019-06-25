package club.issizler.okyanus.runtime;

import club.issizler.okyanus.api.Mod;
import club.issizler.okyanus.api.chat.MessageType;
import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandManager;
import club.issizler.okyanus.api.event.ChatEvent;
import club.issizler.okyanus.api.event.Event;
import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.runtime.command.ModsCommand;
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
    }

}