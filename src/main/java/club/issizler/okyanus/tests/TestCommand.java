package club.issizler.okyanus.tests;

import club.issizler.okyanus.api.cmd.CommandRunnable;
import club.issizler.okyanus.api.cmd.CommandSource;

import static club.issizler.okyanus.tests.Tests.tests;

public class TestCommand implements CommandRunnable {

    @Override
    public int run(CommandSource source) {
        tests.put("Subcommand execution", true);

        for (String line : generateReport(!source.isConsole()).split("\n")) {
            source.send(line);
        }

        return 1;
    }

    public static String generateReport(boolean colored) {
        String GRN = colored ? "§a" : "";
        String RED = colored ? "§c" : "";

        StringBuilder bld = new StringBuilder(GRN+"OkyanusAPI Tests\n");

        tests.forEach((test, value) -> {
            if (value) {
                bld.append(test).append(GRN).append(" works");
            } else {
                bld.append(test).append(RED).append(" doesn't work or untested");
            }

            bld.append("\n");
        });

        bld.append(RED).append("doesn't work or untested ").append(GRN).append("tests might still work if you\n");
        bld.append(GRN).append("execute those events");

        return bld.toString();
    }

}
