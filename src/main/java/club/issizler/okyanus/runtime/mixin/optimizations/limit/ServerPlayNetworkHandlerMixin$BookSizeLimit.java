// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0343-Book-Size-Limits.patch

package club.issizler.okyanus.runtime.mixin.optimizations.limit;

import club.issizler.okyanus.runtime.Runtime;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.BookUpdateC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.nio.charset.StandardCharsets.UTF_8;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin$BookSizeLimit {

    @Shadow @Final private MinecraftServer server;

    @Shadow @Final private static Logger LOGGER;

    @Shadow public ServerPlayerEntity player;

    @Shadow public abstract void disconnect(Text text_1);

    @Inject(at = @At("HEAD"), method = "onBookUpdate", cancellable = true)
    private void oky$onBookUpdate(BookUpdateC2SPacket bookUpdateC2SPacket_1, CallbackInfo ci) {
        ItemStack testStack = bookUpdateC2SPacket_1.getBook();

        if (!server.isOnThread() && !testStack.isEmpty() && testStack.getTag() != null) {
            ListTag pageList = testStack.getTag().getList("pages", 8);
            int maxBookPageSize = Runtime.config.get("limits.maxBookPageSize");
            double multiplier = Math.max(0.3d, Math.min(1d, Runtime.config.get("limits.maxBookTotalSizeMultiplier")));

            long byteAllowed = maxBookPageSize;
            long byteTotal = 0;

            for (int i = 0; i < pageList.size(); ++i) {
                String testString = pageList.getString(i);

                int byteLength = testString.getBytes(UTF_8).length;
                int length = testString.length();
                int multibytes = 0;

                byteTotal += byteLength;

                if (byteLength != length)
                    for (char c : testString.toCharArray())
                        if (c > 127)
                            multibytes++;

                byteAllowed += (maxBookPageSize * Math.min(1, Math.max(0.1d, length / 255d))) * multiplier;

                if (multibytes > 1)
                    byteAllowed -= multibytes;
            }

            if (byteTotal > byteAllowed) {
                LOGGER.warn("Okyanus: " + this.player.getName() + " sent a very large book of size " + byteTotal + "!");
                server.executeSync(() -> this.disconnect(new LiteralText("Book too large!"))); // TODO: is executeSync postToMainThread
                ci.cancel();
            }
        }
    }

}
