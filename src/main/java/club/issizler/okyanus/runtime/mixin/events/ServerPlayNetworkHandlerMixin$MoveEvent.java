package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.api.event.EventManagerImpl;
import club.issizler.okyanus.api.event.MoveEventImpl;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$MoveEvent {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onPlayerMove", cancellable = true)
    private void oky$onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        MoveEventImpl e = EventManager.getInstance().trigger(new MoveEventImpl(packet, player));

        if (e.isCancelled()) {
            ci.cancel(); // TODO: Alert client of this cancellation.
        }
    }

}
