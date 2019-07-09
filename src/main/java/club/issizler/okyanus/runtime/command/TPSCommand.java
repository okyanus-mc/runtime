package club.issizler.okyanus.runtime.command;

import club.issizler.okyanus.api.cmdnew.CommandRunnable;
import club.issizler.okyanus.api.cmdnew.CommandSender;
import club.issizler.okyanus.api.cmdnew.CommandSource;
import club.issizler.okyanus.api.cmdnew.ConsoleCommandSender;
import club.issizler.okyanus.runtime.SomeGlobals;
import org.jetbrains.annotations.NotNull;

public class TPSCommand implements CommandRunnable {

    @Override
    public int run(@NotNull CommandSource source) {
        CommandSender sender = source.getCommandSender();
        boolean isConsole = sender instanceof ConsoleCommandSender;
        StringBuilder tps = new StringBuilder();

        if (!isConsole)
            tps.append("§a");

        tps.append("TPS (1m/5m/15m): ");

        if (!isConsole)
            tps.append("§r");

        tps.append(String.format("%.02f", SomeGlobals.tps1.getAverage())).append(", ");
        tps.append(String.format("%.02f", SomeGlobals.tps5.getAverage())).append(", ");
        tps.append(String.format("%.02f", SomeGlobals.tps15.getAverage()));

        sender.sendMessage(tps.toString());
        return 1;
    }

}
