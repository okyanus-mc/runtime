package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.cmdnew.OkyanusCommandMap;
import club.issizler.okyanus.api.event.ReadyEventImpl;
import club.issizler.okyanus.runtime.command.CommandRegistrar;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin {

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(at = @At(value = "INVOKE", target = "net.minecraft.util.SystemUtil.getMeasuringTimeNano()J", ordinal = 1), method = "setupServer")
    private void oky$setupServer$ready(CallbackInfoReturnable<Boolean> cir) {
        new OkyanusCommandMap(LOGGER).registerAll();
        CommandRegistrar.register();
        LOGGER.info("Okyanus: Starting all plugins");
        Okyanus.getServer().getEventRegistry().trigger(new ReadyEventImpl());
    }

}
