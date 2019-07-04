package club.issizler.okyanus.test.events;

import club.issizler.okyanus.api.event.ConnectEvent;
import club.issizler.okyanus.api.event.EventHandler;

import static club.issizler.okyanus.test.Tests.tests;

public class ConnectEventTest implements EventHandler<ConnectEvent> {

    @Override
    public void handle(ConnectEvent event) {
        tests.put("ConnectEvent", true);
        tests.put("ConnectEvent.getPlayer", event.getPlayer() != null);
    }

}
