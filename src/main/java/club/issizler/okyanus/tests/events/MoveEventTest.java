package club.issizler.okyanus.tests.events;

import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.MoveEvent;

import static club.issizler.okyanus.tests.Tests.tests;

public class MoveEventTest implements EventHandler<MoveEvent> {

    @Override
    public void handle(MoveEvent event) {
        tests.put("MoveEvent", true);
        tests.put("MoveEvent.getPlayer", event.getPlayer() != null);
    }

}
