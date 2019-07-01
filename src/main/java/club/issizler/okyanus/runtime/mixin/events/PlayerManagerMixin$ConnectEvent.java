package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.event.ConnectEventImpl;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin$ConnectEvent {

    @Inject(at = @At("TAIL"), method = "onPlayerConnect", cancellable = true)
    private void oky$onPlayerConnect(ClientConnection connection, ServerPlayerEntity playerEntity, CallbackInfo ci) {
        ConnectEventImpl e = Okyanus.getServer().triggerEvent(new ConnectEventImpl(connection, playerEntity));

        if (e.isCancelled()) {
            connection.disconnect(new LiteralText(e.getCancelReason()));
            ci.cancel();
        }
    }

}
