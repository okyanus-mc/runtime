package club.issizler.okyanus.api.world;

import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class WorldImpl implements World {

    private net.minecraft.world.World world;

    public WorldImpl(net.minecraft.world.World world) {
        this.world = world;
    }

    public void setBlockAt(Vec3d pos, Blocks block) {
        Optional<net.minecraft.block.Block> b = InternalBlockConverter.convertBlock(block);

        // We might not want to silently ignore this error
        b.ifPresent(value -> world.setBlockState(new BlockPos(pos.x, pos.y, pos.z), value.getDefaultState()));
    }

    public Block getBlockAt(Vec3d pos) {
        return new BlockImpl(world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)));
    }

}
