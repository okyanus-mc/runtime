// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0173-Fix-MC-117075-TE-Unload-Lag-Spike.patch
package club.issizler.okyanus.runtime.mixin.fixes;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(value = World.class)
public abstract class WorldMixin {

    @Shadow
    @Final
    public List<BlockEntity> tickingBlockEntities;
    @Shadow
    @Final
    protected List<BlockEntity> unloadedBlockEntities;

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 0), method = "tickBlockEntities")
    private boolean oky$tickBlockEntities(List list, Collection<?> c) {
        Set<BlockEntity> toRemove = Collections.newSetFromMap(new IdentityHashMap<>());
        toRemove.addAll(unloadedBlockEntities);
        this.tickingBlockEntities.removeAll(toRemove);
        return false;
    }

}
