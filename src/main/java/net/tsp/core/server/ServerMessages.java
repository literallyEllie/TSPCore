package net.tsp.core.server;

import org.bukkit.ChatColor;

/**
 * @author Ellie :: 26/07/2019
 */
public class ServerMessages {

    // Command responses
    // Help
    public static final String CORRECT_USAGE = "Correct usage: /%s %s - %s";
    // Error
    public static final String NO_PERMISSION = ChatColor.RED + "No permission.";
    public static final String PLAYER_NOT_FOUND = "Player was not found.";
    public static final String NEED_PLAYER = "This command can only be ran by a player.";

    // Unexpected error
    public static final String NO_EXECUTOR = "Command executor was not found for this command, please contact a member of staff.";

}
