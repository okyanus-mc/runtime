package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmd.CommandRunnable;
import club.issizler.okyanus.api.cmd.CommandSource;

import static club.issizler.okyanus.runtime.SomeGlobals.recentTps;

public class TPSCommand implements CommandRunnable {

    @Override
    public int run(CommandSource source) {
        boolean isConsole = source.isConsole();
        StringBuilder tps = new StringBuilder();

        if (isConsole)
            tps.append("§a");

        tps.append("TPS (1m/5m/10m): ");

        if (isConsole)
            tps.append("§r");

        tps.append(String.format("%.02f", recentTps[0])).append(", ");
        tps.append(String.format("%.02f", recentTps[1])).append(", ");
        tps.append(String.format("%.02f", recentTps[2]));

        source.send(tps.toString());
        return 1;
    }

}
