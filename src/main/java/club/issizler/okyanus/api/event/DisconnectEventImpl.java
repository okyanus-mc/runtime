package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.EntityImpl;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;

public class DisconnectEventImpl implements DisconnectEvent {

    private PlayerImpl player;

    public DisconnectEventImpl(ServerPlayerEntity playerEntity) {
        this.player = new PlayerImpl(
            playerEntity,
            new EntityImpl(playerEntity)
        );
    }

    public PlayerImpl getPlayer() {
        return player;
    }

}
