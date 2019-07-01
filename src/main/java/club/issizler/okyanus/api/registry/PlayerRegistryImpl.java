package club.issizler.okyanus.api.registry;

import club.issizler.okyanus.api.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerRegistryImpl implements PlayerRegistry {

    private final List<Player> playerList = new ArrayList<>();

    @Override
    public Player getPlayer(String name) {
        for (Player player : playerList)
            if (player.getName().equals(name))
                return player;
        // Return a mock player not null!
        return null;
    }

}
