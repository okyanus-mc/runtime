package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.event.MoveEventImpl;
import net.minecraft.client.network.packet.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$MoveEvent {

    @Shadow
    public ServerPlayerEntity player;

    private int moveCancelSendTimer = 0;

    @Inject(at = @At("HEAD"), method = "onPlayerMove", cancellable = true)
    private void oky$onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        Server s = Okyanus.getServer();
        if (!s.isMainThread())
            return;

        MoveEventImpl e = s.getEventRegistry().trigger(new MoveEventImpl(packet, player));

        if (e.isCancelled()) {
            moveCancelSendTimer++;

            double x = this.player.x;
            double y = this.player.y;
            double z = this.player.z;
            float yaw = this.player.yaw;
            float pitch = this.player.pitch;

            if (moveCancelSendTimer > 5) { // If we don't do this, clients might potentially get kicked out due potential packet rate limits
                this.player.networkHandler.sendPacket(new PlayerPositionLookS2CPacket(x, y, z, yaw, pitch, Collections.emptySet(), 0));
                moveCancelSendTimer = 0;
            }

            ci.cancel();
        }
    }

}
