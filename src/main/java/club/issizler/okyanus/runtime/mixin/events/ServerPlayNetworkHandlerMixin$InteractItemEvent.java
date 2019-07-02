package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.event.BreakEventImpl;
import club.issizler.okyanus.api.event.InteractItemEventImpl;
import net.minecraft.client.network.packet.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import net.minecraft.server.network.packet.PlayerInteractItemC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$InteractItemEvent {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "onPlayerInteractItem", cancellable = true)
    private void oky$onPlayerInteractItem(PlayerInteractItemC2SPacket playerInteractItemC2SPacket_1, CallbackInfo ci) {
        Server s = Okyanus.getServer();
        if (!s.isMainThread())
            return;

        InteractItemEventImpl e = s.triggerEvent(new InteractItemEventImpl(playerInteractItemC2SPacket_1, player));
        if (e.isCancelled()) {
            ci.cancel();
        }
    }

}
