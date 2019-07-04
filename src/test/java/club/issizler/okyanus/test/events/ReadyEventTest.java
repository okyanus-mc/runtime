package club.issizler.okyanus.test.events;

import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.ReadyEvent;

import static club.issizler.okyanus.test.Tests.tests;

public class ReadyEventTest implements EventHandler<ReadyEvent> {

    @Override
    public void handle(ReadyEvent event) {
        tests.put("ReadyEvent", true);
    }

}
