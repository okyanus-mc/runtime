// https://github.com/SpongePowered/SpongeCommon/blob/1.13/src/main/java/org/spongepowered/common/mixin/optimization/MixinWorldServer_Explosion.java

package club.issizler.okyanus.runtime.mixin.optimizations.explosion;

import club.issizler.okyanus.runtime.Runtime;
import net.minecraft.client.network.packet.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    @Shadow
    @Final
    private List<ServerPlayerEntity> players;

    protected ServerWorldMixin(LevelProperties levelProperties_1, DimensionType dimensionType_1, BiFunction<World, Dimension, ChunkManager> biFunction_1, Profiler profiler_1, boolean boolean_1) {
        super(levelProperties_1, dimensionType_1, biFunction_1, profiler_1, boolean_1);
    }

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ServerWorld;players:Ljava/util/List;", opcode = Opcodes.GETFIELD), method = "createExplosion")
    private List<ServerPlayerEntity> oky$createExplosion$playersFieldAccess(ServerWorld serverWorld) {
        if (!Runtime.USE_FAST_EXPLOSIONS)
            return serverWorld.getPlayers();

        return Collections.emptyList();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/Explosion;affectWorld(Z)V"), method = "createExplosion")
    private void oky$createExplosion$affectWorld(Explosion explosion, boolean bool) {
        if (!Runtime.USE_FAST_EXPLOSIONS) {
            explosion.affectWorld(bool);
            return;
        }

        explosion.affectWorld(true); // Forced to be true

        for (ServerPlayerEntity player : this.players) {
            final Vec3d knockback = explosion.getAffectedPlayers().get(player);
            if (knockback != null) {
                // In Vanilla, affectWorld always updates the 'motion[xyz]' fields for every entity in range.
                // However, this field is completely ignored for players (since 'velocityChanged') is never set, and
                // a completely different value is sent through 'SPacketExplosion'.

                // To replicate this behavior, we manually send a velocity packet. It's critical that we don't simply
                // add to the 'motion[xyz]' fields, as that will end up using the value set by 'affectWorld', which must be
                // ignored.

                player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player.getEntityId(), new Vec3d(knockback.x, knockback.y, knockback.z)));
            }
        }

    }

}
