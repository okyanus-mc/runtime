package club.issizler.okyanus.api.cmd;

/**
 * Your should either
 *  - Implement this in a class
 *  - Create an anonymous class implementing this
 *
 * And pass it to {@link CommandBuilder#run(CommandRunnable)} to make your
 * command actually do something
 */
@FunctionalInterface
public interface CommandRunnable {

    int run(CommandSource source);

}
