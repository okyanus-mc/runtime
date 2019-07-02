package club.issizler.okyanus.api.entity;

import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.WorldImpl;
import net.minecraft.text.LiteralText;

import java.util.UUID;

public class EntityImpl implements Entity {

    private final net.minecraft.entity.Entity entity;

    public EntityImpl(net.minecraft.entity.Entity entity) {
        this.entity = entity;
    }

    public String getName() {
        return entity.getName().asFormattedString();
    }

    public String getCustomName() {
        if (entity.getCustomName() == null)
            return getName();

        return entity.getCustomName().asFormattedString();
    }

    public void setCustomName(String name) {
        entity.setCustomName(new LiteralText(name));
    }

    public UUID getUUID() {
        return entity.getUuid();
    }

    public Vec3d getPos() {
        net.minecraft.util.math.Vec3d pos = entity.getPos();

        return new Vec3d(pos.x, pos.y, pos.z);
    }

    public WorldImpl getWorld() {
        return new WorldImpl(entity.world);
    }

    public void teleport(Vec3d pos) {
        entity.teleport(pos.x, pos.y, pos.z);
    }

}
