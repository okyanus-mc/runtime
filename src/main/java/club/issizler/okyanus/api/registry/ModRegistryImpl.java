package club.issizler.okyanus.api.registry;

import club.issizler.okyanus.api.ModRegistry;
import club.issizler.okyanus.api.cmdnew.Command;
import club.issizler.okyanus.api.event.Event;
import club.issizler.okyanus.api.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class ModRegistryImpl implements ModRegistry {

    private final List<Command> commandList = new ArrayList<>();
    private final List<Event> eventList = new ArrayList<>();

    @Override
    public void registerCommand(Command... commands) {

    }

    @Override
    public void registerEvent(EventHandler... events) {

    }

}
