package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.api.event.StopEvent;
import club.issizler.okyanus.runtime.SomeGlobals;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SystemUtil;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    private static final int TPS = 20;
    private static final int SAMPLE_INTERVAL = 100;

    private long curTime, tickCount = 1;

    @Final
    @Shadow
    private static Logger LOGGER;

    private static double calcTps(double avg, double exp, double tps) {
        return (avg * exp) + (tps * (1 - exp));
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void oky$tick(CallbackInfo ci) {
        curTime = SystemUtil.getMeasuringTimeMs();

        if (tickCount++ % SAMPLE_INTERVAL == 0) {
            double currentTps = 1E3 / (curTime - SomeGlobals.tickSection) * SAMPLE_INTERVAL;
            SomeGlobals.recentTps[0] = calcTps(SomeGlobals.recentTps[0], 0.92, currentTps); // 1/exp(5sec/1min)
            SomeGlobals.recentTps[1] = calcTps(SomeGlobals.recentTps[1], 0.9835, currentTps); // 1/exp(5sec/5min)
            SomeGlobals.recentTps[2] = calcTps(SomeGlobals.recentTps[2], 0.9945, currentTps); // 1/exp(5sec/15min)
            SomeGlobals.tickSection = curTime;
        }
    }

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void oky$shutdown(CallbackInfo ci) {
        LOGGER.info("Okyanus: Sending stop event...");
        EventManager.INSTANCE.trigger(new StopEvent());
    }

}
