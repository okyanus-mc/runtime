// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0325-MC-50319-Check-other-worlds-for-shooter-of-projectil.patch
package club.issizler.okyanus.runtime.mixin.fixes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {

    @Shadow
    public UUID ownerUuid;

    public ProjectileEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Fix MC-50319
     */
    @Overwrite
    @Nullable
    public Entity getOwner() {
        Entity e = this.ownerUuid != null && this.world instanceof ServerWorld ? ((ServerWorld) this.world).getEntity(this.ownerUuid) : null;

        if (e == null) {
            for (ServerWorld world : Objects.requireNonNull(world.getServer()).getWorlds()) {
                e = world.getEntity(this.ownerUuid);

                if (e != null)
                    break;
            }
        }

        return e;
    }

}
