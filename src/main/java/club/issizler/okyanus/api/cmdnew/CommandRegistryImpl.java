package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.cmdnew.mck.MckCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistryImpl implements CommandRegistry {

    private final List<Command> commands = new ArrayList<>();

    @Override
    public void register(Command cmd) {
        commands.add(cmd);
    }

    @Override
    public Command getCommand(String commandLabel) {
        for (Command command : commands)
            if (command.getLabel().equals(commandLabel))
                return command;
        return new MckCommand();
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

}