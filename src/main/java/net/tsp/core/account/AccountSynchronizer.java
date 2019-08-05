package net.tsp.core.account;

import net.tsp.core.redis.RedisChannelMessage;
import net.tsp.core.redis.RedisChannelSubscriber;
import net.tsp.core.redis.RedisGroup;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Ellie :: 26/07/2019
 */
public class AccountSynchronizer implements RedisChannelSubscriber {

    private static final String CHANNEL_SYNC = "SYNC";

    private AccountManager accountManager;

    public AccountSynchronizer(AccountManager accountManager) {
        this.accountManager = accountManager;

        this.accountManager.getPlugin().getRedisManager().registerChannelSubscriber(CHANNEL_SYNC, this);
    }

    public void sendSync(UUID uuid, String key, String value) {
        if (!accountManager.getPlugin().getRedisManager().isActive()) return;

        accountManager.getPlugin().getRedisManager().sendMessage(CHANNEL_SYNC, new RedisChannelMessage()
                .setTo(RedisGroup.SPIGOT)
                .setSubject("PLAYER")
                .setArgs(Arrays.asList(uuid.toString(), key, value)));
    }

    @Override
    public void onChannelMessageEvent(RedisChannelMessage message) {
        if (!message.getSubject().equals("PLAYER")) return;

        UUID uuid = UUID.fromString(message.getArgs().get(0));
        String key = message.getArgs().get(1);
        String value = message.getArgs().get(2);

        Account account = null;

        if (accountManager.getCachedAccounts().asMap().containsKey(uuid)) {
            account = accountManager.getCachedAccounts().asMap().get(uuid);
        } else if (accountManager.getAccountMap().containsKey(uuid)) {
            accountManager.getAccountMap().get(uuid);
        }

        if (account != null)
            account.sync(key, value);
    }


}
