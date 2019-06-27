package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.api.event.StopEvent;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Final
    @Shadow
    private static Logger LOGGER;

    @Inject(at = @At("HEAD"), method = "shutdown")
    private void oky$shutdown(CallbackInfo ci) {
        LOGGER.info("Okyanus: Stopping all plugins");
        EventManager.INSTANCE.trigger(new StopEvent());
    }

}
