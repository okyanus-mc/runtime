package club.issizler.okyanus.api.world;

public class BlockImpl implements Block {

    private final net.minecraft.block.Block block;

    BlockImpl(net.minecraft.block.Block block) {
        this.block = block;
    }

    net.minecraft.block.Block __internal_getBlock() {
        return block;
    }
}
