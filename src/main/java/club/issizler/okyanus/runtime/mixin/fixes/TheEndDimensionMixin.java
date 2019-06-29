// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0329-Fix-MC-93764.patch
package club.issizler.okyanus.runtime.mixin.fixes;

import net.minecraft.world.dimension.TheEndDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TheEndDimension.class)
public abstract class TheEndDimensionMixin {

    /**
     * @author BillyGalbreath @ Paper & Okyanus
     * @reason Fix MC-93764
     */
    @Overwrite
    public float getSkyAngle(long long_1, float float_1) {
        return 0.5F;
    }

}
