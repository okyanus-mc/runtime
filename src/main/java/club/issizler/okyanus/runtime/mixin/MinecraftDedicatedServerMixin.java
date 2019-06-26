package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.runtime.SomeGlobals;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.SystemUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

import static club.issizler.okyanus.runtime.SomeGlobals.recentTps;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin {

    /**
     * @author Okyanus
     * @reason Hide the GUI by default
     */
    @Overwrite
    public void createGui() {
        /* intentionally empty */
    }

    @Inject(at = @At("TAIL"), method = "setupServer")
    private void oky$setupServer$tail(CallbackInfoReturnable<Boolean> cir) {
        Arrays.fill(recentTps, 20);
        SomeGlobals.tickSection = SystemUtil.getMeasuringTimeMs();
    }

}
