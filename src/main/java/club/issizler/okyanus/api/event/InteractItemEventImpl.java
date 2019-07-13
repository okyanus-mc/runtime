package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerInteractItemC2SPacket;
import org.jetbrains.annotations.NotNull;

public class InteractItemEventImpl implements InteractItemEvent {

    private boolean isCancelled = false;

    private final PlayerInteractItemC2SPacket packet;
    private final Player player;

    public InteractItemEventImpl(@NotNull final PlayerInteractItemC2SPacket playerInteractItemC2SPacket,
                                 @NotNull final ServerPlayerEntity player) {
        this.player = new PlayerImpl(player);
        this.packet = playerInteractItemC2SPacket;
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
    public Player getPlayer() {
        return player;
    }

}
