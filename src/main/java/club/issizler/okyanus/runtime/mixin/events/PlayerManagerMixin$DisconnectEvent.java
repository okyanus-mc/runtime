package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.event.DisconnectEventImpl;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin$DisconnectEvent {

    @Inject(at = @At("TAIL"), method = "remove")
    private void oky$remove(ServerPlayerEntity serverPlayerEntity_1, CallbackInfo ci) {
        Okyanus.getServer().triggerEvent(new DisconnectEventImpl(serverPlayerEntity_1));
    }

}
