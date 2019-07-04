package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.math.Vec3d;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;

public class InteractBlockEventImpl implements InteractBlockEvent {

    private boolean isCancelled = false;

    private PlayerInteractBlockC2SPacket packet;
    private Player player;

    public InteractBlockEventImpl(PlayerInteractBlockC2SPacket playerInteractBlockC2SPacket_1, ServerPlayerEntity player) {
        this.player = new PlayerImpl(player);
        this.packet = playerInteractBlockC2SPacket_1;
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
    public Player getPlayer() {
        return player;
    }

    @Override
    public Vec3d getLocation() {
        BlockPos pos = packet.getHitY().getBlockPos();
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

}
