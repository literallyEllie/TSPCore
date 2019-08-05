package net.tsp.core.command;

import net.tsp.core.TSPPlugin;
import net.tsp.core.account.Account;
import net.tsp.core.server.ServerMessages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author Ellie :: 26/07/2019
 */
public abstract class Command extends org.bukkit.command.Command implements CommandExecutor {

    // Ease of access.
    protected final TSPPlugin plugin;

    private CommandData commandData;

    private CommandExecutor commandExecutor;

    public Command(TSPPlugin plugin, CommandData commandData) {
        super(commandData.getLabel());
        this.plugin = plugin;
        this.commandData = commandData;

        // Native methods
        setAliases(commandData.getAliases());
        setPermissionMessage(ServerMessages.NO_PERMISSION);

        this.commandExecutor = this;
    }

    public Command(TSPPlugin plugin, CommandBuilder commandBuilder) {
        this(plugin, commandBuilder.build());
    }

    public abstract void onCommand(CommandEnvironment commandEnvironment);

    @Override
    public final boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (commandExecutor != null)
            return commandExecutor.onCommand(sender, this, commandLabel, args);

        sender.sendMessage(ServerMessages.NO_EXECUTOR);
        return false;
    }

    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        Account account = null;

        if (isPlayer(sender)) {
            account = plugin.getAccountManager().getAccount(((Player)sender).getUniqueId());

            if (!account.getRank().isHigherOrEqualTo(commandData.getRequiredRank())) {
                sender.sendMessage(prefix(label) + ServerMessages.NO_PERMISSION);
                return true;
            }

        } else if (commandData.isRequirePlayer()) {
            sender.sendMessage(ServerMessages.NEED_PLAYER);
            return true;
        }

        if (!commandData.isEnoughArgs(args)) {
            sender.sendMessage(prefix(label) + commandData.getCorrectUsageMessage());
            return true;
        }


        onCommand(new CommandEnvironment(sender, label, args, account));

        return true;
    }

    public boolean isPlayer(CommandSender commandSender) {
        return !(commandSender instanceof ConsoleCommandSender);
    }

    public String prefix(String label) {
        return StringUtils.capitalize(label) + " :: ";
    }

    public String prefix(CommandEnvironment commandEnvironment) {
        return this.prefix(commandEnvironment.getUsedLabel());
    }

    public void usage(CommandSender commandSender) {
        commandSender.sendMessage(prefix(getLabel()) + commandData.getCorrectUsageMessage());
    }

    protected void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

}
