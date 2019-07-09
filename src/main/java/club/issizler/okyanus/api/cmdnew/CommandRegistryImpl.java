package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.runtime.SomeGlobals;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandRegistryImpl implements CommandRegistry {

    @Override
    public void register(@NotNull Command cmd) {
        SomeGlobals.commandMap.register(cmd);
    }

    @Override
    public void unregister(@NotNull Command cmd) {
        SomeGlobals.commandMap.unregister(cmd);
    }

    @NotNull
    @Override
    public Command getCommand(@NotNull String commandLabel) {
        return SomeGlobals.commandMap.getCommand(commandLabel);
    }

    @NotNull
    @Override
    public List<Command> getCommands() {
        return SomeGlobals.commandMap.getCommands();
    }
}