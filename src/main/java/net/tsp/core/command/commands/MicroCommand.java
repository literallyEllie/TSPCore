package net.tsp.core.command.commands;

import com.google.common.collect.Lists;
import net.tsp.core.TSPPlugin;
import net.tsp.core.command.Command;
import net.tsp.core.command.CommandBuilder;
import net.tsp.core.command.CommandEnvironment;

import java.util.List;

/**
 * @author Ellie :: 26/07/2019
 */
public class MicroCommand extends Command {

    private String response;

    public MicroCommand(TSPPlugin plugin, String label, List<String> aliases, String response) {
        super (plugin, new CommandBuilder(label)
                .setDescription("Micro-command to show " + label)
                .setAliases(aliases));

        this.response = response;
    }

    public MicroCommand(TSPPlugin plugin, String label, String response) {
        this(plugin, label, Lists.newArrayList(), response);
    }

    @Override
    public void onCommand(CommandEnvironment commandEnvironment) {
        commandEnvironment.getSender().sendMessage(prefix(commandEnvironment) + response);
    }

    public void special() {

    }

}
