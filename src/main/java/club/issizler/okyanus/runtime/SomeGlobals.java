package club.issizler.okyanus.runtime;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class SomeGlobals {

    public static final double[] recentTps = new double[3];
    public static long tickSection;

    public static MinecraftDedicatedServer dedicatedServer;
    public static CommandDispatcher<ServerCommandSource> commandDispatcher;
}
