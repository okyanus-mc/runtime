// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0032-Optimize-explosions.patch

package club.issizler.okyanus.runtime.mixin.optimizations.explosion;

import club.issizler.okyanus.runtime.Runtime;
import club.issizler.okyanus.runtime.utils.ExplosionCacheKey;
import club.issizler.okyanus.runtime.utils.ExplosionPublicable;
import club.issizler.okyanus.runtime.utils.WorldDensityCacheable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public abstract class ExplosionMixin implements ExplosionPublicable {

    @Shadow
    @Final
    private World world;

    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double z;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/Explosion;getExposure(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)F"), method = "collectBlocksAndDamageEntities")
    private float oky$collectBlocksAndDamageEntities$optimize(Vec3d vec3d_1, Entity entity_1) {
        if (!Runtime.USE_FAST_EXPLOSIONS)
            return Explosion.getExposure(vec3d_1, entity_1);

        return getBlockDensity(vec3d_1, entity_1);
    }

    private float getBlockDensity(Vec3d vec3d_1, Entity entity_1) {
        ExplosionCacheKey key = new ExplosionCacheKey(this, entity_1.getBoundingBox());

        Float blockDensity = ((WorldDensityCacheable) world).getExplosionDensityCache().get(key);
        if (blockDensity == null) {
            blockDensity = Explosion.getExposure(vec3d_1, entity_1);
            ((WorldDensityCacheable) world).getExplosionDensityCache().put(key, blockDensity);
        }

        return blockDensity;
    }


    public World getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
