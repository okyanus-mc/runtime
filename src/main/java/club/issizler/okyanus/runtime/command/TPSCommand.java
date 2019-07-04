package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmdnew.CommandRunnable;
import club.issizler.okyanus.api.cmdnew.CommandSource;
import club.issizler.okyanus.runtime.SomeGlobals;

public class TPSCommand implements CommandRunnable {

    @Override
    public int run(CommandSource source) {
        boolean isConsole = source.isConsole();
        StringBuilder tps = new StringBuilder();

        if (!isConsole)
            tps.append("§a");

        tps.append("TPS (1m/5m/15m): ");

        if (!isConsole)
            tps.append("§r");

        tps.append(String.format("%.02f", SomeGlobals.tps1.getAverage())).append(", ");
        tps.append(String.format("%.02f", SomeGlobals.tps5.getAverage())).append(", ");
        tps.append(String.format("%.02f", SomeGlobals.tps15.getAverage()));

        source.sendMessage(tps.toString());
        return 1;
    }

}
