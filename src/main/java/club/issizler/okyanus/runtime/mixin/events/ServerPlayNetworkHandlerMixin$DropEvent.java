package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.event.DropEventImpl;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$DropEvent {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onPlayerAction", cancellable = true)
    private void oky$onPlayerAction(PlayerActionC2SPacket playerActionC2SPacket_1, CallbackInfo ci) {
        if (!(playerActionC2SPacket_1.getAction() == PlayerActionC2SPacket.Action.DROP_ITEM || playerActionC2SPacket_1.getAction() == PlayerActionC2SPacket.Action.DROP_ALL_ITEMS))
            return;

        Server s = Okyanus.getServer();
        if (!s.isMainThread())
            return;

        DropEventImpl e = s.triggerEvent(new DropEventImpl(playerActionC2SPacket_1, player));

        if (e.isCancelled()) {
            ci.cancel(); // TODO: Alert client of this cancellation.
        }
    }

}
