// https://github.com/PaperMC/Paper/blob/ver/1.14/Spigot-Server-Patches/0278-MC-135506-Experience-should-save-as-Integers.patch
package club.issizler.okyanus.runtime.mixin.fixes;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin {

    // I just don't give a damn about overwriting anymore, and that's a disappointing perspective at Mixins

    @Shadow
    private int health;

    @Shadow
    public int orbAge;

    @Shadow
    private int amount;

    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Fix MC-135506
     */
    @Overwrite
    public void writeCustomDataToTag(CompoundTag compoundTag_1) {
        compoundTag_1.putShort("Health", (short)this.health);
        compoundTag_1.putShort("Age", (short)this.orbAge);
        compoundTag_1.putInt("Value", this.amount);
    }
    /**
     * @author Aikar @ Paper & Okyanus
     * @reason Fix MC-135506
     */
    @Overwrite
    public void readCustomDataFromTag(CompoundTag compoundTag_1) {
        this.health = compoundTag_1.getShort("Health");
        this.orbAge = compoundTag_1.getShort("Age");
        this.amount = compoundTag_1.getInt("Value");
    }

}
