package club.issizler.okyanus.api.entity;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.chat.MessageType;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.Block;
import club.issizler.okyanus.api.world.World;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.HitResult;

import java.util.UUID;

public class PlayerImpl implements Player {

    private final ServerPlayerEntity player;
    private final Entity entity;

    public PlayerImpl(Player player) {
        if (!(player instanceof PlayerImpl))
            throw new IllegalStateException("The parameter named player is must be a PlayerImpl");

        final PlayerImpl playerImpl = (PlayerImpl)player;

        this.player = playerImpl.player;
        this.entity = playerImpl.entity;
    }

    public PlayerImpl(ServerPlayerEntity player, Entity entity) {
        this.player = player;
        this.entity = entity;
    }

    public PlayerImpl(String name) {
        this(Okyanus.getServer().getPlayer(name));
    }

    public String getName() {
        return entity.getName();
    }

    public String getCustomName() {
        return entity.getCustomName();
    }

    public void setCustomName(String name) {
        entity.setCustomName(name);
    }

    public UUID getUUID() {
        return entity.getUUID();
    }

    public Vec3d getPos() {
        return entity.getPos();
    }

    public World getWorld() {
        return entity.getWorld();
    }

    public Block getTargetBlock(double distance, boolean returnFluids) {
        HitResult res = player.rayTrace(distance, 1.0f, returnFluids); // 1.0f = unknown

        if (res.getType() != HitResult.Type.BLOCK)
            return null;

        return getWorld().getBlockAt(new Vec3d(res.getPos().x, res.getPos().y, res.getPos().z));
    }

    public void teleport(Vec3d pos) {
        player.teleport(pos.x, pos.y, pos.z, true);
    }

    public void send(String message) {
        send(message, MessageType.CHAT);
    }

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

    public void kick(String message) {
        player.networkHandler.disconnect(new LiteralText(message));
    }

}
