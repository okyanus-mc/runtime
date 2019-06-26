package club.issizler.okyanus.runtime.mixin.optimizations;

import net.minecraft.util.PackedIntegerArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PackedIntegerArray.class)
public abstract class PackedIntegerArrayMixin {

    // This class is intended to ""remove"" these calls by redirecting them to nothingness. No clue if it works or not.

    @Redirect(at=@At(value = "INVOKE", target = "Lorg/apache/commons/lang3/Validate;inclusiveBetween(JJJ)V"), method = "<init>(II[J)V")
    private void oky$init(long start, long end, long value) {
        // Don't do anything.
    }

    @Redirect(at=@At(value = "INVOKE", target = "Lorg/apache/commons/lang3/Validate;inclusiveBetween(JJJ)V"), method = "setAndGetOldValue")
    private void oky$setAndGetOldValue(long start, long end, long value) {
        // Don't do anything.
    }

    @Redirect(at=@At(value = "INVOKE", target = "Lorg/apache/commons/lang3/Validate;inclusiveBetween(JJJ)V"), method = "set")
    private void oky$set(long start, long end, long value) {
        // Don't do anything.
    }

    @Redirect(at=@At(value = "INVOKE", target = "Lorg/apache/commons/lang3/Validate;inclusiveBetween(JJJ)V"), method = "get")
    private void oky$get(long start, long end, long value) {
        // Don't do anything.
    }

}
