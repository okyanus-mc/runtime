package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;

public class DisconnectEventImpl implements DisconnectEvent {

    private Player player;

    public DisconnectEventImpl(ServerPlayerEntity playerEntity) {
        this.player = new PlayerImpl(playerEntity);
    }

    public Player getPlayer() {
        return player;
    }

}
