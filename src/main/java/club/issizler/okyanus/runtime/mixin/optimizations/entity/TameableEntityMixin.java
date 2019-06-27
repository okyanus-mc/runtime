// https://github.com/SpongePowered/SpongeCommon/blob/1.13/src/main/java/org/spongepowered/common/mixin/optimization/entity/MixinEntityTameable_Cached_Owner.java

package club.issizler.okyanus.runtime.mixin.optimizations.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin extends AnimalEntity {

    @Shadow
    @Final
    protected static TrackedData<Optional<UUID>> OWNER_UUID;

    @Nullable
    private Optional<UUID> cachedOwnerId;

    protected TameableEntityMixin(EntityType<? extends AnimalEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    /**
     * @author gabizou @ Sponge
     * @reason Uses the cached owner id to save constant lookups from the data watcher
     */
    @Nullable
    @Overwrite
    public UUID getOwnerUuid() {
        //noinspection OptionalAssignedToNull
        if (this.cachedOwnerId == null) {
            this.cachedOwnerId = this.dataTracker.get(OWNER_UUID);
        }

        return this.cachedOwnerId.orElse(null);
    }

    /**
     * @author gabizou @ Sponge
     * @reason stores the cached owner id
     */
    @Overwrite
    public void setOwnerUuid(@Nullable UUID ownerUuid) {
        this.cachedOwnerId = Optional.ofNullable(ownerUuid);
        this.dataTracker.set(OWNER_UUID, this.cachedOwnerId);
    }

}
