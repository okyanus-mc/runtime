package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.event.EventManagerImpl;
import club.issizler.okyanus.api.event.PlaceEventImpl;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractBlockC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$PlaceEvent {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onPlayerInteractBlock", cancellable = true)
    private void oky$onPlayerAction(PlayerInteractBlockC2SPacket playerInteractBlockC2SPacket_1, CallbackInfo ci) {
        PlaceEventImpl e = EventManagerImpl.INSTANCE.trigger(new PlaceEventImpl(playerInteractBlockC2SPacket_1, player));

        if (e.isCancelled()) {
            ci.cancel(); // TODO: Alert client of this cancellation.
        }
    }

}
