package club.issizler.okyanus.api.newcmd;

import club.issizler.okyanus.api.cmdnew.Command;
import club.issizler.okyanus.api.cmdnew.cmd.CommandRegistry;

import java.util.ArrayList;
import java.util.List;

public class OkyanusCommandRegistry implements CommandRegistry {

    private List<Command> commands = new ArrayList<>();

    // Override
    public void register(Command cmd) {
        commands.add(cmd);
    }

    // Override
    public List<Command> getCommandList() {
        return commands;
    }

}