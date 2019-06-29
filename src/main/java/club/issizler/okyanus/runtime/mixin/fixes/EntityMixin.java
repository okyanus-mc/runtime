// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0272-MC-111480-Start-Entity-ID-s-at-1.patch
package club.issizler.okyanus.runtime.mixin.fixes;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Final
    @Shadow
    private static final AtomicInteger MAX_ENTITY_ID = new AtomicInteger(1);

}
