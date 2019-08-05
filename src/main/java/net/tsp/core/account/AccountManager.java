package net.tsp.core.account;

import com.google.common.cache.*;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import net.tsp.core.Module;
import net.tsp.core.TSPPlugin;
import net.tsp.core.mongo.MongoCollectionId;
import net.tsp.core.util.D;
import net.tsp.core.util.UtilPlayer;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ellie :: 26/07/2019
 */
public class AccountManager extends Module {

    private Map<UUID, Account> onlineAccounts;

    private LoadingCache<UUID, Account> cachedAccounts;

    private AccountEventWatcher accountEventWatcher;
    private AccountSynchronizer accountSynchronizer;
    private UUIDNameLookup uuidNameLookup;

    public AccountManager(TSPPlugin plugin) {
        super(plugin, "Account Manager");
    }

    @Override
    public void start() {
        this.onlineAccounts = Maps.newConcurrentMap();

        // Cache for not-online players on this server.
        this.cachedAccounts = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<UUID, Account>() {
                    @Override
                    public Account load(UUID key) throws Exception {
                        return getAccount(key);
                    }
                });

        this.accountEventWatcher = new AccountEventWatcher(this);
        this.accountSynchronizer = new AccountSynchronizer(this);
        this.uuidNameLookup = new UUIDNameLookup(this);
    }

    @Override
    public void end() {

        this.onlineAccounts.clear();
        this.cachedAccounts.invalidateAll();
    }

    public Account getAccount(Player player) {
        return onlineAccounts.get(player.getUniqueId());
    }

    public Account getAccount(UUID uuid) {
        if (onlineAccounts.containsKey(uuid))
            return onlineAccounts.get(uuid);

        if (cachedAccounts.asMap().containsKey(uuid))
            return cachedAccounts.getIfPresent(uuid);

        // Go backend.
        Account account = getAccountMongo(uuid);

        if (account == null) {
            account = new Account(getPlugin(), UtilPlayer.getPlayer(uuid), true);
            insertAccountMongo(account);
        }

        if (account.getBase() != null) {

            uuidNameLookup.updateCache(uuid, account.getName(), account.getBase().getName());

            if (!account.getName().equals(account.getBase().getName())) {
                account.addPreviousName(account.getName());
                account.setName(account.getBase().getName());
            }

            onlineAccounts.put(uuid, account);
        } else {
            cachedAccounts.put(uuid, account);
        }

        return account;
    }

    public Account getAccount(String name) {
        UUID uuid = uuidNameLookup.getUUID(name);

        if (uuid == null) {
            D.d("no uuid for " + name);
            return null;
        }

        return getAccount(uuid);
    }

    Account loadOnlineAccount(Player player) {
        if (onlineAccounts.containsKey(player.getUniqueId()))
            return onlineAccounts.get(player.getUniqueId());

        // Remove player from cache
        cachedAccounts.invalidate(player.getUniqueId());

        return getAccount(player.getUniqueId());
    }

    void unloadAccount(Account account) {
        onlineAccounts.remove(account.getUuid());
    }

    // MONGO

    private Account getAccountMongo(UUID uuid) {
        final MongoCollection<Document> accCollection = getAccCollection();

        D.d("Going to mongo for query.");

        final Document document = accCollection.find(eq(Account.ID_UUID, uuid.toString())).first();

        if (document == null)
            return null;

        return new Account(getPlugin(), document);
    }

    private void insertAccountMongo(Account account) {
        final MongoCollection<Document> accCollection = getAccCollection();
        accCollection.insertOne(account.toDocument());
    }

    public void updateAccountMongo(UUID uuid, String key, Object object) {
        final MongoCollection<Document> accCollection = getAccCollection();
        accCollection.updateOne(eq(Account.ID_UUID, uuid.toString()), set(key, object));

        accountSynchronizer.sendSync(uuid, key, TSPPlugin.gson.toJson(object));
    }

    // MISC

    public Map<UUID, Account> getAccountMap() {
        return Collections.unmodifiableMap(onlineAccounts);
    }

    public LoadingCache<UUID, Account> getCachedAccounts() {
        return cachedAccounts;
    }

    public AccountEventWatcher getAccountEventWatcher() {
        return accountEventWatcher;
    }

    private MongoCollection<Document> getAccCollection() {
        return getPlugin().getMongoManager().getCollection(MongoCollectionId.ACCOUNT);
    }

}
