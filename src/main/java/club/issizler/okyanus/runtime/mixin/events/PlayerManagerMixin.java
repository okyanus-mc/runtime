package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Player;
import club.issizler.okyanus.api.event.ChatEvent;
import club.issizler.okyanus.api.event.Event;
import club.issizler.okyanus.api.event.EventManager;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(at = @At("HEAD"), method = "broadcastChatMessage", cancellable = true)
    private void oky$broadcastChatMessage(Text message, boolean isSystem, CallbackInfo ci) {
        if (isSystem)
            return;

        TranslatableText text = (TranslatableText) message;
        String playerName = ((Text) text.getArgs()[0]).asFormattedString();
        String textMessage = ((String) text.getArgs()[1]);

        ChatEvent e = (ChatEvent) EventManager.INSTANCE.trigger(new ChatEvent(new Player(playerName), textMessage));

        if (e.isCancelled())
            ci.cancel();

        message = new LiteralText(e.getMessage());
    }

}
