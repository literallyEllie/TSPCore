package net.tsp.core.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import net.tsp.core.Module;
import net.tsp.core.TSPPlugin;
import net.tsp.core.server.settings.ServerConfigKey;
import org.apache.commons.lang.StringUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Ellie :: 26/07/2019
 */
public class RedisManager extends Module {

    private boolean active;
    private JedisPool pool;

    private Map<String, Collection<RedisChannelSubscriber>> channelSubscribers;

    private Set<String> channels;
    private Set<BukkitTask> channelListeners;

    private String serverId;

    public RedisManager(TSPPlugin plugin) {
        super(plugin, "Redis Manager");
    }

    @Override
    public void start() {
        RedisSettings redisSettings = new RedisSettings();

        if (redisSettings.getFields().isEmpty()) {
            return;
        }
        active = true;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(200);
        poolConfig.setMaxTotal(300);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);

        if (StringUtils.isEmpty(redisSettings.getField("auth"))) {
            this.pool = new JedisPool(poolConfig, (String) redisSettings.getField("host"), Integer.parseInt(redisSettings.getField("port")));
        } else this.pool = new JedisPool(poolConfig, redisSettings.getField("host"),
                Integer.parseInt(redisSettings.getField("port")), 2000, redisSettings.getField("auth"));

        this.channelSubscribers = Maps.newHashMap();
        this.channels = Sets.newHashSet();
        this.channelListeners = Sets.newHashSet();

        this.serverId = getPlugin().getServerSettings().getSetting(ServerConfigKey.ID);

    }

    @Override
    public void end() {
        if (active) {
            channelSubscribers.clear();
            channels.clear();
            pool.close();
            channelListeners.forEach(BukkitTask::cancel);
            channelListeners.clear();
        }
    }

    public JedisPool getPool() {
        return pool;
    }

    public boolean isActive() {
        return active;
    }

    public void sendMessage(String channel, RedisChannelMessage channelMessage) {
        if (!active) return;
        channelMessage.setFrom(serverId);
        try (Jedis jedis = pool.getResource()) {
            jedis.publish(channel, TSPPlugin.gson.toJson(channelMessage));
        }
    }

    public void registerChannelSubscriber(String channel, RedisChannelSubscriber subscriber) {
        if (!active) return;
        if (subscriber == null) return;

        if (channelSubscribers.containsKey(channel)) {
            channelSubscribers.get(channel).add(subscriber);
        } else channelSubscribers.put(channel, Lists.newArrayList(subscriber));

        registerChannel(channel);
    }

    private void registerChannel(String channel) {
        if (!active) return;
        if (channels.contains(channel)) return;

        channelListeners.add(getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Thread.currentThread().setName("redis-channel-watcher-" + channel);
            try (Jedis jedis = pool.getResource()) {

                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if (!channelSubscribers.containsKey(channel)) return;

                        RedisChannelMessage redisChannelMessage = TSPPlugin.gson.fromJson(message, new TypeToken<RedisChannelMessage>() {
                        }.getType());

                        if (redisChannelMessage.getFrom().equals(serverId) ||
                                (!redisChannelMessage.getTo().equalsIgnoreCase(serverId) && !redisChannelMessage.getTo().equalsIgnoreCase(RedisGroup.ALL)
                                        && !redisChannelMessage.getTo().equals(RedisGroup.SPIGOT))) return;

                        channelSubscribers.get(channel).forEach(subscriber -> {

                            try {
                                subscriber.onChannelMessageEvent(redisChannelMessage);
                            } catch (Throwable e) {
                                logError("Subscriber failed to handle redis message event.");
                                e.printStackTrace();
                            }

                        });

                    }
                }, channel);
            }
        }));


    }

}
