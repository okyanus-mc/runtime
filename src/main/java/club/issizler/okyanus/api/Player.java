package club.issizler.okyanus.api;

import club.issizler.okyanus.api.chat.MessageType;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.World;
import club.issizler.okyanus.runtime.SomeGlobals;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.UUID;

public class Player {

    private ServerPlayerEntity player;

    public Player(String name) {
        player = SomeGlobals.dedicatedServer.getPlayerManager().getPlayer(name);
    }

    public Player(UUID uuid) {
        player = SomeGlobals.dedicatedServer.getPlayerManager().getPlayer(uuid);
    }

    public Player(ServerPlayerEntity e) {
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

    public World getWorld() {
        return new World(player.world);
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
