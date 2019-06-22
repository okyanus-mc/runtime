package club.issizler.okyanus.mixin;

import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftDedicatedServer.class)
public abstract class NoGuiMixin {

    /**
     * @author Okyanus
     * @reason Hide the GUI by default
     */
    @Overwrite
    public void createGui() {
        /* intentionally empty */
    }

}
