// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0090-Remove-unused-World-Tile-Entity-List.patch

package club.issizler.okyanus.runtime.mixin.optimizations.blockentity;

import club.issizler.okyanus.runtime.utils.WorldDensityCacheable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin$UnusedBlockEntityList implements WorldDensityCacheable {

    @Shadow
    @Final
    public List<BlockEntity> tickingBlockEntities;

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), method = "addBlockEntity")
    private boolean oky$addBlockEntity(List list, Object o) {
        return !tickingBlockEntities.contains(o);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z", ordinal = 1), method = "tickBlockEntities")
    private boolean oky$tickBlockEntities$removeAll(List list, Collection<?> c) {
        return false; // Doesn't really matter
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"), method = "tickBlockEntities")
    private boolean oky$tickBlockEntities$remove(List list, Object o) {
        return false; // Doesn't really matter
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"), method = "tickBlockEntities")
    private boolean oky$tickBlockEntities$contains(List list, Object o) {
        return true;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"), method = "removeBlockEntity")
    private boolean oky$removeBlockEntity(List list, Object o) {
        return false; // Doesn't really matter
    }

}
