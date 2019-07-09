package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.chat.ChatColor;
import org.cactoos.scalar.And;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommandSenderImpl extends ServerCommandSender implements ConsoleCommandSender {

    @Override
    public void sendMessage(@NotNull final String... messages) {
        try {
            new And(
                input -> {
                    System.out.println(ChatColor.stripColor(input));
                },
                messages
            ).value();
        } catch (Exception e) {
            // Ignore...
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean op) {
        throw new UnsupportedOperationException("Cannot change operator status of server console");
    }

}
