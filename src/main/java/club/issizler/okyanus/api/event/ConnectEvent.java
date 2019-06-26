package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.Player;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;

public class ConnectEvent implements Event {

    private ClientConnection connection;
    private Player player;

    public ConnectEvent(ClientConnection connection, ServerPlayerEntity playerEntity) {
        this.connection = connection;
        this.player = new Player(playerEntity);
    }

    public Player getPlayer() {
        return player;
    }
}
