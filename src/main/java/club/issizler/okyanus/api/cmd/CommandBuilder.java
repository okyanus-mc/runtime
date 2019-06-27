package club.issizler.okyanus.api.cmd;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

// We'll definitely need to add more stuff as time goes
public class CommandBuilder {

    private String name;
    private List<Triple<String, ArgumentType, Boolean>> args = new ArrayList<>();

    private CommandRunnable runnable;

    private boolean isOpOnly = false;

    public CommandBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CommandBuilder opOnly() {
        this.isOpOnly = true;
        return this;
    }

    public CommandBuilder run(CommandRunnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public CommandBuilder arg(String name, ArgumentType type) {
        return arg(name, type, false);
    }

    public CommandBuilder arg(String name, ArgumentType type, boolean isOptional) {
        this.args.add(new ImmutableTriple<>(name, type, isOptional));
        return this;
    }

    public String __internal_name() {
        return this.name;
    }

    public boolean __internal_isOpOnly() {
        return this.isOpOnly;
    }

    public CommandRunnable __internal_runnable() {
        return this.runnable;
    }

    public List<Triple<String, ArgumentType, Boolean>> __internal_args() {
        return this.args;
    }

}
