package club.issizler.okyanus.mixin;

import club.issizler.okyanus.command.PluginsCommand;
import club.issizler.okyanus.command.TPSCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CustomCommandsMixin {

    @Final
    @Shadow
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(boolean isOp, CallbackInfo ci) {
        if (isOp) {
            PluginsCommand.register(dispatcher);
            TPSCommand.register(dispatcher);
        }
    }

}
