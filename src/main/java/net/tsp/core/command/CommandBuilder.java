package net.tsp.core.command;

import com.google.common.collect.Lists;
import net.tsp.core.rank.Rank;

import java.util.List;

/**
 * @author Ellie :: 26/07/2019
 */
public class CommandBuilder {

    private String label;
    private String description;
    private String[] usage;
    private List<String> aliases;

    private Rank requiredRank;
    private boolean requirePlayer;

    public CommandBuilder(String label) {
        this.label = label;
        this.aliases = Lists.newArrayList();
        this.usage = new String[]{};
        this.requiredRank = Rank.PLAYER;
    }

    public CommandData build() {
        return new CommandData(label, description, usage, aliases, requiredRank, requirePlayer);
    }

    // Setters

    public CommandBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public CommandBuilder setDescription(String description) {
        if (!description.endsWith(".") && !description.endsWith("!") && !description.endsWith("?")) description += ".";
        this.description = description;

        return this;
    }

    public CommandBuilder setUsage(String... usage) {
        this.usage = usage;
        return this;
    }

    public CommandBuilder setAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    public CommandBuilder setRequiredRank(Rank requiredRank) {
        this.requiredRank = requiredRank;
        return this;
    }

    public CommandBuilder setRequirePlayer(boolean requirePlayer) {
        this.requirePlayer = requirePlayer;
        return this;
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

    public boolean isRequirePlayer() {
        return requirePlayer;
    }


}
