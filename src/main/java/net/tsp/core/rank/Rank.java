package net.tsp.core.rank;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Ellie :: 26/07/2019
 */
public enum Rank {

    PLAYER("Player", ChatColor.GRAY),
    MOD("Moderator", ChatColor.BLUE),
    ADMIN("Admin", ChatColor.RED)

    ;

    private String display;
    private ChatColor chatColor;

    Rank(String display, ChatColor chatColor) {
        this.display = display;
        this.chatColor = chatColor;
    }

    public String getDisplay() {
        return display;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public boolean isDefault() {
        return this == PLAYER;
    }

    public boolean isHigherOrEqualTo(Rank rank) {
        return this.ordinal() >= rank.ordinal();
    }

    public static Optional<Rank> fromString(String input) {
        return Arrays.stream(Rank.values()).filter(rank -> rank.getDisplay().equalsIgnoreCase(input) || rank.name().equalsIgnoreCase(input)).findAny();
    }

}
