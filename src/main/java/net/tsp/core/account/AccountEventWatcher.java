package net.tsp.core.account;

import net.tsp.core.server.settings.ServerConfigKey;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Ellie :: 26/07/2019
 */
public class AccountEventWatcher implements Listener {

    private AccountManager accountManager;

    public AccountEventWatcher(AccountManager accountManager) {
        this.accountManager = accountManager;

        this.accountManager.getPlugin().registerListener(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(AsyncPlayerPreLoginEvent e) {
        if (accountManager.getPlugin().getServerSettings().getField(ServerConfigKey.LOCAL.getKey())) return;

    }

    @EventHandler(priority = EventPriority.LOW)
    public void on(PlayerJoinEvent e) {

        Account account = accountManager.loadOnlineAccount(e.getPlayer());

        account.sendMessage("Hi " + account.getRank() + " " + account.getName());

    }

    @EventHandler
    public void on(PlayerQuitEvent e) {

        Account account = accountManager.getAccount(e.getPlayer());

        Bukkit.broadcastMessage(account.getName() + " left us!");

        accountManager.unloadAccount(account);

    }

}
