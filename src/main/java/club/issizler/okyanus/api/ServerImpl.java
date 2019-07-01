package club.issizler.okyanus.api;

import club.issizler.okyanus.api.cmd.CommandBuilder;
import club.issizler.okyanus.api.cmd.CommandManager;
import club.issizler.okyanus.api.cmd.CommandManagerImpl;
import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.event.Event;
import club.issizler.okyanus.api.event.EventHandler;
import club.issizler.okyanus.api.event.EventManager;
import club.issizler.okyanus.api.world.World;
import club.issizler.okyanus.api.world.WorldImpl;
import club.issizler.okyanus.runtime.utils.accessors.MinecraftServerLoggable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ServerImpl implements Server {

    private final MinecraftServer server;

    private final CommandManager commandManager;
    private final EventManager eventManager;

    public ServerImpl(MinecraftServer server, CommandManager commandManager, EventManager eventManager) {
        this.server = server;

        this.commandManager = commandManager;
        this.eventManager = eventManager;
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
    public void registerCommand(CommandBuilder cmd) {
        commandManager.register(cmd);
    }

    @Override
    public void registerEvent(EventHandler e) {
        eventManager.register(e);
    }

    @Override
    public <E extends Event> E triggerEvent(E e) {
        return eventManager.trigger(e);
    }

    @Override
    public Optional<Player> getPlayerByName(String playerName) {
        ServerPlayerEntity e = server.getPlayerManager().getPlayer(playerName);
        if (e == null)
            return Optional.empty();

        return Optional.of(new PlayerImpl(e));
    }

    public CommandManagerImpl getCommandManager() {
        return (CommandManagerImpl) commandManager;
    }
}
