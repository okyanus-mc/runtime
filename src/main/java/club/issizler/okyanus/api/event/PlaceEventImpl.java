package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.PlayerImpl;
import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractBlockC2SPacket;

public class PlaceEventImpl implements PlaceEvent {

    private PlayerInteractBlockC2SPacket packet;
    private PlayerImpl player;

    private boolean isCancelled;

    public PlaceEventImpl(PlayerInteractBlockC2SPacket packet, ServerPlayerEntity playerEntity) {
        this.packet = packet;
        this.player = new PlayerImpl(playerEntity);
    }

    public PlayerImpl getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public Vec3d getLocation() {
        return new Vec3d(packet.getHitY().getBlockPos().getX(), packet.getHitY().getBlockPos().getY(), packet.getHitY().getBlockPos().getZ());
    }

}
