package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;

public class DisconnectEventImpl implements DisconnectEvent {

    private String message;

    private Player player;

    public DisconnectEventImpl(ServerPlayerEntity playerEntity, String message) {
        this.player = new PlayerImpl(playerEntity);
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

}
