package club.issizler.okyanus.runtime.utils.accessors;

import net.minecraft.network.Packet;

public interface PacketRateLimitable {

    boolean rateLimit(final Packet packet);

}
