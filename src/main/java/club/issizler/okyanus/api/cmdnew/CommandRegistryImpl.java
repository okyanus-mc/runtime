package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.cmdnew.mck.MckCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistryImpl implements CommandRegistry {

    private final List<ICommand> commands = new ArrayList<>();

    @Override
    public void register(ICommand cmd) {
        commands.add(cmd);
    }

    @Override
    public ICommand getCommand(String commandLabel) {
        for (ICommand command : commands)
            if (command.getLabel().equals(commandLabel))
                return command;
        return new MckCommand();
    }

    @Override
    public List<ICommand> getCommands() {
        return commands;
    }

}