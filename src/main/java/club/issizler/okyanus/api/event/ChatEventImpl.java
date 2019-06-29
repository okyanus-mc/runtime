package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.runtime.Runtime;

public class ChatEventImpl implements ChatEvent {

    private PlayerImpl player;

    private String format;
    private String message;

    private boolean cancelled;

    public ChatEventImpl(PlayerImpl player, String message) {
        this.player = player;
        this.message = message;

        this.format = Runtime.config.get("chat.defaultFormat");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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

    public PlayerImpl getPlayer() {
        return player;
    }

    public String getFormattedMessage() {
        return String.format(format, this.player.getCustomName(), this.message);
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
