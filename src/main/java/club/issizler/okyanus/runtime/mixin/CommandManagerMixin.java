package club.issizler.okyanus.runtime.mixin;

import club.issizler.okyanus.api.cmd.ArgumentType;
import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandSource;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
            ArgumentBuilder argumentBuilder = null;

            List<Pair<String, ArgumentType>> args = command.__internal_args();
            Command<ServerCommandSource> cmd = context -> command.__internal_runnable().run(new CommandSource(context));

            if (command.__internal_isOpOnly())
                builder = builder.requires(source -> source.hasPermissionLevel(3));

            Collections.reverse(args);
            for (Pair<String, ArgumentType> arg : args) {
                com.mojang.brigadier.arguments.ArgumentType type;

                switch (arg.getRight()) {
                    case PLAYER:
                        type = EntityArgumentType.players();
                        break;
                    case TEXT:
                    default:
                        type = StringArgumentType.string();
                        break;
                }

                if (argumentBuilder == null) {
                    argumentBuilder = CommandManager.argument(arg.getLeft(), type).executes(cmd);
                } else {
                    argumentBuilder = CommandManager.argument(arg.getLeft(), type).then(argumentBuilder);
                }
            }

            if (argumentBuilder != null) {
                builder.then(argumentBuilder);
            } else {
                builder = builder.executes(cmd);
            }

            dispatcher.register(builder);
        }

        isRegistered = true;
    }

}
