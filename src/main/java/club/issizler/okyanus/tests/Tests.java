package club.issizler.okyanus.tests;

import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandManager;
import club.issizler.okyanus.api.cmd.CommandManagerImpl;
import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.runtime.command.OkyanusCommand;
import club.issizler.okyanus.tests.events.*;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Tests implements DedicatedServerModInitializer {

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
    public void onInitializeServer() {
        RUN_TESTS = !System.getProperty("okyanus.test", "false").equals("false");
        if (!RUN_TESTS)
            return;

        logger.info("Okyanus: Testing is enabled. Trigger every event and run /okyanus test to see the results.");

        // oh god
        ((CommandManagerImpl) CommandManager.getInstance()).__internal_getCommands().forEach(bld -> {
            if (bld.getRunnable() instanceof OkyanusCommand) {
                bld.subCommand(new CommandBuilder()
                        .name("test")
                        .opOnly()
                        .run(new TestCommand())
                );
            }
        });

        tests.put("Mod class init()", true);

        EventManager mgr = EventManager.getInstance();

        mgr.register(new BreakEventTest());
        mgr.register(new ChatEventTest());
        mgr.register(new ConnectEventTest());
        mgr.register(new DisconnectEventTest());
        mgr.register(new DropEventTest());
        mgr.register(new MoveEventTest());
        mgr.register(new PlaceEventTest());
        mgr.register(new ReadyEventTest());
        mgr.register(new StopEventTest());
    }

}
