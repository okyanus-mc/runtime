package club.issizler.okyanus.test;

import club.issizler.okyanus.api.Mod;
import club.issizler.okyanus.test.events.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Tests extends Mod {

    public static boolean RUN_TESTS = false;
    public static Map<String, Boolean> tests = new HashMap<String, Boolean>() {{
        put("Mod class init()", false);

        put("Command execution", false);
        put("Subcommand execution", false);

        put("BreakEvent", false);
        put("BreakEvent.getPlayer", false);
        put("BreakEvent.getLocation", false);

        put("ChatEvent", false);
        put("ChatEvent.getPlayer", false);
        put("ChatEvent.getMessage", false);
        put("ChatEvent.getFormattedMessage", false);

        put("ConnectEvent", false);
        put("ConnectEvent.getPlayer", false);

        put("DisconnectEvent", false);
        put("DisconnectEvent.getPlayer", false);

        put("DropEvent", false);
        put("DropEvent.getPlayer", false);

        put("MoveEvent", false);
        put("MoveEvent.getPlayer", false);

        put("PlaceEvent", false);
        put("PlaceEvent.getPlayer", false);
        put("PlaceEvent.getLocation", false);

        put("ReadyEvent", false);
        put("StopEvent", false);
    }};

    private Logger logger = LogManager.getLogger();

    @Override
    public void init() {
        RUN_TESTS = !System.getProperty("okyanus.test", "false").equals("false");
        if (!RUN_TESTS)
            return;

        logger.info("Okyanus: Testing is enabled. Trigger every event and run /okyanus test to see the results.");
        tests.put("Mod class init()", true);

        registerEvent(new BreakEventTest());
        registerEvent(new ChatEventTest());
        registerEvent(new ConnectEventTest());
        registerEvent(new DisconnectEventTest());
        registerEvent(new DropEventTest());
        registerEvent(new MoveEventTest());
        registerEvent(new PlaceEventTest());
        registerEvent(new ReadyEventTest());
        registerEvent(new StopEventTest());
    }

}
