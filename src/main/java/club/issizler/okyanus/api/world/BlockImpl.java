package club.issizler.okyanus.api.world;

import net.minecraft.block.BlockState;

public class BlockImpl implements Block {

    private final BlockState block;

    BlockImpl(BlockState block) {
        this.block = block;
    }

}
