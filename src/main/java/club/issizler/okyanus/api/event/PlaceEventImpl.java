package club.issizler.okyanus.api.event;

import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class PlaceEventImpl implements PlaceEvent {

    private final PlayerImpl player;
    private final ItemUsageContext context;
    private final Block block;

    private boolean isCancelled;

    public PlaceEventImpl(@NotNull final ItemUsageContext itemUsageContext,
                          @NotNull final PlayerEntity player, Block block) {
        this.context = itemUsageContext;
        this.player = new PlayerImpl((ServerPlayerEntity) player);
        this.block = block;
    }

    @NotNull
    public PlayerImpl getPlayer() {
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
        BlockPos pos = context.getBlockPos();
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    @NotNull
    @Override
    public Block getBlock() {
        return block;
    }
}
