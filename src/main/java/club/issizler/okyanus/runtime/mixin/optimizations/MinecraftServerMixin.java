// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0032-Optimize-explosions.patch

package club.issizler.okyanus.runtime.mixin.optimizations;

import club.issizler.okyanus.runtime.utils.WorldDensityCacheable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow
    @Final
    private Map<DimensionType, ServerWorld> worlds;

    @Inject(at = @At("TAIL"), method = "tickWorlds")
    private void oky$tickWorlds(BooleanSupplier booleanSupplier_1, CallbackInfo ci) {
        worlds.forEach((dimensionType, world) -> {
            ((WorldDensityCacheable) world).getExplosionDensityCache().clear();
        });
    }

}
