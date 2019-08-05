package net.tsp.core.redis;

/**
 * @author Ellie :: 26/07/2019
 */
public interface RedisChannelSubscriber {

    void onChannelMessageEvent(RedisChannelMessage message);

}
