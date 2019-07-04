package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.event.ConnectEventImpl;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin$ConnectEvent {

    private String joinMessage;

    @Inject(at = @At("HEAD"), method = "onPlayerConnect", cancellable = true)
    private void oky$onPlayerConnect(ClientConnection connection, ServerPlayerEntity playerEntity, CallbackInfo ci) {
        joinMessage = null;
        ConnectEventImpl e = Okyanus.getServer().getEventRegistry().trigger(new ConnectEventImpl(connection, playerEntity, "§e" + playerEntity.getDisplayName().asFormattedString() + " joined the game"));

        if (e.isCancelled()) {
            connection.disconnect(new LiteralText(e.getCancelReason()));
            ci.cancel();
        }

        joinMessage = e.getMessage();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/text/Text;)V"), method = "onPlayerConnect")
    private void oky$onPlayerConnect$sendToAll(PlayerManager playerManager, Text text_1) {
        if (joinMessage == null)
            return;

        text_1 = new LiteralText(joinMessage);
        playerManager.sendToAll(text_1);
    }

}
