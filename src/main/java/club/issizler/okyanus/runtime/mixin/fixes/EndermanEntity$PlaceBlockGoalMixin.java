// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0292-Fix-MC-124320.patch
package club.issizler.okyanus.runtime.mixin.fixes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(targets = "net.minecraft.entity.mob.EndermanEntity.PlaceBlockGoal")
public abstract class EndermanEntity$PlaceBlockGoalMixin {

    @Shadow
    @Final
    private EndermanEntity enderman;

    @Shadow
    protected abstract boolean method_7033(ViewableWorld viewableWorld_1, BlockPos blockPos_1, BlockState blockState_1, BlockState blockState_2, BlockState blockState_3, BlockPos blockPos_2);

    /**
     * @author BillyGalbreath @ Paper & Okyanus
     * @reason Fix MC-124320
     */
    @Overwrite
    public void tick() {
        Random random_1 = this.enderman.getRand();
        IWorld iWorld_1 = this.enderman.world;
        int int_1 = MathHelper.floor(this.enderman.x - 1.0D + random_1.nextDouble() * 2.0D);
        int int_2 = MathHelper.floor(this.enderman.y + random_1.nextDouble() * 2.0D);
        int int_3 = MathHelper.floor(this.enderman.z - 1.0D + random_1.nextDouble() * 2.0D);
        BlockPos blockPos_1 = new BlockPos(int_1, int_2, int_3);
        BlockState blockState_1 = iWorld_1.getBlockState(blockPos_1);
        BlockPos blockPos_2 = blockPos_1.down();
        BlockState blockState_2 = iWorld_1.getBlockState(blockPos_2);
        BlockState blockState_3 = Block.getRenderingState(enderman.getCarriedBlock(), enderman.world, blockPos_2); // Okyanus
        if (blockState_3 != null && this.method_7033(iWorld_1, blockPos_1, blockState_3, blockState_1, blockState_2, blockPos_2)) {
            iWorld_1.setBlockState(blockPos_1, blockState_3, 3);
            this.enderman.setCarriedBlock(null);
        }

    }

}
