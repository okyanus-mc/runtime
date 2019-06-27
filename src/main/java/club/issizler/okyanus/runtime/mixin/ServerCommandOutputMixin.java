package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.runtime.Runtime;
import club.issizler.okyanus.runtime.SomeGlobals;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.ServerCommandOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerCommandOutput.class)
public abstract class ServerCommandOutputMixin {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(MinecraftServer minecraftServer, CallbackInfo ci) {
        SomeGlobals.dedicatedServer = (MinecraftDedicatedServer) minecraftServer;

        if (Runtime.DEBUG)
            SomeGlobals.dedicatedServer.log("Set dedicatedServer to " + minecraftServer);
    }

}
