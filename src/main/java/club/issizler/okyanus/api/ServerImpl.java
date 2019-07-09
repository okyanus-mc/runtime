package club.issizler.okyanus.api;

import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmdnew.ColoredConsoleCommandSender;
import club.issizler.okyanus.api.cmdnew.CommandRegistry;
import club.issizler.okyanus.api.cmdnew.ConsoleCommandSender;
import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.entity.mck.MckPlayer;
import club.issizler.okyanus.api.event.Event;
import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.EventRegistry;
import club.issizler.okyanus.api.world.World;
import club.issizler.okyanus.api.world.WorldImpl;
import club.issizler.okyanus.runtime.utils.accessors.MinecraftServerLoggable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ServerImpl implements Server {

    private final ConsoleCommandSender sender = new ColoredConsoleCommandSender();
    private final MinecraftServer server;
    private final CommandRegistry commandRegistry;
    private final club.issizler.okyanus.api.cmd.CommandRegistry oldCommandRegistry;
    private final EventRegistry eventRegistry;

    public ServerImpl(MinecraftServer server, CommandRegistry commandRegistry, club.issizler.okyanus.api.cmd.CommandRegistry oldCommandRegistry, EventRegistry eventRegistry) {
        this.server = server;
        this.commandRegistry = commandRegistry;
        this.oldCommandRegistry = oldCommandRegistry;
        this.eventRegistry = eventRegistry;
    }

    @NotNull
    @Override
    public String getName() {
        return server.getName();
    }

    @NotNull
    @Override
    public String getVersion() {
        return server.getVersion();
    }

    @NotNull
    @Override
    public Logger getLogger() {
        return ((MinecraftServerLoggable) server).getLogger();
    }

    @NotNull
    @Override
    public Collection<Player> getPlayerList() {
        Set<Player> players = new HashSet<>();

        server.getPlayerManager().getPlayerList().forEach(e ->
            players.add(new PlayerImpl(e))
        );

        return players;
    }

    @NotNull
    @Override
    public List<World> getWorlds() {
        List<World> worlds = new ArrayList<>();
        server.getWorlds().forEach(world -> worlds.add(new WorldImpl(world)));

        return worlds;
    }

    @Override
    public boolean isMainThread() {
        return Thread.currentThread().getName().equals("Server thread");
    }

    @NotNull
    @Override
    public Player getPlayerByName(@NotNull String playerName) {
        ServerPlayerEntity e = server.getPlayerManager().getPlayer(playerName);
        if (e == null)
            return new MckPlayer();

        return new PlayerImpl(e);
    }

    @Override
    public void runCommand(@NotNull String command) {
        server.getCommandManager().execute(server.getCommandSource(), command);
    }

    @NotNull
    @Override
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    @NotNull
    @Override
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    @Override
    public void broadcast(@NotNull String message) {
        server.sendMessage(new LiteralText(message));
        getPlayerList().forEach(player -> player.send(message));
    }

    @NotNull
    @Override
    public ConsoleCommandSender getConsoleSender() {
        return sender;
    }

    // Deprecated Section

    @Override
    public club.issizler.okyanus.api.cmd.CommandRegistry getOldCommandRegistry() {
        return oldCommandRegistry;
    }

    @Override
    public void registerCommand(CommandBuilder cmd) {
        oldCommandRegistry.register(cmd);
    }

    @Override
    public void registerEvent(EventHandler e) {
        eventRegistry.register(e);
    }

    @Override
    public <E extends Event> E triggerEvent(E e) {
        return eventRegistry.trigger(e);
    }

    @Override
    public void exec(String command) {
        server.getCommandManager().execute(server.getCommandSource(), command);
    }

}