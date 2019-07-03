package club.issizler.okyanus.api.world;

import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.block.BlockState;

public class BlockImpl implements Block {

    private final BlockState block;
    private final Vec3d pos;

    BlockImpl(BlockState block, Vec3d pos) {
        this.block = block;
        this.pos = pos;
    }

    @Override
    public Vec3d getLocation() {
        return pos;
    }

}
