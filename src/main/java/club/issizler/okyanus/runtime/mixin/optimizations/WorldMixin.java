// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0379-Optimize-Captured-TileEntity-Lookup.patch
// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0032-Optimize-explosions.patch
// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0082-Do-not-load-chunks-for-light-checks.patch
// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0090-Remove-unused-World-Tile-Entity-List.patch

package club.issizler.okyanus.runtime.mixin.optimizations;

import club.issizler.okyanus.runtime.utils.ExplosionCacheKey;
import club.issizler.okyanus.runtime.utils.WorldDensityCacheable;
import com.google.common.collect.Maps;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(World.class)
public abstract class WorldMixin implements WorldDensityCacheable {

    @Shadow
    public static boolean isHeightInvalid(BlockPos int_1) {
        return false;
    }

    @Shadow
    @Final
    public boolean isClient;

    @Shadow
    @Final
    private Thread thread;

    @Shadow
    protected boolean iteratingTickingBlockEntities;

    @Shadow
    @Nullable
    protected abstract BlockEntity getPendingBlockEntity(BlockPos blockPos_1);

    @Shadow
    public abstract WorldChunk getWorldChunk(BlockPos blockPos_1);

    @Shadow
    public abstract boolean isHeightValidAndBlockLoaded(BlockPos blockPos_1);

    @Shadow @Final public List<BlockEntity> tickingBlockEntities;
    private Map<BlockPos, BlockEntity> capturedTileEntities = Maps.newHashMap();
    private Map<ExplosionCacheKey, Float> explosionDensityCache = new HashMap<>();

    /**
     * @author Aikar @ PaperSpigot & Okyanus
     * @reason Optimization
     */
    @Nullable
    @Overwrite
    public BlockEntity getBlockEntity(BlockPos blockPos_1) {
        if (isHeightInvalid(blockPos_1)) {
            return null;
        } else if (!this.isClient && Thread.currentThread() != this.thread) {
            return null;
        } else {
            BlockEntity blockEntity_1 = null;

            // Okyanus
            if (!capturedTileEntities.isEmpty() && (blockEntity_1 = capturedTileEntities.get(blockPos_1)) != null)
                return blockEntity_1;
            // Okyanus

            if (this.iteratingTickingBlockEntities) {
                blockEntity_1 = this.getPendingBlockEntity(blockPos_1);
            }

            if (blockEntity_1 == null) {
                blockEntity_1 = this.getWorldChunk(blockPos_1).getBlockEntity(blockPos_1, WorldChunk.CreationType.IMMEDIATE);
            }

            if (blockEntity_1 == null) {
                blockEntity_1 = this.getPendingBlockEntity(blockPos_1);
            }

            return blockEntity_1;
        }
    }

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

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;setPos(Lnet/minecraft/util/math/BlockPos;)V"), method = "setBlockEntity")
    private void oky$setBlockEntity(BlockPos blockPos_1, BlockEntity blockEntity_1, CallbackInfo ci) {
        capturedTileEntities.put(blockPos_1, blockEntity_1);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), method = "addBlockEntity")
    private boolean oky$addBlockEntity(List list, Object o) {
        return !tickingBlockEntities.contains(o);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 1), method = "tickBlockEntities")
    private boolean oky$tickBlockEntities$removeAll(List list, Collection<?> c) {
        return false; // Doesn't really matter
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"), method = "tickBlockEntities")
    private boolean oky$tickBlockEntities$remove(List list, Object o) {
        return false; // Doesn't really matter
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"), method = "tickBlockEntities")
    private boolean oky$tickBlockEntities$contains(List list, Object o) {
        return true;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"), method = "removeBlockEntity")
    private boolean oky$removeBlockEntity(List list, Object o) {
        return false; // Doesn't really matter
    }

    public Map<ExplosionCacheKey, Float> getExplosionDensityCache() {
        return explosionDensityCache;
    }

}
