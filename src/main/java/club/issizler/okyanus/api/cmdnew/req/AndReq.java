package club.issizler.okyanus.api.cmdnew.req;

import club.issizler.okyanus.api.cmdnew.CommandSender;
import net.minecraft.server.command.ServerCommandSource;
import org.cactoos.Scalar;
import org.cactoos.scalar.And;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class AndReq implements Predicate<ServerCommandSource> {

    private final List<Requirement> requirements;
    private final CommandSender sender;
    private final String[] inputs;
    private final int location;

    public AndReq(@NotNull final List<Requirement> requirements,
                  @NotNull final CommandSender sender,
                  @NotNull final String[] inputs,
                  int location) {
        this.requirements = requirements;
        this.sender = sender;
        this.inputs = inputs;
        this.location = location;
    }

    @Override
    public boolean test(ServerCommandSource serverCommandSource) {
        final Scalar<Boolean> and = new And(
            requirement -> {
                return requirement.control(sender, inputs, location);
            },
            requirements
        );
        try {
            return and.value();
        } catch (Exception e) {
            return false;
        }
    }
}
