package club.issizler.okyanus.api.entity;

import club.issizler.okyanus.api.chat.MessageType;
import club.issizler.okyanus.api.json.JsonCompound;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.Block;
import club.issizler.okyanus.runtime.utils.accessors.EntityServerRaytraceable;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.client.network.packet.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import org.cactoos.Proc;
import org.cactoos.scalar.And;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerImpl extends EntityImpl implements Player {

    private final ServerPlayerEntity player;

    public PlayerImpl(@NotNull final ServerPlayerEntity player) {
        super(player);

        this.player = player;
    }

    @Override
    public Optional<Block> getTargetBlock(double distance, boolean returnFluids) {
        HitResult res = ((EntityServerRaytraceable) player).rayTraceInServer(distance, 1.0f, returnFluids); // 1.0f = unknown

        if (res.getType() != HitResult.Type.BLOCK)
            return Optional.empty();

        return Optional.of(getWorld().getBlockAt(new Vec3d(res.getPos().x, res.getPos().y, res.getPos().z)));
    }

    @Override
    public void teleport(@NotNull Vec3d pos) {
        player.teleport(pos.x, pos.y, pos.z, true);
    }

    @Override
    public void sendMessage(@NotNull final String... messages) {
        try {
            new And(
                (Proc<@NotNull String>) this::send,
                messages
            ).value();
        } catch (Exception e) {
            // Ignore...
        }
    }

    @Override
    public void sendRawJson(@NotNull JsonCompound jsonCompound) {
        player.networkHandler.sendPacket(new ChatMessageS2CPacket(Text.Serializer.fromJson(jsonCompound.convert())));
    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subtitle, int fadeIn, int stay, int fadeOut) {
        TitleS2CPacket times = new TitleS2CPacket(fadeIn, stay, fadeOut);
        player.networkHandler.sendPacket(times);

        if (!title.isEmpty()) {
            TitleS2CPacket packetTitle = new TitleS2CPacket(TitleS2CPacket.Action.TITLE, new LiteralText(title));
            player.networkHandler.sendPacket(packetTitle);
        }

        if (!subtitle.isEmpty()) {
            TitleS2CPacket packetSubtitle = new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, new LiteralText(subtitle));
            player.networkHandler.sendPacket(packetSubtitle);
        }
    }

    @Override
    public void kick(@NotNull String message) {
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

    @NotNull
    @Override
    public String getIdentifier() {
        return getUUID().toString();
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

}
