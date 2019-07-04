package club.issizler.okyanus.test.events;

import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.PlaceEvent;

import static club.issizler.okyanus.test.Tests.tests;

public class PlaceEventTest implements EventHandler<PlaceEvent> {

    @Override
    public void handle(PlaceEvent event) {
        tests.put("PlaceEvent", true);
        tests.put("PlaceEvent.getPlayer", event.getPlayer() != null);
        tests.put("PlaceEvent.getLocation", event.getLocation() != null);
    }

}
