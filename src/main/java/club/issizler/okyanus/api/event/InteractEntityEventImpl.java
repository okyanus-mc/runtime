package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractEntityC2SPacket;
import org.jetbrains.annotations.NotNull;

public class InteractEntityEventImpl implements InteractEntityEvent {

    private boolean isCancelled;

    private Player player;
    private PlayerInteractEntityC2SPacket packet;

    public InteractEntityEventImpl(PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket_1, ServerPlayerEntity player) {
        this.player = new PlayerImpl(player);
        this.packet = playerInteractEntityC2SPacket_1;
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
    @Override
    public Player getPlayer() {
        return player;
    }

}
