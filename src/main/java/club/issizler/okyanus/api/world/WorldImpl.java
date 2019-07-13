package club.issizler.okyanus.api.world;

import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WorldImpl implements World {

    private final net.minecraft.world.World world;

    public WorldImpl(@NotNull final net.minecraft.world.World world) {
        this.world = world;
    }

    public void setBlockAt(@NotNull Vec3d pos, @NotNull Blocks block) {
        Optional<net.minecraft.block.Block> b = InternalBlockConverter.convertBlock(block);

        // We might not want to silently ignore this error
        b.ifPresent(value -> world.setBlockState(new BlockPos(pos.x, pos.y, pos.z), value.getDefaultState()));
    }

    @NotNull
    public Block getBlockAt(@NotNull Vec3d pos) {
        return new BlockImpl(this, world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)), pos);
    }

    @NotNull
    @Override
    public String getName() {
        return world.getServer().getLevelName(); // Not sure if we should account for mutliple worlds or not.
    }

}
