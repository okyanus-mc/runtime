package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;

public class DisconnectEventImpl implements DisconnectEvent {

    private PlayerImpl player;

    public DisconnectEventImpl(ServerPlayerEntity playerEntity) {
        this.player = new PlayerImpl(playerEntity);
    }

    public PlayerImpl getPlayer() {
        return player;
    }

}
