// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0082-Do-not-load-chunks-for-light-checks.patch

package club.issizler.okyanus.runtime.mixin.optimizations.unneccesarychunks;

import club.issizler.okyanus.runtime.utils.WorldDensityCacheable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldMixin implements WorldDensityCacheable {

    @Shadow
    public abstract boolean isHeightValidAndBlockLoaded(BlockPos blockPos_1);

    @Shadow
    public abstract WorldChunk getWorldChunk(BlockPos blockPos_1);

    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Optimization
     */
    @Overwrite
    public int getLightLevel(BlockPos blockPos_1, int int_1) {
        if (blockPos_1.getX() >= -30000000 && blockPos_1.getZ() >= -30000000 && blockPos_1.getX() < 30000000 && blockPos_1.getZ() < 30000000) {
            if (blockPos_1.getY() < 0) {
                return 0;
            } else {
                if (blockPos_1.getY() >= 256) {
                    blockPos_1 = new BlockPos(blockPos_1.getX(), 255, blockPos_1.getZ());
                }

                // Okyanus
                if (!this.isHeightValidAndBlockLoaded(blockPos_1))
                    return 0;
                // Okyanus

                return this.getWorldChunk(blockPos_1).getLightLevel(blockPos_1, int_1);
            }
        } else {
            return 15;
        }
    }

}
