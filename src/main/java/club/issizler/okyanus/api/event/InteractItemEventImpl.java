package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractItemC2SPacket;

public class InteractItemEventImpl implements InteractItemEvent {

    private boolean isCancelled = false;

    private PlayerInteractItemC2SPacket packet;
    private Player player;

    public InteractItemEventImpl(PlayerInteractItemC2SPacket playerInteractItemC2SPacket_1, ServerPlayerEntity player) {
        this.player = new PlayerImpl(player);
        this.packet = playerInteractItemC2SPacket_1;
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

}
