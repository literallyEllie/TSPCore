package net.tsp.core.redis;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Ellie :: 26/07/2019
 */
public class RedisChannelMessage {

    private String to, from, subject;
    private List<String> args;

    public RedisChannelMessage() {

    }

    public RedisChannelMessage(String to, String from, String subject, Object... args) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.args = Lists.newLinkedList();
        for (Object arg : args)
            this.args.add(String.valueOf(arg));
    }

    public RedisChannelMessage setTo(String to) {
        this.to = to;
        return this;
    }

    public RedisChannelMessage setFrom(String from) {
        this.from = from;
        return this;
    }

    public RedisChannelMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public RedisChannelMessage setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    // Getters

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getArgs() {
        return args;
    }
}
