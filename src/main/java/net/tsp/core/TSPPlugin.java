package net.tsp.core;

import com.google.gson.Gson;
import net.tsp.core.server.settings.ServerConfigKey;
import net.tsp.core.server.settings.ServerSettings;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ellie :: 25/07/2019
 */
public abstract class TSPPlugin extends JavaPlugin {

    private static final String VERSION_API = "0.1-DEV";

    // Thread safe
    public static final Gson gson = new Gson();

    @Override
    public final void onEnable() {
        log("TSP Plugin loading: " + getDescription().getName() + ", version " + getDescription().getVersion());
        log("API version: " + VERSION_API);

        ServerSettings serverSettings = new ServerSettings();
        if (serverSettings.isFirstTime()) {
            throw new RuntimeException("PLEASE FILL IN " + serverSettings.getFile().getName() + " APPROPRIATELY. *PLUGIN WILL NOT FUNCTION*");
        }

        log("SERVER ID: " + serverSettings.getSetting(ServerConfigKey.ID));
        log((serverSettings.getSetting(ServerConfigKey.LOCAL) ? "Local" : "Network") + " server running in " + (serverSettings.getSetting(ServerConfigKey.DEV) ? "DEVELOPMENT" : "REGULAR") + " mode.");
        if (serverSettings.getSetting(ServerConfigKey.LOBBY))
            log("As a lobby.");

        // TODO required rank
        log("The rank " + serverSettings.getSetting(ServerConfigKey.MIN_RANK) + " is required to join.");

        start();
    }

    @Override
    public final void onDisable() {
        log("TSP Plugin now beginning disabling process.");

        end();
    }

    protected abstract void start();

    protected abstract void end();

    public void log(String info) {
        logInfo(info);
    }

    public void logInfo(String info) {
        getLogger().info(info);
    }

    public void logWarn(String warn) {
        getLogger().info(warn);
    }

    public void logError(String error) {
        getLogger().severe(error);
    }


}
