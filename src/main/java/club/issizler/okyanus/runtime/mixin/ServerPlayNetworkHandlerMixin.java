package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.api.event.MoveEvent;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    private ServerPlayerEntity player;

    @Inject(at=@At("HEAD"), method = "onPlayerMove", cancellable = true)
    private void oky$onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        MoveEvent e = (MoveEvent) EventManager.INSTANCE.trigger(new MoveEvent(packet, player));

        if (e.isCancelled()) {
            ci.cancel(); // TODO: Alert client of this cancellation.
        }
    }

}