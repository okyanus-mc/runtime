package club.issizler.okyanus.api;

import club.issizler.okyanus.api.entity.EntityImpl;
import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.entity.PlayerImpl;
import club.issizler.okyanus.api.registry.CommandRegistryImpl;
import club.issizler.okyanus.api.registry.EventRegistryImpl;
import club.issizler.okyanus.api.world.World;
import club.issizler.okyanus.api.world.WorldImpl;
import club.issizler.okyanus.runtime.utils.accessors.MinecraftServerLoggable;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ServerImpl implements Server {

    private final CommandRegistry commandRegistry = new CommandRegistryImpl();
    private final EventRegistry eventRegistry = new EventRegistryImpl();

    private final MinecraftServer server;

    public ServerImpl(MinecraftServer server) {
        this.server = server;
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
            players.add(new PlayerImpl(
                e,
                new EntityImpl(e)
            ))
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

    public MinecraftServer getInternal() {
        return server;
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
    public void runCommand(String command) {

    }

    @Override
    public void sendMessage(String text) {

    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void addPermission(String perm) {

    }

    @Override
    public boolean removePermission(String perm) {
        return false;
    }

    @Override
    public void setOp(boolean op) {

    }

    @Override
    public boolean isOp() {
        return false;
    }

}
