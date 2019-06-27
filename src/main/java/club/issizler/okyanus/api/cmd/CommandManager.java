package club.issizler.okyanus.api.cmd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public enum CommandManager {
    INSTANCE;

    private List<CommandBuilder> commands = new ArrayList<>();
    private Logger logger = LogManager.getLogger();

    public void register(CommandBuilder cmd) {
        logger.debug("Okyanus: Registering /" + cmd.__internal_name() + " with " + cmd.__internal_args().size() + " args");
        commands.add(cmd);
    }

    public List<CommandBuilder> __internal_getCommands() {
        return commands;
    }

}
