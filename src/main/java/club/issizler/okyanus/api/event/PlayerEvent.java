package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.Player;

public interface PlayerEvent extends Event {

    public Player getPlayer();

}
