package club.issizler.okyanus.api.registry;

import club.issizler.okyanus.api.EventRegistry;
import club.issizler.okyanus.api.event.EventHandler;
import org.cactoos.list.ListOf;

import java.util.ArrayList;
import java.util.List;

public class EventRegistryImpl implements EventRegistry {

    private final List<EventHandler> eventList = new ArrayList<>();

    @Override
    public void registerEvent(EventHandler... events) {
        eventList.addAll(new ListOf<>(events));
    }

}
