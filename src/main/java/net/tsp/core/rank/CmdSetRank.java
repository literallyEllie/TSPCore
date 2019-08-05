package net.tsp.core.rank;

import net.tsp.core.TSPPlugin;
import net.tsp.core.account.Account;
import net.tsp.core.command.Command;
import net.tsp.core.command.CommandBuilder;
import net.tsp.core.command.CommandEnvironment;
import net.tsp.core.server.ServerMessages;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * @author Ellie :: 26/07/2019
 */
public class CmdSetRank extends Command {

    public CmdSetRank(TSPPlugin plugin) {
        super(plugin, new CommandBuilder("setrank")
                        .setDescription("Set the rank of a player")
                        .setUsage("<rank>", "[player]")
                        .setRequiredRank(Rank.ADMIN));
    }

    @Override
    public void onCommand(CommandEnvironment commandEnvironment) {
        final String[] args = commandEnvironment.getArgs();
        final CommandSender sender = commandEnvironment.getSender();

        Optional<Rank> rank = Rank.fromString(args[0]);

        if (!rank.isPresent()) {
            sender.sendMessage(prefix(commandEnvironment.getUsedLabel()) + "Rank not found.");
            return;
        }

        Account target = null;

        if (isPlayer(sender)) {
            target = commandEnvironment.getAccount();
            if (rank.get().isHigherOrEqualTo(target.getRank())) {
                sender.sendMessage(prefix(commandEnvironment.getUsedLabel()) + "You cannot set a rank equal or higher than your own.");
                return;
            }
        } else if (args.length > 1) target = plugin.getAccountManager().getAccount(args[1]);
        else {
            usage(sender);
            return;
        }

        if (target == null) {
            sender.sendMessage(prefix(commandEnvironment.getUsedLabel()) + ServerMessages.PLAYER_NOT_FOUND);
            return;
        }

        target.setRank(rank.get());

        if (!sender.getName().equals(target.getName())) {
            sender.sendMessage("Set rank of " + target.getName() + " to " + rank.get().getDisplay());
        }

        target.sendMessage("Your rank is now " + rank.get().getChatColor() + rank.get().getDisplay());

    }

}
