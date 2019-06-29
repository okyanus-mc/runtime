package club.issizler.okyanus.api.world;

public class Block {

    private final net.minecraft.block.Block block;

    Block(net.minecraft.block.Block block) {
        this.block = block;
    }

    net.minecraft.block.Block __internal_getBlock() {
        return block;
    }
}
