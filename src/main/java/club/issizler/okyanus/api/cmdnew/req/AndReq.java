package club.issizler.okyanus.api.cmdnew.req;

import club.issizler.okyanus.api.cmdnew.CommandSource;
import club.issizler.okyanus.api.cmdnew.Requirement;
import net.minecraft.server.command.ServerCommandSource;
import org.cactoos.Scalar;
import org.cactoos.scalar.And;

import java.util.List;
import java.util.function.Predicate;

public class AndReq implements Predicate<ServerCommandSource> {

    private final List<Requirement> requirements;
    private final CommandSource source;
    private final String[] inputs;
    private final int location;

    public AndReq(List<Requirement> requirements, CommandSource source, String[] inputs, int location) {
        this.requirements = requirements;
        this.source = source;
        this.inputs = inputs;
        this.location = location;
    }

    @Override
    public boolean test(ServerCommandSource serverCommandSource) {
        final Scalar<Boolean> and = new And(
            requirement -> {
                return requirement.control(source, inputs, location);
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
