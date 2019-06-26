package club.issizler.okyanus.runtime.utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface RedstoneWireFastable {

    public BlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, BlockState state);

    public boolean canProvidePower();
    public void setCanProvidePower(boolean value);

}
