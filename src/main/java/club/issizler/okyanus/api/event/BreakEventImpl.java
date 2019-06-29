package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.PlayerImpl;
import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;

public class BreakEventImpl implements BreakEvent {

    private PlayerActionC2SPacket packet;
    private PlayerImpl player;

    private boolean isCancelled;

    public BreakEventImpl(PlayerActionC2SPacket packet, ServerPlayerEntity playerEntity) {
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
        return new Vec3d(packet.getPos().getX(), packet.getPos().getY(), packet.getPos().getZ());
    }

}
