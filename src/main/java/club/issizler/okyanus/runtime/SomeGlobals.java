package club.issizler.okyanus.runtime;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class SomeGlobals {

    public static final double[] recentTps = new double[3];
    public static long tickSection;

    public static MinecraftDedicatedServer dedicatedServer;
}
