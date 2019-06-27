package club.issizler.okyanus.runtime.utils;

import net.minecraft.network.Packet;

public interface PacketRateLimitable {

    boolean rateLimit(final Packet packet);

}
