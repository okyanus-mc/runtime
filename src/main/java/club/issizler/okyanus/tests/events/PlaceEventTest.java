package club.issizler.okyanus.tests.events;

import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.MoveEvent;
import club.issizler.okyanus.api.event.PlaceEvent;

import static club.issizler.okyanus.tests.Tests.tests;

public class PlaceEventTest implements EventHandler<PlaceEvent> {

    @Override
    public void handle(PlaceEvent event) {
        tests.put("PlaceEvent", true);
        tests.put("PlaceEvent.getPlayer", event.getPlayer() != null);
        tests.put("PlaceEvent.getLocation", event.getLocation() != null);
    }

}
