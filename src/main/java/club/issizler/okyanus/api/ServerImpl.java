package club.issizler.okyanus.api;

import club.issizler.okyanus.api.entity.Player;
import club.issizler.okyanus.api.registry.*;
import club.issizler.okyanus.api.world.World;
import club.issizler.okyanus.api.world.WorldImpl;
import club.issizler.okyanus.runtime.utils.accessors.MinecraftServerLoggable;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServerImpl implements Server {

    private final CommandRegistry commandRegistry = new CommandRegistryImpl();
    private final EventRegistry eventRegistry = new EventRegistryImpl();
    private final PlayerRegistry playerRegistry = new PlayerRegistryImpl();

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
        return playerRegistry.getPlayerList();
    }

    @Override
    public Player getPlayer(String name) {
        return playerRegistry.getPlayer(name);
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
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    @Override
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    @Override
    public PlayerRegistry getPlayerRegistry() {
        return playerRegistry;
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
