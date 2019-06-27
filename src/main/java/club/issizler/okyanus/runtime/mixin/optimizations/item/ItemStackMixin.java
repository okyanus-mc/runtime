package club.issizler.okyanus.runtime.mixin.optimizations.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Shadow @Final @Deprecated private Item item;

    @Shadow private int count;

    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Optimization
     */
    @Overwrite
    public boolean isEmpty() {
        return this.item == null || this.item == Items.AIR || this.count <= 0; // TODO compare this to ItemStack.EMPTY whenever you find a good way to get `this` in mixins.
    }

}
