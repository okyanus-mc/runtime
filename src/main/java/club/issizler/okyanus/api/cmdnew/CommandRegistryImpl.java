package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.runtime.SomeGlobals;

import java.util.List;

public class CommandRegistryImpl implements CommandRegistry {

    @Override
    public void register(Command cmd) {
        SomeGlobals.commandMap.register(cmd);
    }

    @Override
    public void unregister(Command cmd) {
        SomeGlobals.commandMap.unregister(cmd);
    }

    @Override
    public Command getCommand(String commandLabel) {
        return SomeGlobals.commandMap.getCommand(commandLabel);
    }

    @Override
    public List<Command> getCommands() {
        return SomeGlobals.commandMap.getCommands();
    }
}