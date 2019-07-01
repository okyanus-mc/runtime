package club.issizler.okyanus.api.registry;

import club.issizler.okyanus.api.cmdnew.Command;
import org.cactoos.list.ListOf;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistryImpl implements CommandRegistry {

    private final List<Command> commandList = new ArrayList<>();

    @Override
    public void registerCommand(Command... commands) {
        commandList.addAll(new ListOf<>(commands));
    }

}
