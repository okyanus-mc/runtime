package club.issizler.okyanus.api.cmdnew;

import java.util.ArrayList;
import java.util.List;

public class OkyanusCommandRegistry implements CommandRegistry {

    private List<ICommand> commands = new ArrayList<>();

    // Override
    public void register(ICommand cmd) {
        commands.add(cmd);
    }

    // Override
    public List<ICommand> getCommandList() {
        return commands;
    }

}