package club.issizler.okyanus.runtime.mixin.events;

import club.issizler.okyanus.api.Okyanus;
import club.issizler.okyanus.api.event.PlaceEventImpl;
import club.issizler.okyanus.api.math.Vec3d;
import club.issizler.okyanus.api.world.BlockImpl;
import club.issizler.okyanus.api.world.WorldImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    @Nullable
    protected abstract BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    private void oky$place(ItemPlacementContext itemPlacementContext_1, CallbackInfoReturnable<ActionResult> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) itemPlacementContext_1.getPlayer();

        BlockPos mcPos = itemPlacementContext_1.getBlockPos();
        Vec3d pos = new Vec3d(mcPos.getX(), mcPos.getY(), mcPos.getZ());

        club.issizler.okyanus.api.world.Block block = new BlockImpl(new WorldImpl(itemPlacementContext_1.getWorld()), getPlacementState(itemPlacementContext_1), pos);
        PlaceEventImpl e = Okyanus.getServer().getEventRegistry().trigger(new PlaceEventImpl(itemPlacementContext_1, player, block));

        if (e.isCancelled()) {
            player.world.setBlockState(itemPlacementContext_1.getBlockPos(), Blocks.AIR.getDefaultState()); // This might be bad, not sure

            cir.setReturnValue(ActionResult.FAIL);
            cir.cancel();
        }
    }

}
