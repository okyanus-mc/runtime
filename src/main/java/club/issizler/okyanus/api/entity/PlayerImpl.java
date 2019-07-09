package club.issizler.okyanus.api.entity;

import club.issizler.okyanus.api.chat.MessageType;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.Block;
import club.issizler.okyanus.api.world.mck.MckBlock;
import club.issizler.okyanus.runtime.utils.accessors.EntityServerRaytraceable;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.HitResult;
import java.util.Optional;

public class PlayerImpl extends EntityImpl implements Player {

    private final ServerPlayerEntity player;

    public PlayerImpl(ServerPlayerEntity player) {
        super(player);

        this.player = player;
    }

    @Override
    public Optional<Block> getTargetBlock(double distance, boolean returnFluids) {
        HitResult res = ((EntityServerRaytraceable) player).rayTraceInServer(distance, 1.0f, returnFluids); // 1.0f = unknown

        if (res.getType() != HitResult.Type.BLOCK)
            return Optional.empty();

        return Optional.ofNullable(getWorld().getBlockAt(new Vec3d(res.getPos().x, res.getPos().y, res.getPos().z)));
    }

    @Override
    public void teleport(Vec3d pos) {
        player.teleport(pos.x, pos.y, pos.z, true);
    }

    @Override
    public void send(String message) {
        send(message, MessageType.CHAT);
    }

    @Override
    public void send(String message, MessageType type) {
        net.minecraft.network.MessageType nmsType;

        switch (type) {
            case INFO:
                nmsType = net.minecraft.network.MessageType.GAME_INFO;
                break;
            case SYSTEM:
                nmsType = net.minecraft.network.MessageType.SYSTEM;
                break;
            default:
                nmsType = net.minecraft.network.MessageType.CHAT;
                break;
        }

        player.sendChatMessage(new LiteralText(message), nmsType);
    }

    @Override
    public void kick(String message) {
        player.networkHandler.disconnect(new LiteralText(message));
    }

    @Override
    public boolean isOp() {
        return player.server.getPlayerManager().isOperator(player.getGameProfile());
    }

    @Override
    public void setOp(boolean isOp) {
        if (isOp)
            player.server.getPlayerManager().addToOperators(player.getGameProfile());
        else
            player.server.getPlayerManager().removeFromOperators(player.getGameProfile());
    }

    @Override
    public String getIdentifier() {
        return getUUID().toString();
    }

}
