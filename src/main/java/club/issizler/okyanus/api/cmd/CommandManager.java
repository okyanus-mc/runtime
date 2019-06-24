package club.issizler.okyanus.api.cmd;

import java.util.ArrayList;
import java.util.List;

public enum CommandManager {
    INSTANCE;

    private List<CommandBuilder> commands = new ArrayList<>();

    public void register(CommandBuilder cmd) {
        commands.add(cmd);
    }

    public List<CommandBuilder> __internal_getCommands() {
        return commands;
    }

}
