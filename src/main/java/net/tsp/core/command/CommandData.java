package net.tsp.core.command;

import com.google.common.base.Joiner;
import net.tsp.core.rank.Rank;
import net.tsp.core.server.ServerMessages;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ellie :: 26/07/2019
 */
public class CommandData {

    private String label;
    private String description;
    private String[] usage;
    private List<String> aliases;

    private Rank requiredRank;
    private boolean requirePlayer;

    private String correctUsageMessage;
    private int requiredArgs;

    public CommandData(String label, String description, String[] usage, List<String> aliases, Rank requiredRank, boolean requirePlayer) {
        this.label = label;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.requiredRank = requiredRank;
        this.requirePlayer = requirePlayer;

        // Do it now for efficiency later.
        this.correctUsageMessage = String.format(ServerMessages.CORRECT_USAGE, label, Joiner.on(" ").join(usage), description);
        this.requiredArgs = (int) Arrays.stream(usage).filter(s -> s.contains("<")).count();
    }

    public CommandData(String label, String description, String[] usage, List<String> aliases) {
        this (label, description, usage, aliases, Rank.PLAYER, false);
    }

    public boolean isEnoughArgs(String[] inputArgs) {
        return inputArgs.length >= requiredArgs;
    }

    // Getters

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String[] getUsage() {
        return usage;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Rank getRequiredRank() {
        return requiredRank;
    }

    public boolean isRequirePlayer() {
        return requirePlayer;
    }

    public String getCorrectUsageMessage() {
        return correctUsageMessage;
    }

    public int getRequiredArgs() {
        return requiredArgs;
    }


}
