package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class DisconnectEventImpl implements DisconnectEvent {

    private String message;

    private final Player player;

    public DisconnectEventImpl(@NotNull final ServerPlayerEntity playerEntity, @NotNull final String message) {
        this.player = new PlayerImpl(playerEntity);
        this.message = message;
    }

    @NotNull
    public Player getPlayer() {
        return player;
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
