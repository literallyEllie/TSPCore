package net.tsp.core.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Ellie :: 26/07/2019
 */
public class UtilPlayer {

    public static Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public static Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

}
