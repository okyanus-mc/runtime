package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ConnectEventImpl implements ConnectEvent {

    private String cancelReason = "Disconnected";
    private String message;

    private ClientConnection connection;
    private Player player;
    private boolean isCancelled = false;

    public ConnectEventImpl(ClientConnection connection, ServerPlayerEntity playerEntity, String message) {
        this.connection = connection;
        this.player = new PlayerImpl(playerEntity);
        this.message = message;
    }

    public Player getPlayer() {
        return player;
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
    public String getCancelReason() {
        return cancelReason;
    }

    @Override
    public void setCancelReason(String reason) {
        cancelReason = reason;
    }

    @Override
    public InetAddress getAddress() {
        return ((InetSocketAddress) connection.getAddress()).getAddress();
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
