package club.issizler.okyanus.runtime.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface RedstoneWireFastable {

    BlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, BlockState state);

    boolean canProvidePower();

    void setCanProvidePower(boolean value);

}
