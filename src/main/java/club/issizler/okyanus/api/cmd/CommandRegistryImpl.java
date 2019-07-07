package club.issizler.okyanus.api.cmd;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CommandRegistryImpl implements CommandRegistry {

    private List<CommandBuilder> commands = new ArrayList<>();

    public void register(CommandBuilder cmd) {
        commands.add(cmd);
    }

    public List<CommandBuilder> getCommands() {
        return commands;
    }

}