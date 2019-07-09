package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RawCommandEventImpl implements RawCommandEvent {

    private final Player player;

    private String command;
    private boolean isCancelled;

    public RawCommandEventImpl(Player player, String command) {
        this.player = player;
        this.command = command;
    }

    public RawCommandEventImpl(String command) {
        this.player = null;
        this.command = command;
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
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    @NotNull
    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(@NotNull String command) {
        this.command = command;
    }

}
