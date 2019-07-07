package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.event.PingEventImpl;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import net.minecraft.server.network.packet.QueryRequestC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerQueryNetworkHandler.class)
public abstract class ServerQueryNetworkHandlerMixin {

    @Inject(at = @At("HEAD"), method = "onRequest")
    private void oky$onRequest(QueryRequestC2SPacket queryRequestC2SPacket_1, CallbackInfo ci) {
        Okyanus.getServer().getEventRegistry().trigger(new PingEventImpl());
    }

}
