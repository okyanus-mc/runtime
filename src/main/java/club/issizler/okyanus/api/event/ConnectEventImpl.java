package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ConnectEventImpl implements ConnectEvent {

    private String cancelReason = "Disconnected";
    private String message;

    private final ClientConnection connection;
    private final Player player;
    private boolean isCancelled = false;

    public ConnectEventImpl(@NotNull final ClientConnection connection,
                            @NotNull final ServerPlayerEntity playerEntity,
                            @NotNull final String message) {
        this.connection = connection;
        this.player = new PlayerImpl(playerEntity);
        this.message = message;
    }

    @NotNull
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

    @NotNull
    @Override
    public String getCancelReason() {
        return cancelReason;
    }

    @Override
    public void setCancelReason(@NotNull String reason) {
        cancelReason = reason;
    }

    @Override
    public InetAddress getAddress() {
        return ((InetSocketAddress) connection.getAddress()).getAddress();
    }

    @NotNull
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(@NotNull String message) {
        this.message = message;
    }

}
