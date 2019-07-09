package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerMoveC2SPacket;
import org.jetbrains.annotations.NotNull;

public class MoveEventImpl implements MoveEvent {

    private Player player;
    private PlayerMoveC2SPacket packet;

    private boolean isCancelled = false;

    public MoveEventImpl(PlayerMoveC2SPacket packet, ServerPlayerEntity playerEntity) {
        this.packet = packet;
        this.player = new PlayerImpl(playerEntity);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

}
