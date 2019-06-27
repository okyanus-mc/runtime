// https://github.com/PaperMC/Paper/blob/bf57c7ccfabe3f67f626c12985a9990c05abcfe3/Spigot-Server-Patches/0424-Rate-limit-packets-incoming-from-players.patch

package club.issizler.okyanus.runtime.mixin.optimizations.limit;

import club.issizler.okyanus.runtime.utils.PacketRateLimitable;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    /**
     * @author Spottedleaf @ Paper & Okyanus
     * @reason Packet rate limits
     */
    @Overwrite
    private static <T extends PacketListener> void handlePacket(Packet<T> packet_1, PacketListener packetListener_1) {
        if (packetListener_1 instanceof PacketRateLimitable)
            if (((PacketRateLimitable) packetListener_1).rateLimit(packet_1))
                return;

        packet_1.apply((T) packetListener_1);
    }

}
