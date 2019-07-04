package club.issizler.okyanus.test.events;

import club.issizler.okyanus.api.event.ChatEvent;
import club.issizler.okyanus.api.event.EventHandler;

import static club.issizler.okyanus.test.Tests.tests;

public class ChatEventTest implements EventHandler<ChatEvent> {

    @Override
    public void handle(ChatEvent event) {
        tests.put("ChatEvent", true);
        tests.put("ChatEvent.getPlayer", event.getPlayer() != null);
        tests.put("ChatEvent.getMessage", event.getMessage() != null);
        tests.put("ChatEvent.getFormattedMessage", event.getFormattedMessage() != null);
    }

}
