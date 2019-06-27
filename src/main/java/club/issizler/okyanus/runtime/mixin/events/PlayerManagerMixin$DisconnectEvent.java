package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.event.DisconnectEvent;
import club.issizler.okyanus.api.event.EventManager;
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
        EventManager.INSTANCE.trigger(new DisconnectEvent(serverPlayerEntity_1));
    }

}
