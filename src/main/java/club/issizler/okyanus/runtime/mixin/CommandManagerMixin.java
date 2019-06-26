package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.api.cmd.ArgumentType;
import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandSource;
import club.issizler.okyanus.runtime.SomeGlobals;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {

    @Final
    @Shadow
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void oky$init(boolean isDedicated, CallbackInfo ci) {
        SomeGlobals.commandDispatcher = dispatcher;
    }

}
