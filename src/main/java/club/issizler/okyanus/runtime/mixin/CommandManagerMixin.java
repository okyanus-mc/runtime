package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandSource;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {

    @Final
    @Shadow
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Final
    @Shadow
    private static Logger LOGGER;

    private boolean isRegistered;

    @Inject(at = @At("HEAD"), method = "execute")
    private void oky$execute(ServerCommandSource serverCommandSource_1, String string_1, CallbackInfoReturnable<Integer> cir) {
        // HACK: Mixins execute earlier than mod entrypoints, so let's just register commands when they're used instead
        // This might bite us in the ass later

        if (isRegistered) return;

        LOGGER.info("Okyanus: Late command registration");
        for (CommandBuilder command : club.issizler.okyanus.api.cmd.CommandManager.INSTANCE.__internal_getCommands()) {
            LiteralArgumentBuilder<ServerCommandSource> builder = literal(command.__internal_name());

            if (command.__internal_isOpOnly())
                builder = builder.requires(source -> source.hasPermissionLevel(3));

            builder = builder.executes(context -> command.__internal_runnable().run(new CommandSource(context)));
            dispatcher.register(builder);
        }

        isRegistered = true;
    }

}
