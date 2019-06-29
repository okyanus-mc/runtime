package club.issizler.okyanus.api.world;

import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;

public class InternalBlockConverter {

    public static Block convertBlock(Blocks block) {
        try {
            return (Block) net.minecraft.block.Blocks.class.getDeclaredField(block.name()).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LogManager.getLogger().error("Error getting block for " + block.name() + "! IT WILL BE NULL");
            e.printStackTrace();
        }

        return null;
    }

}
