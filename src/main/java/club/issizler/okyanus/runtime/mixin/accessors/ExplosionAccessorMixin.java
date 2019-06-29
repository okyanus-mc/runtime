package club.issizler.okyanus.runtime.mixin.accessors;

import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Explosion.class)
public interface ExplosionAccessorMixin {

    @Accessor
    World getWorld();

    @Accessor
    double getX();

    @Accessor
    double getY();

    @Accessor
    double getZ();

}
