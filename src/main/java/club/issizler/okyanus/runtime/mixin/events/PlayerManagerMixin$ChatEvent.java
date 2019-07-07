package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.mck.MckPlayer;
import club.issizler.okyanus.api.event.ChatEventImpl;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin$ChatEvent {

    private Text newMessage;
    private boolean isSystem;

    @Inject(at = @At("HEAD"), method = "broadcastChatMessage", cancellable = true)
    private void oky$broadcastChatMessage(Text message, boolean isSystem, CallbackInfo ci) {
        this.isSystem = isSystem;
        if (isSystem)
            return;

        TranslatableText text = (TranslatableText) message;
        String playerName = ((Text) text.getArgs()[0]).asFormattedString();
        String textMessage = ((String) text.getArgs()[1]);

        Server s = Okyanus.getServer();

        Player player = s.getPlayerByName(playerName);
        if (player instanceof MckPlayer)
            return;

        ChatEventImpl e = s.getEventRegistry().trigger(new ChatEventImpl(player, textMessage));

        if (e.isCancelled())
            ci.cancel();

        newMessage = new LiteralText(e.getFormattedMessage());
    }

    @ModifyArg(at = @At(
            value = "INVOKE",
            target = "net.minecraft.server.MinecraftServer.sendMessage(Lnet/minecraft/text/Text;)V"
    ), method = "broadcastChatMessage", index = 0)
    private Text oky$broadcastChatMessage$setMessageOnServerLog(Text message) {
        if (isSystem)
            return message;

        if (newMessage == null)
            return message; // This shouldn't happen but ok

        return newMessage;
    }

    @ModifyArg(at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/packet/ChatMessageS2CPacket;<init>(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;)V"
    ), method = "broadcastChatMessage", index = 0)
    private Text oky$broadcastChatMessage$setMessageSentToPlayers(Text message) {
        if (isSystem)
            return message;

        if (newMessage == null)
            return message; // This shouldn't happen but ok

        return newMessage;
    }

}
