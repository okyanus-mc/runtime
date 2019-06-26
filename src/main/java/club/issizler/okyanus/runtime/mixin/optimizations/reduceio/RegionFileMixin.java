// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0078-Reduce-IO-ops-opening-a-new-region-file.patch

package club.issizler.okyanus.runtime.mixin.optimizations.reduceio;

import net.minecraft.world.storage.RegionFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Mixin(RegionFile.class)
public abstract class RegionFileMixin {

    private IntBuffer headerAsInts;

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/io/RandomAccessFile;seek(J)V"), method = "<init>")
    private void oky$init$seek(RandomAccessFile randomAccessFile, long pos) throws IOException {
        randomAccessFile.seek(pos);

        ByteBuffer header = ByteBuffer.allocate(8192);
        while (header.hasRemaining())
            if (randomAccessFile.getChannel().read(header) == -1)
                throw new EOFException();

        header.clear();
        headerAsInts = header.asIntBuffer();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/io/RandomAccessFile;readInt()I"), method = "<init>")
    private int oky$init$readInt(RandomAccessFile randomAccessFile) {
        return headerAsInts.get();
    }

}
