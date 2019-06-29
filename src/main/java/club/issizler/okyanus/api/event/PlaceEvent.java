package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.Player;
import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractBlockC2SPacket;

public class PlaceEvent implements PlayerEvent, Cancellable, Locateable {

    private PlayerInteractBlockC2SPacket packet;
    private Player player;

    private boolean isCancelled;

    public PlaceEvent(PlayerInteractBlockC2SPacket packet, ServerPlayerEntity playerEntity) {
        this.packet = packet;
        this.player = new Player(playerEntity);
    }

    public Player getPlayer() {
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
