package club.issizler.okyanus.api;

import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmdnew.CommandRegistry;
import club.issizler.okyanus.api.cmdnew.CommandSender;
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

import java.util.*;

public class ServerImpl implements Server {

    private final CommandSender sender;
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

    @Override
    public String getName() {
        return server.getName();
    }

    @Override
    public String getVersion() {
        return server.getVersion();
    }

    @Override
    public Logger getLogger() {
        return ((MinecraftServerLoggable) server).getLogger();
    }

    @Override
    public Collection<Player> getPlayerList() {
        Set<Player> players = new HashSet<>();

        server.getPlayerManager().getPlayerList().forEach(e ->
            players.add(new PlayerImpl(e))
        );

        return players;
    }

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

    @Override
    public Player getPlayerByName(String playerName) {
        ServerPlayerEntity e = server.getPlayerManager().getPlayer(playerName);
        if (e == null)
            return new MckPlayer();

        return new PlayerImpl(e);
    }

    @Override
    public void runCommand(String command) {
        server.getCommandManager().execute(server.getCommandSource(), command);
    }

    @Override
    public club.issizler.okyanus.api.cmd.CommandRegistry getOldCommandRegistry() {
        return oldCommandRegistry;
    }

    @Override
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    @Override
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    @Override
    public void broadcast(String message) {
        server.sendMessage(new LiteralText(message));
        getPlayerList().forEach(player -> player.send(message));
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        return server.;
    }

    // Deprecated Section

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