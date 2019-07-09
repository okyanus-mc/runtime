package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.chat.ChatColor;
import org.jetbrains.annotations.NotNull;

public class CraftConsoleCommandSender extends ServerCommandSender {

    @Override
    public void sendMessage(@NotNull String message) {
        System.out.println(ChatColor.stripColor(message));
    }

    @Override
    public void sendMessage(@NotNull String... messages) {

    }

    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return null;
    }
}
