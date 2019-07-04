package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.event.DisconnectEvent;
import club.issizler.okyanus.api.event.DisconnectEventImpl;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandler$DisconnectEvent {

    @Shadow
    public ServerPlayerEntity player;

    private String leaveMessage;

    @Inject(at = @At("HEAD"), method = "onDisconnected")
    private void oky$remove(Text text_1, CallbackInfo ci) {
        leaveMessage = null;
        DisconnectEvent event = Okyanus.getServer().triggerEvent(new DisconnectEventImpl(player, player.getDisplayName().asFormattedString() + " left the game"));

        leaveMessage = event.getMessage();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/text/Text;)V"), method = "onDisconnected")
    private void oky$remove$sendToAll(PlayerManager playerManager, Text text_1) {
        text_1 = new LiteralText(leaveMessage);
        playerManager.sendToAll(text_1);
    }

}
