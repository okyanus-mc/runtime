// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0070-Use-a-Shared-Random-for-Entities.patch

package club.issizler.okyanus.runtime.mixin.optimizations.sharedrandom;

import club.issizler.okyanus.runtime.SomeGlobals;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Mutable
    @Shadow
    @Final
    protected Random random;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(EntityType<?> entityType_1, World world_1, CallbackInfo ci) {
        random = SomeGlobals.SHARED_RANDOM;
    }

}
