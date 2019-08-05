package net.tsp.core.command;

import com.google.common.collect.Lists;
import net.tsp.core.Module;
import net.tsp.core.TSPPlugin;
import net.tsp.core.command.commands.CmdFly;
import net.tsp.core.rank.CmdSetRank;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * @author Ellie :: 26/07/2019
 */
public class CommandManager extends Module {

    private List<Command> localCommands;
    private CommandMap nativeCommandMap;

    public CommandManager(TSPPlugin plugin) {
        super (plugin, "Command Manager");
    }

    @Override
    public void start() {

        try {
            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            this.nativeCommandMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logError("FATAL ERROR: Failed to access native command map to Spigot");
            e.printStackTrace();
            return;
        }

        this.localCommands = Lists.newArrayList();

        // Register
        // Administration
        registerCommand(new CmdSetRank(getPlugin()));

        // Cosmetic
        registerCommand(new CmdFly(getPlugin()));

        log("Registered " + localCommands.size() + " API commands.");
    }

    @Override
    public void end() {
        localCommands.clear();
    }

    public void registerCommand(Command... commands) {
        for (Command command : commands) {
            nativeCommandMap.register("  ", command);
            this.localCommands.add(command);
        }
    }

    public void unregisterCommand(Command command) {
        this.localCommands.remove(command);
        nativeCommandMap.getCommand(command.getLabel()).unregister(nativeCommandMap);
    }

    public <T> T getCommand(Class<T> command) {
        for (Command localCommand : this.localCommands) {
            if (localCommand.getClass().equals(command))
                return (T) localCommand;
        }
        return null;
    }

    public List<Command> getLocalCommands() {
        return Collections.unmodifiableList(localCommands);
    }
}
