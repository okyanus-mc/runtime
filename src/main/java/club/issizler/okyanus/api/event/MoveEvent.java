package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.Player;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;

public class MoveEvent implements Event, Cancellable {

    private Player player;
    private PlayerMoveC2SPacket packet;

    private boolean isCancelled = false;

    public MoveEvent(PlayerMoveC2SPacket packet, ServerPlayerEntity player) {
        this.packet = packet;
        this.player = new Player(player);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getPlayer() {
        return player;
    }
}
