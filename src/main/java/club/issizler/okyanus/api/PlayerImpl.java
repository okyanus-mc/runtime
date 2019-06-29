package club.issizler.okyanus.api;

import club.issizler.okyanus.api.chat.MessageType;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.Block;
import club.issizler.okyanus.api.world.WorldImpl;
import club.issizler.okyanus.runtime.SomeGlobals;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.HitResult;

import java.util.UUID;

public class PlayerImpl implements Player {

    private ServerPlayerEntity player;

    public PlayerImpl(String name) {
        player = SomeGlobals.dedicatedServer.getPlayerManager().getPlayer(name);
    }

    public PlayerImpl(UUID uuid) {
        player = SomeGlobals.dedicatedServer.getPlayerManager().getPlayer(uuid);
    }

    public PlayerImpl(ServerPlayerEntity e) {
        player = e;
    }

    public String getName() {
        return player.getName().asFormattedString();
    }

    public String getCustomName() {
        if (player.getCustomName() == null)
            return getName();

        return player.getCustomName().asFormattedString();
    }

    public void setCustomName(String name) {
        player.setCustomName(new LiteralText(name));
    }

    public UUID getUUID() {
        return player.getUuid();
    }

    public Vec3d getPos() {
        net.minecraft.util.math.Vec3d pos = player.getPos();

        return new Vec3d(pos.x, pos.y, pos.z);
    }

    public WorldImpl getWorld() {
        return new WorldImpl(player.world);
    }

    public Block getLookedBlock(double distance, boolean returnFluids) {
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
