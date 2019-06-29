// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0341-Optimize-redstone-algorithm.patch

package club.issizler.okyanus.runtime.mixin.optimizations.block;

import club.issizler.okyanus.runtime.Runtime;
import club.issizler.okyanus.runtime.utils.FastRedstoneWire;
import club.issizler.okyanus.runtime.utils.RedstoneWireFastable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin implements RedstoneWireFastable {

    @Shadow
    @Final
    public static IntProperty POWER;

    @Shadow
    private boolean wiresGivePower;

    @Shadow
    @Final
    private Set<BlockPos> affectedNeighbors;

    private FastRedstoneWire turbo = new FastRedstoneWire(this);

    @Shadow
    protected abstract int increasePower(int int_1, BlockState blockState_1);

    @Shadow
    protected abstract BlockState update(World world_1, BlockPos blockPos_1, BlockState blockState_1);

    public boolean canProvidePower() {
        return this.wiresGivePower;
    }

    public void setCanProvidePower(boolean value) {
        this.wiresGivePower = value;
    }

    private Set<BlockPos> getBlocksNeedingUpdate() {
        return this.affectedNeighbors;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RedstoneWireBlock;update(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;"), method = "neighborUpdate")
    private BlockState oky$neighborUpdate(RedstoneWireBlock redstoneWireBlock, World world_1, BlockPos blockPos_1, BlockState blockState_1, BlockState source_blockState_1, World source_world_1, BlockPos source_blockPos_1, Block source_block_1, BlockPos source_blockPos_2, boolean source_boolean_1) {
        if (Runtime.USE_FAST_REDSTONE)
            return turbo.updateSurroundingRedstone(world_1, blockPos_1, blockState_1, source_blockPos_2);

        return update(world_1, blockPos_1, blockState_1);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RedstoneWireBlock;update(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;"), method = "onBlockAdded")
    private BlockState oky$onBlockAdded(RedstoneWireBlock redstoneWireBlock, World world_1, BlockPos blockPos_1, BlockState blockState_1) {
        if (Runtime.USE_FAST_REDSTONE)
            return turbo.updateSurroundingRedstone(world_1, blockPos_1, blockState_1, null);

        return update(world_1, blockPos_1, blockState_1);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RedstoneWireBlock;update(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;"), method = "onBlockRemoved")
    private BlockState oky$onBlockRemoved(RedstoneWireBlock redstoneWireBlock, World world_1, BlockPos blockPos_1, BlockState blockState_1) {
        if (Runtime.USE_FAST_REDSTONE)
            return turbo.updateSurroundingRedstone(world_1, blockPos_1, blockState_1, null);

        return update(world_1, blockPos_1, blockState_1);
    }

    public BlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, BlockState state) {
        BlockState iblockstate = state;
        int i = state.get(POWER);
        int j = 0;
        j = this.increasePower(j, worldIn.getBlockState(pos2));
        this.setCanProvidePower(false);
        int k = worldIn.getReceivedRedstonePower(pos1);
        this.setCanProvidePower(true);

        if (!Runtime.USE_FAST_REDSTONE) {
            // This code is totally redundant to if statements just below the loop.
            if (k > 0 && k > j - 1) {
                j = k;
            }
        }

        int l = 0;

        // The variable 'k' holds the maximum redstone power value of any adjacent blocks.
        // If 'k' has the highest level of all neighbors, then the power level of this
        // redstone wire will be set to 'k'.  If 'k' is already 15, then nothing inside the
        // following loop can affect the power level of the wire.  Therefore, the loop is
        // skipped if k is already 15.
        if (!Runtime.USE_FAST_REDSTONE || k < 15) {
            for (Direction enumfacing : Direction.Type.HORIZONTAL) {
                BlockPos blockpos = pos1.offset(enumfacing);
                boolean flag = blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ();

                if (flag) {
                    l = this.increasePower(l, worldIn.getBlockState(blockpos));
                }

                if (worldIn.getBlockState(blockpos).isOpaque() && !worldIn.getBlockState(pos1.up()).isOpaque()) {
                    if (flag && pos1.getY() >= pos2.getY()) {
                        l = this.increasePower(l, worldIn.getBlockState(blockpos.up()));
                    }
                } else if (!worldIn.getBlockState(blockpos).isOpaque() && flag && pos1.getY() <= pos2.getY()) {
                    l = this.increasePower(l, worldIn.getBlockState(blockpos.down()));
                }
            }
        }

        if (!Runtime.USE_FAST_REDSTONE) {
            // The old code would decrement the wire value only by 1 at a time.
            if (l > j) {
                j = l - 1;
            } else if (j > 0) {
                --j;
            } else {
                j = 0;
            }

            if (k > j - 1) {
                j = k;
            }
        } else {
            // The new code sets this RedstoneWire block's power level to the highest neighbor
            // minus 1.  This usually results in wire power levels dropping by 2 at a time.
            // This optimization alone has no impact on update order, only the number of updates.
            j = l - 1;

            // If 'l' turns out to be zero, then j will be set to -1, but then since 'k' will
            // always be in the range of 0 to 15, the following if will correct that.
            if (k > j) j = k;
        }

        if (i != j) {
            state = state.with(POWER, j);

            if (worldIn.getBlockState(pos1) == iblockstate) {
                worldIn.setBlockState(pos1, state, 2);
            }

            if (!Runtime.USE_FAST_REDSTONE) {
                // The new search algorithm keeps track of blocks needing updates in its own data structures,
                // so only add anything to blocksNeedingUpdate if we're using the vanilla update algorithm.
                this.getBlocksNeedingUpdate().add(pos1);

                for (Direction enumfacing1 : Direction.values()) {
                    this.getBlocksNeedingUpdate().add(pos1.offset(enumfacing1));
                }
            }
        }

        return state;
    }


}
