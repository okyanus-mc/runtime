package club.issizler.okyanus.runtime;

import club.issizler.okyanus.runtime.utils.RollingAverage;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import java.util.Random;

public class SomeGlobals {

    public static final RollingAverage tps1 = new RollingAverage(60);
    public static final RollingAverage tps5 = new RollingAverage(60 * 5);
    public static final RollingAverage tps15 = new RollingAverage(60 * 15);

    public static final Random SHARED_RANDOM = new Random() {
        private boolean locked = false;

        @Override
        public synchronized void setSeed(long seed) {
            if (locked)
                return;

            super.setSeed(seed);
            locked = true;
        }

    };

    public static MinecraftDedicatedServer dedicatedServer;
    public static CommandDispatcher<ServerCommandSource> commandDispatcher;
}
