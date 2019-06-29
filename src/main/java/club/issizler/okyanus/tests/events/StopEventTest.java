package club.issizler.okyanus.tests.events;

import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.StopEvent;
import club.issizler.okyanus.tests.TestCommand;

import static club.issizler.okyanus.tests.Tests.tests;

public class StopEventTest implements EventHandler<StopEvent> {

    @Override
    public void handle(StopEvent event) {
        tests.put("StopEvent", true);

        System.out.println(TestCommand.generateReport(false));
    }

}
