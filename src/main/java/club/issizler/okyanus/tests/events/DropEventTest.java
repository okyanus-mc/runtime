package club.issizler.okyanus.tests.events;

import club.issizler.okyanus.api.event.DropEvent;
import club.issizler.okyanus.api.event.EventHandler;

import static club.issizler.okyanus.tests.Tests.tests;

public class DropEventTest implements EventHandler<DropEvent> {

    @Override
    public void handle(DropEvent event) {
        tests.put("DropEvent", true);
        tests.put("DropEvent.getPlayer", event.getPlayer() != null);
    }

}
