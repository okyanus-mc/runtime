package club.issizler.okyanus.api.world;

import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockImpl implements Block {

    private final BlockState block;
    private final Vec3d pos;
    private final World world;

    public BlockImpl(World world, BlockState block, Vec3d pos) {
        this.world = world;
        this.block = block;
        this.pos = pos;
    }

    @NotNull
    @Override
    public Vec3d getLocation() {
        return pos;
    }

    @NotNull
    @Override
    public World getWorld() {
        return world;
    }

}
