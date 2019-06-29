// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0394-MC-114618-Fix-EntityAreaEffectCloud-from-going-negat.patch
// Fixes MC-114618

package club.issizler.okyanus.runtime.mixin.fixes;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin extends Entity {

    public AreaEffectCloudEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow
    public abstract float getRadius();

    @Inject(at = @At(value = "INVOKE", target = "net.minecraft.entity.AreaEffectCloudEntity.getRadius()F"), method = "tick", cancellable = true)
    private void oky$tick(CallbackInfo ci) {
        if (this.getRadius() < 0.0f) {
            this.kill();
            ci.cancel();
        }
    }

}
