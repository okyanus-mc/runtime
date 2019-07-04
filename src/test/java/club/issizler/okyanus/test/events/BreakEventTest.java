package club.issizler.okyanus.test.events;

import club.issizler.okyanus.api.event.BreakEvent;
import club.issizler.okyanus.api.event.EventHandler;

import static club.issizler.okyanus.test.Tests.tests;

public class BreakEventTest implements EventHandler<BreakEvent> {

    @Override
    public void handle(BreakEvent event) {
        tests.put("BreakEvent", true);
        tests.put("BreakEvent.getPlayer", event.getPlayer() != null);
        tests.put("BreakEvent.getLocation", event.getLocation() != null);
    }

}
