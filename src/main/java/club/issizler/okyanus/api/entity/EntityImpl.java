package club.issizler.okyanus.api.entity;

import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.WorldImpl;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityImpl implements Entity {

    private final net.minecraft.entity.Entity entity;

    public EntityImpl(@NotNull final net.minecraft.entity.Entity entity) {
        this.entity = entity;
    }

    @NotNull
    @Override
    public String getName() {
        return entity.getName().asFormattedString();
    }

    @NotNull
    @Override
    public String getCustomName() {
        if (entity.getCustomName() == null)
            return getName();

        return entity.getCustomName().asFormattedString();
    }

    @Override
    public void setCustomName(@NotNull String name) {
        entity.setCustomName(new LiteralText(name));
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return entity.getUuid();
    }

    @NotNull
    @Override
    public Vec3d getPos() {
        net.minecraft.util.math.Vec3d pos = entity.getPos();

        return new Vec3d(pos.x, pos.y, pos.z);
    }

    @NotNull
    @Override
    public WorldImpl getWorld() {
        return new WorldImpl(entity.world);
    }

    @Override
    public void teleport(@NotNull Vec3d pos) {
        entity.teleport(pos.x, pos.y, pos.z);
    }

    @Override
    public void sendMessage(@NotNull final String... messages) {
    }

    @Override
    public int hashCode() {
        return entity.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return entity.equals(o);
    }

}
