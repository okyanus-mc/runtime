package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.Player;
import net.minecraft.server.network.ServerPlayerEntity;

public class DisconnectEvent implements PlayerEvent {

    private Player player;

    public DisconnectEvent(ServerPlayerEntity playerEntity) {
        this.player = new Player(playerEntity);
    }

    public Player getPlayer() {
        return player;
    }

}
