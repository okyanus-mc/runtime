package club.issizler.okyanus.runtime.mixin.optimizations.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.Int2ObjectBiMap;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleRegistry.class)
public abstract class MaterialsRegistryMixin<T> {

    @Mutable
    @Shadow
    @Final
    protected Int2ObjectBiMap<T> indexedEntries;

    @Mutable
    @Shadow
    @Final
    protected BiMap<Identifier, T> entries;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(CallbackInfo ci) {
        indexedEntries = new Int2ObjectBiMap<>(2048);
        entries = HashBiMap.create(2048);
    }

}
