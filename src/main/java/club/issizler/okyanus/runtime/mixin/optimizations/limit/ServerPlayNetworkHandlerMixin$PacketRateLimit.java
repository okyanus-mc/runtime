// https://github.com/PaperMC/Paper/blob/bf57c7ccfabe3f67f626c12985a9990c05abcfe3/Spigot-Server-Patches/0424-Rate-limit-packets-incoming-from-players.patch

package club.issizler.okyanus.runtime.mixin.optimizations.limit;

import club.issizler.okyanus.runtime.Runtime;
import club.issizler.okyanus.runtime.utils.PacketLimiter;
import club.issizler.okyanus.runtime.utils.PacketRateLimitable;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$PacketRateLimit implements PacketRateLimitable {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    public abstract void disconnect(Text text_1);

    @Shadow @Final private static Logger LOGGER;
    @Shadow public ServerPlayerEntity player;
    private PacketLimiter limiter;
    private boolean spamKicked;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(MinecraftServer minecraftServer_1, ClientConnection clientConnection_1, ServerPlayerEntity serverPlayerEntity_1, CallbackInfo ci) {
        if (Runtime.PACKET_RATE_LIMIT != 0)
            limiter = new PacketLimiter(Runtime.PACKET_RATE_LIMIT_INTERVAL * 1000.0, 100);
    }

    public boolean rateLimit(final Packet packet) {
        final PacketLimiter limiter = this.limiter;

        if (limiter == null)
            return false;

        if (spamKicked)
            return true;

        final int limit = Runtime.PACKET_RATE_LIMIT;

        synchronized (limiter) {
            if (spamKicked) // re-check if it changes while lock happens
                return true;

            final int packets = limiter.incrementPackets(1);
            if (packets / (limiter.intervalTime / 1000.0) <= limit)
                return false;

            this.spamKicked = true;
            this.server.executeSync(() -> {
                this.disconnect(new LiteralText("Too fast!"));
                LOGGER.warn(player.getName().asFormattedString() + " was kicked for sending too many packets!");
            });
        }

        return true;
    }

}
