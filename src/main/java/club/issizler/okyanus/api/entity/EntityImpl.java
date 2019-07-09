package club.issizler.okyanus.api.entity;

import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.WorldImpl;
import net.minecraft.text.LiteralText;

import java.util.UUID;

public abstract class EntityImpl implements Entity {

    private final net.minecraft.entity.Entity entity;

    public EntityImpl(net.minecraft.entity.Entity entity) {
        this.entity = entity;
    }

    @Override
    public String getName() {
        return entity.getName().asFormattedString();
    }

    @Override
    public String getCustomName() {
        if (entity.getCustomName() == null)
            return getName();

        return entity.getCustomName().asFormattedString();
    }

    @Override
    public void setCustomName(String name) {
        entity.setCustomName(new LiteralText(name));
    }

    @Override
    public UUID getUUID() {
        return entity.getUuid();
    }

    @Override
    public Vec3d getPos() {
        net.minecraft.util.math.Vec3d pos = entity.getPos();

        return new Vec3d(pos.x, pos.y, pos.z);
    }

    @Override
    public WorldImpl getWorld() {
        return new WorldImpl(entity.world);
    }

    @Override
    public void teleport(Vec3d pos) {
        entity.teleport(pos.x, pos.y, pos.z);
    }

}
