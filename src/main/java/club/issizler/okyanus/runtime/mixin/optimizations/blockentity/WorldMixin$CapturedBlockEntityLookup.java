// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0379-Optimize-Captured-TileEntity-Lookup.patch

package club.issizler.okyanus.runtime.mixin.optimizations.blockentity;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Map;

@Mixin(World.class)
public abstract class WorldMixin$CapturedBlockEntityLookup implements WorldDensityCacheable {

    @Shadow
    public static boolean isHeightInvalid(BlockPos blockPos_1) {
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

    private Map<BlockPos, BlockEntity> capturedTileEntities = Maps.newHashMap();

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

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;setPos(Lnet/minecraft/util/math/BlockPos;)V"), method = "setBlockEntity")
    private void oky$setBlockEntity(BlockPos blockPos_1, BlockEntity blockEntity_1, CallbackInfo ci) {
        capturedTileEntities.put(blockPos_1, blockEntity_1);
    }

}
