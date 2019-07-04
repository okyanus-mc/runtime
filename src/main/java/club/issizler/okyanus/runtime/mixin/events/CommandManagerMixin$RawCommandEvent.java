package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.Server;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.event.RawCommandEventImpl;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin$RawCommandEvent {

    @Inject(at = @At("HEAD"), method = "execute", cancellable = true)
    private void oky$execute(ServerCommandSource serverCommandSource_1, String string_1, CallbackInfoReturnable<Integer> cir) {
        if (string_1.startsWith("/"))
            string_1 = string_1.substring(1);

        RawCommandEventImpl event = null;
        Server s = Okyanus.getServer();

        if (serverCommandSource_1.getEntity() == null) {
            event = s.getEventRegistry().trigger(new RawCommandEventImpl(string_1));
        } else {
            try {
                event = s.getEventRegistry().trigger(new RawCommandEventImpl(new PlayerImpl(serverCommandSource_1.getPlayer()), string_1));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        string_1 = event.getCommand();
        if (event.isCancelled())
            cir.cancel();
    }

}
