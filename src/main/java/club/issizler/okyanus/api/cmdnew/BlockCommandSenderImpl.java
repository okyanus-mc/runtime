package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.Block;
import club.issizler.okyanus.api.world.BlockImpl;
import club.issizler.okyanus.api.world.WorldImpl;
import club.issizler.okyanus.api.world.mck.MckBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cactoos.scalar.And;
import org.jetbrains.annotations.NotNull;

public class BlockCommandSenderImpl extends ServerCommandSender implements BlockCommandSender {

    private final ServerCommandSource sourceBlock;
    private final BlockEntity blockEntity;

    public BlockCommandSenderImpl(ServerCommandSource sourceBlock, BlockEntity blockEntity) {
        this.sourceBlock = sourceBlock;
        this.blockEntity = blockEntity;
    }

    @NotNull
    @Override
    public Block getBlock() {
        final BlockPos pos = blockEntity.getPos();
        final World world = blockEntity.getWorld();

        if (world == null)
            return new MckBlock();

        return new BlockImpl(
            new WorldImpl(
                blockEntity.getWorld()
            ),
            world.getBlockState(
                new BlockPos(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ()
                )
            ),
            new Vec3d(
                pos.getX(),
                pos.getY(),
                pos.getZ()
            )
        );
    }

    @Override
    public void sendMessage(@NotNull final String... messages) {
        try {
            new And(
                message -> {
                    sourceBlock.sendFeedback(new LiteralText(message), false);
                },
                messages
            ).value();
        } catch (Exception e) {
            // Ignore...
        }
    }

    @NotNull
    @Override
    public String getName() {
        return sourceBlock.getName() == null
            ? ""
            : sourceBlock.getName();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

}
