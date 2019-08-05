package net.tsp.core.account;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import net.tsp.core.TSPPlugin;
import net.tsp.core.rank.Rank;
import net.tsp.core.server.settings.ServerConfigKey;
import net.tsp.core.util.UtilPlayer;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * @author Ellie :: 26/07/2019
 */
public class Account {

    public static final String ID_UUID = "uuid";
    public static final String ID_NAME = "name";
    public static final String ID_FIRST_SEEN = "first_seen";
    public static final String ID_LAST_SEEN = "last_seen";
    public static final String ID_PREV_NAMES = "previous_names";
    public static final String ID_RECORD_IPS = "record_ips";
    public static final String ID_LAST_SERVER = "last_server";
    public static final String ID_RANK = "rank";

    private TSPPlugin plugin;

    // Core info
    private UUID uuid;
    private String name;
    private Player base;

    // Meta
    private long firstSeen, lastSeen;
    private List<String> previousNames;
    private List<String> recordIPs;
    private boolean firstTime;

    // Network attributes
    private String lastSeenServer;
    private Rank rank;

    public Account(TSPPlugin plugin, Player player, boolean firstTime) {
        this.plugin = plugin;
        this.base = player;
        this.firstTime = firstTime;

        if (base != null) {
            this.uuid = player.getUniqueId();
            this.name = player.getName();
        }

        this.firstSeen = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
        this.previousNames = Lists.newArrayList();
        this.recordIPs = Lists.newArrayList();
        this.lastSeenServer = plugin.getServerSettings().getSetting(ServerConfigKey.ID);
        this.rank = Rank.PLAYER;
    }

    public Account(TSPPlugin plugin, Player player) {
        this(plugin, player, false);
    }

    public Account(TSPPlugin plugin) {
        this (plugin, (Player) null);
    }

    public Account(TSPPlugin plugin, Document document) {
        this(plugin, (Player) null);

        this.uuid = UUID.fromString(document.getString(ID_UUID));
        this.name = document.getString(ID_NAME);
        this.firstSeen = document.getLong(ID_FIRST_SEEN);
        this.lastSeen = document.getLong(ID_LAST_SEEN);
        this.previousNames = (List<String>) document.get(ID_PREV_NAMES);
        this.recordIPs = (List<String>) document.get(ID_RECORD_IPS);
        this.lastSeenServer = document.getString(ID_LAST_SERVER);
        this.rank = Rank.valueOf(document.getString(ID_RANK));
    }

    public void sendMessage(String message) {
        if (base != null)
            base.sendMessage(message);
    }

    public TSPPlugin getPlugin() {
        return plugin;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_UUID, uuid.toString());
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_NAME, name);
    }

    public Player getBase() {
        if (base == null && UtilPlayer.getPlayer(uuid) != null)
            return (base = UtilPlayer.getPlayer(uuid));
        return base;
    }

    public long getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(long firstSeen) {
        this.firstSeen = firstSeen;
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_FIRST_SEEN, firstSeen);
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_LAST_SEEN, lastSeen);
    }

    public List<String> getPreviousNames() {
        return previousNames;
    }

    public void setPreviousNames(List<String> previousNames) {
        this.previousNames = previousNames;
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_PREV_NAMES, previousNames);
    }

    public void addPreviousName(String name) {
        this.previousNames.add(name);
        setPreviousNames(previousNames);
    }

    public List<String> getRecordIPs() {
        return recordIPs;
    }

    public void setRecordIPs(List<String> recordIPs) {
        this.recordIPs = recordIPs;
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_RECORD_IPS, recordIPs);
    }

    public void addRecordIP(String ip) {
        this.recordIPs.add(ip);
        setRecordIPs(recordIPs);
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastSeenServer() {
        return lastSeenServer;
    }

    public void setLastSeenServer(String lastSeenServer) {
        this.lastSeenServer = lastSeenServer;
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_LAST_SERVER, lastSeenServer);
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        plugin.getAccountManager().updateAccountMongo(this.uuid, ID_RANK, rank.toString());
    }

    public Document toDocument() {
        return new Document(ID_UUID, uuid.toString())
                .append(ID_NAME, name)
                .append(ID_FIRST_SEEN, firstSeen)
                .append(ID_LAST_SEEN, lastSeen)
                .append(ID_PREV_NAMES, previousNames)
                .append(ID_RECORD_IPS, recordIPs)
                .append(ID_RANK, rank.toString())
                .append(ID_LAST_SERVER, lastSeenServer);
    }

    void sync(String key, String value) {
        switch (key) {
            case ID_NAME:
                this.name = TSPPlugin.gson.fromJson(value, new TypeToken<String>(){}.getType());
                break;
            case ID_FIRST_SEEN:
                this.firstSeen = TSPPlugin.gson.fromJson(value, new TypeToken<Long>(){}.getType());
                break;
            case ID_LAST_SEEN:
                this.lastSeen = TSPPlugin.gson.fromJson(value, new TypeToken<Long>(){}.getType());
                break;
            case ID_PREV_NAMES:
                this.previousNames = TSPPlugin.gson.fromJson(value, new TypeToken<List<String>>(){}.getType());
                break;
            case ID_RECORD_IPS:
                this.recordIPs = TSPPlugin.gson.fromJson(value, new TypeToken<List<String>>(){}.getType());
                break;
            case ID_RANK:
                this.rank = Rank.valueOf(TSPPlugin.gson.fromJson(value, new TypeToken<String>(){}.getType()));
                break;
            case ID_LAST_SERVER:
                this.lastSeenServer = TSPPlugin.gson.fromJson(value, new TypeToken<String>(){}.getType());
                break;
        }
    }

}
