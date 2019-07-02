package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.event.InteractEntityEventImpl;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$InteractEntityEvent {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onPlayerInteractEntity", cancellable = true)
    private void oky$onPlayerInteractEntity(PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket_1, CallbackInfo ci) {
        Server s = Okyanus.getServer();
        if (!s.isMainThread())
            return;

        InteractEntityEventImpl e = s.triggerEvent(new InteractEntityEventImpl(playerInteractEntityC2SPacket_1, player));
        if (e.isCancelled()) {
            ci.cancel();
        }
    }

}
