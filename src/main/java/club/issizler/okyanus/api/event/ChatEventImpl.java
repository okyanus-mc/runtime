package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.runtime.Runtime;
import org.jetbrains.annotations.NotNull;

public class ChatEventImpl implements ChatEvent {

    private Player player;

    private String format;
    private String message;

    private boolean cancelled;

    public ChatEventImpl(Player player, String message) {
        this.player = player;
        this.message = message;

        this.format = Runtime.config.get("chat.defaultFormat");
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        cancelled = isCancelled;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public String getFormattedMessage() {
        return String.format(format, this.player.getCustomName(), this.message);
    }

    public void setFormat(@NotNull String format) {
        this.format = format;
    }

}
