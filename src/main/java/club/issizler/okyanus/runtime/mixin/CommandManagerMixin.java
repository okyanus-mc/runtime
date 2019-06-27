package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.runtime.Runtime;
import club.issizler.okyanus.runtime.SomeGlobals;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {

    @Final
    @Shadow
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(boolean isDedicated, CallbackInfo ci) {
        SomeGlobals.commandDispatcher = dispatcher;
        LOGGER.debug("Okyanus: Set commandDispatcher to " + dispatcher);
    }

}
