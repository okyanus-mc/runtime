package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.PlayerActionC2SPacket;
import org.jetbrains.annotations.NotNull;

public class BreakEventImpl implements BreakEvent {

    private final PlayerActionC2SPacket packet;
    private final Player player;

    private boolean isCancelled;

    public BreakEventImpl(@NotNull final PlayerActionC2SPacket packet, @NotNull final ServerPlayerEntity playerEntity) {
        this.packet = packet;
        this.player = new PlayerImpl(playerEntity);
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
    public Vec3d getLocation() {
        return new Vec3d(packet.getPos().getX(), packet.getPos().getY(), packet.getPos().getZ());
    }

    @NotNull
    @Override
    public Block getBlock() {
        return player.getWorld().getBlockAt(getLocation());
    }
}
