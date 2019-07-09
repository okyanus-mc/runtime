package club.issizler.okyanus.api.cmdnew;

import club.issizler.okyanus.api.chat.ChatColor;
import jline.Terminal;
import jline.console.ConsoleReader;
import org.apache.logging.log4j.LogManager;
import org.cactoos.scalar.And;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class ColoredConsoleCommandSender extends ConsoleCommandSenderImpl {

    private final Map<ChatColor, String> replacements = new EnumMap<>(ChatColor.class);
    private final ChatColor[] colors = ChatColor.values();

    private final Terminal terminal;

    public ColoredConsoleCommandSender() {
        ConsoleReader reader = null;
        try {
            reader = new ConsoleReader(System.in, System.out);
            reader.setExpandEvents(false);
        } catch (Exception e) {
            System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
            System.setProperty("user.language", "en");
            try {
                reader = new ConsoleReader(System.in, System.out);
                reader.setExpandEvents(false);
            } catch (IOException ex) {
                LogManager.getLogger().warn("", ex);
            }
        }
        assert reader != null;
        terminal = reader.getTerminal();

        replacements.put(ChatColor.BLACK, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        replacements.put(ChatColor.GOLD, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        replacements.put(ChatColor.GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(ChatColor.BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(ChatColor.GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(ChatColor.AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(ChatColor.RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(ChatColor.WHITE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
        replacements.put(ChatColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
        replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
        replacements.put(ChatColor.RESET, Ansi.ansi().a(Attribute.RESET).toString());
    }

    @Override
    public void sendMessage(@NotNull final String... messages) {
        try {
            new And(
                message -> {
                    if (terminal.isAnsiSupported()) {
                        String result = message;
                        for (ChatColor color : colors) {
                            if (replacements.containsKey(color)) {
                                result = result.replaceAll("(?i)" + color.toString(), replacements.get(color));
                            } else {
                                result = result.replaceAll("(?i)" + color.toString(), "");
                            }
                        }
                        System.out.println(result + Ansi.ansi().reset().toString());
                    } else {
                        super.sendMessage(message);
                    }
                },
                messages
            ).value();
        } catch (Exception e) {
            // Ignore...
        }
    }

}