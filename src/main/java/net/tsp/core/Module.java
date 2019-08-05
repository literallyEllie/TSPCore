package net.tsp.core;

import net.tsp.core.util.UtilTime;
import org.bukkit.event.Listener;

import java.util.logging.Level;

/**
 * @author Ellie :: 26/07/2019
 */
public abstract class Module implements IStartup, Listener {

    private final TSPPlugin plugin;

    private final String display;
    private final long startupTime;

    public Module(TSPPlugin plugin, String display) {
        this.plugin = plugin;
        this.display = display;
        this.startupTime = UtilTime.now();

        try {
            start();

            plugin.registerModule(this);
        } catch (Throwable e) {
            logWarn("Failed to enable module " + display);
            e.printStackTrace();
        }
    }

    public final TSPPlugin getPlugin() {
        return plugin;
    }

    public final String getDisplay() {
        return display;
    }

    public final long getStartupTime() {
        return startupTime;
    }

    protected final void log(String message) {
        log(Level.INFO, message);
    }

    protected final void logWarn(String message) {
        log(Level.WARNING, message);
    }

    protected final void logError(String message) {
        log(Level.SEVERE, message);
    }

    protected void log(Level level, String message) {
        this.plugin.getLogger().log(level, "[" + this.display + "] " + message);
    }

}
