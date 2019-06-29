package club.issizler.okyanus.tests.events;

import club.issizler.okyanus.api.event.DisconnectEvent;
import club.issizler.okyanus.api.event.EventHandler;

import static club.issizler.okyanus.tests.Tests.tests;

public class DisconnectEventTest implements EventHandler<DisconnectEvent> {

    @Override
    public void handle(DisconnectEvent event) {
        tests.put("DisconnectEvent", true);
        tests.put("DisconnectEvent.getPlayer", event.getPlayer() != null);
    }

}
