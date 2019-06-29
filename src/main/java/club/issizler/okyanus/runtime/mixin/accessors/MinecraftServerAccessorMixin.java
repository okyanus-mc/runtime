package club.issizler.okyanus.runtime.mixin.accessors;

import club.issizler.okyanus.runtime.utils.accessors.MinecraftServerLoggable;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerAccessorMixin implements MinecraftServerLoggable {

    @Final
    @Shadow
    private static Logger LOGGER;

    public Logger getLogger() {
        return LOGGER;
    }

}
