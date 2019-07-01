package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.event.PlaceEventImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItem$PlaceEvent {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;")
    private void oky$place(ItemPlacementContext itemPlacementContext_1, CallbackInfoReturnable<ActionResult> cir) {
        PlayerEntity player = itemPlacementContext_1.getPlayer();
        PlaceEventImpl e = Okyanus.getServer().triggerEvent(new PlaceEventImpl(itemPlacementContext_1, player));

        if (e.isCancelled()) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

}
