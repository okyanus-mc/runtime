// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0032-Optimize-explosions.patch

package club.issizler.okyanus.runtime.mixin.optimizations.explosion;

import club.issizler.okyanus.runtime.utils.ExplosionCacheKey;
import club.issizler.okyanus.runtime.utils.accessors.WorldDensityCacheable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.HashMap;
import java.util.Map;

@Mixin(World.class)
public abstract class WorldMixin implements WorldDensityCacheable {

    private Map<ExplosionCacheKey, Float> explosionDensityCache = new HashMap<>();

    public Map<ExplosionCacheKey, Float> getExplosionDensityCache() {
        return explosionDensityCache;
    }

}
