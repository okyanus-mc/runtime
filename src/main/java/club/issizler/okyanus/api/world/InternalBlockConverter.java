package club.issizler.okyanus.api.world;

import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;

import java.util.Optional;

class InternalBlockConverter {

    static Optional<Block> convertBlock(Blocks block) {
        try {
            return Optional.ofNullable((Block) net.minecraft.block.Blocks.class.getDeclaredField(block.name()).get(null));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LogManager.getLogger().error("Error getting block for " + block.name() + "!");
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
