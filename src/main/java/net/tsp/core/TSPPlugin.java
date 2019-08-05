package net.tsp.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.tsp.core.account.AccountManager;
import net.tsp.core.command.CommandManager;
import net.tsp.core.mongo.MongoManager;
import net.tsp.core.redis.RedisManager;
import net.tsp.core.server.settings.ServerConfigKey;
import net.tsp.core.server.settings.ServerSettings;
import net.tsp.core.util.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Ellie :: 25/07/2019
 */
public abstract class TSPPlugin extends JavaPlugin implements IStartup {

    private static final String VERSION_API = "0.1-DEV";

    // Thread safe
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private long startLoadTime, finishLoadTime;

    private ServerSettings serverSettings;

    private Deque<Module> modules;

    private MongoManager mongoManager;
    private RedisManager redisManager;

    private AccountManager accountManager;
    private CommandManager commandManager;

    @Override
    public final void onEnable() {
        this.startLoadTime = UtilTime.now();
        /* Spam start */
        log("TSP Plugin loading: " + getDescription().getName() + ", version " + getDescription().getVersion());
        log("API version: " + VERSION_API);

        this.serverSettings = new ServerSettings();
        if (serverSettings.isFirstTime()) {
            throw new RuntimeException("PLEASE FILL IN " + serverSettings.getFile().getName() + " APPROPRIATELY. *PLUGIN WILL NOT FUNCTION*");
        }

        log("SERVER ID: " + serverSettings.getSetting(ServerConfigKey.ID));
        log((serverSettings.getSetting(ServerConfigKey.LOCAL) ? "Local" : "Network") + " server running in " + (serverSettings.getSetting(ServerConfigKey.DEV) ? "DEVELOPMENT" : "REGULAR") + " mode.");
        if (serverSettings.getSetting(ServerConfigKey.LOBBY))
            log("As a lobby.");

        // TODO required rank
        log("The rank " + serverSettings.getSetting(ServerConfigKey.MIN_RANK) + " is required to join.");
        /* Spam end */

        /* Start module init */
        this.modules = new ArrayDeque<>();

        // Backend Init
        this.mongoManager = new MongoManager(this);
        this.redisManager = new RedisManager(this);

        // Important
        this.accountManager = new AccountManager(this);
        this.commandManager = new CommandManager(this);

        // 4fun

        /* Module init end. */

        // Impl IStartup
        start();

        finishLoadTime = UtilTime.now();

        log("Startup complete. Took " + (finishLoadTime - startLoadTime) + "ms.");

    }

    @Override
    public final void onDisable() {
        log("TSP Plugin now beginning disabling process.");

        // Impl IStartup
        end();

        // Disable all modules.
        modules.forEach(Module::end);
        modules.clear();
    }

    public void registerModule(Module module) {
        modules.add(module);
        registerListener(module);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void log(String info) {
        getLogger().info(info);
    }

    public void logWarn(String warn) {
        getLogger().info(warn);
    }

    public void logError(String error) {
        getLogger().severe(error);
    }

    public long getStartLoadTime() {
        return startLoadTime;
    }

    public long getFinishLoadTime() {
        return finishLoadTime;
    }

    public ServerSettings getServerSettings() {
        return serverSettings;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

}
