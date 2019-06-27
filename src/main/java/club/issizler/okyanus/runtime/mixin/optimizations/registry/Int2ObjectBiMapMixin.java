package club.issizler.okyanus.runtime.mixin.optimizations.registry;

import net.minecraft.util.Int2ObjectBiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.BitSet;

// TODO: don't overwrite, like half of the entire class

@Mixin(Int2ObjectBiMap.class)
public abstract class Int2ObjectBiMapMixin<K> {

    @Shadow
    private int nextId;

    @Shadow
    private K[] values;

    @Shadow
    private int[] ids;

    @Shadow
    private K[] idToValues;

    @Shadow
    private int size;

    @Shadow protected abstract int findFree(int int_1);

    @Shadow protected abstract int getIdealIndex(@Nullable K object_1);

    private BitSet usedIds;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(int int_1, CallbackInfo ci) {
        usedIds = new BitSet();
    }

    /**
     * @author Andrew Steinborn @ Paper & Okyanus
     * @reason Optimization
     */
    @Overwrite
    private int nextId() {
        this.nextId = this.usedIds.nextClearBit(0);
        return this.nextId;
    }

    /**
     * @author Andrew Steinborn @ Paper & Okyanus
     * @reason Optimization
     */
    @Overwrite
    private void resize(int int_1) {
        K[] objects_1 = this.values;
        int[] ints_1 = this.ids;

        this.values = (K[]) new Object[int_1];
        this.idToValues = (K[]) new Object[int_1];

        this.ids = new int[int_1];

        this.nextId = 0;
        this.size = 0;

        this.usedIds.clear(); // Okyanus

        for (int int_2 = 0; int_2 < objects_1.length; ++int_2) {
            if (objects_1[int_2] != null) {
                this.put(objects_1[int_2], ints_1[int_2]);
            }
        }

    }

    /**
     * @author Andrew Steinborn @ Paper & Okyanus
     * @reason Optimization
     */
    @Overwrite
    public void put(K object_1, int int_1) {
        int int_2 = Math.max(int_1, this.size + 1);
        int int_3;
        if ((float)int_2 >= (float)this.values.length * 0.8F) {
            for(int_3 = this.values.length << 1; int_3 < int_1; int_3 <<= 1) {
            }

            this.resize(int_3);
        }

        int_3 = this.findFree(this.getIdealIndex(object_1));
        this.values[int_3] = object_1;
        this.ids[int_3] = int_1;
        this.idToValues[int_1] = object_1;
        this.usedIds.set(int_1); // Okyanus
        ++this.size;
        if (int_1 == this.nextId) {
            ++this.nextId;
        }
    }

    /**
     * @author Andrew Steinborn @ Paper & Okyanus
     * @reason Optimization
     */
    @Overwrite
    public void clear() {
        Arrays.fill(this.values, (Object)null);
        Arrays.fill(this.idToValues, (Object)null);
        this.nextId = 0;
        this.size = 0;

        this.usedIds.clear(); // Okyanus
    }

}
