package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.event.BreakEventImpl;
import net.minecraft.client.network.packet.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$BreakEvent {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onPlayerAction", cancellable = true)
    private void oky$onPlayerAction(PlayerActionC2SPacket playerActionC2SPacket_1, CallbackInfo ci) {
        if (!player.isCreative())
            if (playerActionC2SPacket_1.getAction() != PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK)
                return;

        Server s = Okyanus.getServer();
        if (!s.isMainThread())
            return;

        BreakEventImpl e = s.triggerEvent(new BreakEventImpl(playerActionC2SPacket_1, player));
        if (e.isCancelled()) {
            this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(player.world, playerActionC2SPacket_1.getPos()));
            ci.cancel();
        }
    }

}
