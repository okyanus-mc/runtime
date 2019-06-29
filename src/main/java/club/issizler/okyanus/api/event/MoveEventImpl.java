package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;

public class MoveEventImpl implements MoveEvent {

    private PlayerImpl player;
    private PlayerMoveC2SPacket packet;

    private boolean isCancelled = false;

    public MoveEventImpl(PlayerMoveC2SPacket packet, ServerPlayerEntity player) {
        this.packet = packet;
        this.player = new PlayerImpl(player);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public PlayerImpl getPlayer() {
        return player;
    }

}
