package club.issizler.okyanus.runtime;

import club.issizler.okyanus.api.cmdnew.OkyanusCommandMap;
import club.issizler.okyanus.runtime.utils.RollingAverage;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class SomeGlobals {

    public static final RollingAverage tps1 = new RollingAverage(60);
    public static final RollingAverage tps5 = new RollingAverage(60 * 5);
    public static final RollingAverage tps15 = new RollingAverage(60 * 15);

    public static CommandDispatcher<ServerCommandSource> commandDispatcher;
    public static OkyanusCommandMap commandMap;

    private SomeGlobals() {
    }

}
