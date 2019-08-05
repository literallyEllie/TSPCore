package net.tsp.core.command;

import net.tsp.core.account.Account;
import org.bukkit.command.CommandSender;

/**
 * @author Ellie :: 26/07/2019
 */
public class CommandEnvironment {

    private final CommandSender sender;
    private final String usedLabel;
    private final String[] args;

    private Account account;

    public CommandEnvironment(CommandSender sender, String usedLabel, String[] args, Account account) {
        this.sender = sender;
        this.usedLabel = usedLabel;
        this.args = args;
        this.account = account;
    }

    public CommandSender getSender() {
        return sender;
    }

    public String getUsedLabel() {
        return usedLabel;
    }

    public String[] getArgs() {
        return args;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
