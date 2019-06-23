package club.issizler.okyanus.mixin.tps;

import club.issizler.okyanus.ServerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SystemUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    private static final int TPS = 20;
    private static final int TICK_TIME = 1000000000 / TPS;
    private static final int SAMPLE_INTERVAL = 100;

    private long curTime, tickCount = 1;

    private static double calcTps(double avg, double exp, double tps) {
        return (avg * exp) + (tps * (1 - exp));
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void oky$tick(CallbackInfo ci) {
        curTime = SystemUtil.getMeasuringTimeMs();

        if (tickCount++ % SAMPLE_INTERVAL == 0) {
            double currentTps = 1E3 / (curTime - ServerStatus.tickSection) * SAMPLE_INTERVAL;
            ServerStatus.recentTps[0] = calcTps(ServerStatus.recentTps[0], 0.92, currentTps); // 1/exp(5sec/1min)
            ServerStatus.recentTps[1] = calcTps(ServerStatus.recentTps[1], 0.9835, currentTps); // 1/exp(5sec/5min)
            ServerStatus.recentTps[2] = calcTps(ServerStatus.recentTps[2], 0.9945, currentTps); // 1/exp(5sec/15min)
            ServerStatus.tickSection = curTime;
        }
    }

}
