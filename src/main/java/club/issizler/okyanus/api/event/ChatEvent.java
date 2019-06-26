package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.Player;

public class ChatEvent implements Event, Cancellable {

    private String format = "<%s> %s";

    private Player player;
    private String message;

    private boolean cancelled;

    public ChatEvent(Player player, String message) {
        this.player = player;
        this.message = message;
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

    public Player getPlayer() {
        return player;
    }

    public String getFormattedMessage() {
        return String.format(format, this.player.getCustomName(), this.message);
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
