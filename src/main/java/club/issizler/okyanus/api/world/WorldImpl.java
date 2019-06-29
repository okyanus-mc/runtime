package club.issizler.okyanus.api.world;

import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.util.math.BlockPos;

public class WorldImpl implements World {

    private net.minecraft.world.World world;

    public WorldImpl(net.minecraft.world.World world) {
        this.world = world;
    }

    public void setBlockAt(Vec3d pos, Block block) {
        world.setBlockState(new BlockPos(pos.x, pos.y, pos.z), ((BlockImpl) block).__internal_getBlock().getDefaultState());
    }

}
