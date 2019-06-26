package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.api.event.ReadyEvent;
import club.issizler.okyanus.runtime.command.CommandRegistrar;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin {

    @Inject(at = @At(value = "INVOKE", target = "net.minecraft.util.SystemUtil.getMeasuringTimeNano()J", ordinal = 1), method = "setupServer")
    private void oky$setupServer$ready(CallbackInfoReturnable<Boolean> cir) {
        CommandRegistrar.register();
        EventManager.INSTANCE.trigger(new ReadyEvent());
    }

}
