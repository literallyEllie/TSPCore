package net.tsp.core.command.commands;

import net.tsp.core.TSPPlugin;
import net.tsp.core.command.Command;
import net.tsp.core.command.CommandBuilder;
import net.tsp.core.command.CommandEnvironment;
import net.tsp.core.rank.Rank;
import net.tsp.core.server.ServerMessages;
import net.tsp.core.util.UtilPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Ellie :: 26/07/2019
 */
public class CmdFly extends Command {

    public CmdFly(TSPPlugin plugin) {
        super (plugin, new CommandBuilder("fly")
            .setDescription("Fly!")
            .setUsage("[player]")
            .setRequiredRank(Rank.MOD));
    }

    @Override
    public void onCommand(CommandEnvironment commandEnvironment) {

        final CommandSender sender = commandEnvironment.getSender();

        Player target;

        if (commandEnvironment.getArgs().length == 0) {

            if (!isPlayer(sender)) {
                sender.sendMessage(ServerMessages.NEED_PLAYER);
                return;
            }

            target = (Player) sender;
        } else {
            target = UtilPlayer.getPlayer(commandEnvironment.getArgs()[1]);

            if (target == null) {
                sender.sendMessage(ServerMessages.PLAYER_NOT_FOUND);
                return;
            }
        }

        target.setAllowFlight(!target.getAllowFlight());
        target.setFlying(target.getAllowFlight());

        if (target != sender) {
            target.sendMessage("You can " + (target.getAllowFlight() ? "now" : "no longer") + " fly.");
        }

        sender.sendMessage(target.getName() + " can " + (target.getAllowFlight() ? "now" : "no longer") + " fly.");
    }

}
