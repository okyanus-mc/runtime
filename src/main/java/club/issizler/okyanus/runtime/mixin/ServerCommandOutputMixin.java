package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.ServerImpl;
import club.issizler.okyanus.api.cmdnew.CommandRegistryImpl;
import club.issizler.okyanus.api.event.EventRegistryImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.ServerCommandOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommandOutput.class)
public abstract class ServerCommandOutputMixin {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(MinecraftServer minecraftServer, CallbackInfo ci) {
        Okyanus.setServer(
            new ServerImpl(
                minecraftServer,
                new CommandRegistryImpl(),
                new club.issizler.okyanus.api.cmd.CommandRegistryImpl(),
                new EventRegistryImpl()
            )
        );
    }

}
