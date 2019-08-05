package net.tsp.core.account;

import com.mongodb.client.model.Filters;
import net.tsp.core.mongo.MongoCollectionId;
import net.tsp.core.redis.RedisDatabaseIndex;
import net.tsp.core.util.UtilPlayer;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * @author Ellie :: 26/07/2019
 */
public class UUIDNameLookup {

    private AccountManager accountManager;

    public UUIDNameLookup(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public UUID getUUID(String name) {
        if (UtilPlayer.getPlayer(name) != null)
            return UtilPlayer.getPlayer(name).getUniqueId();

        UUID uuid = null;

        if (accountManager.getPlugin().getRedisManager().isActive()) {
            try (Jedis jedis = accountManager.getPlugin().getRedisManager().getPool().getResource()) {
                jedis.select(RedisDatabaseIndex.NAME_UUID);
                if (jedis.exists(name.toLowerCase()))
                    uuid = UUID.fromString(jedis.get(name.toLowerCase()));
            }
        }

        if (uuid == null) {
            final Document nameQuery = accountManager.getPlugin().getMongoManager().
                    getCollection(MongoCollectionId.ACCOUNT).find(Filters.eq(Account.ID_NAME, name)).first();

            if (nameQuery != null) {
                uuid = UUID.fromString(nameQuery.getString(Account.ID_UUID));
                updateCache(uuid, null, name);
            }

        }

        return uuid;
    }

    public void updateCache(UUID uuid, String oldName, String name) {
        if (accountManager.getPlugin().getRedisManager().isActive()) {
            try (Jedis jedis = accountManager.getPlugin().getRedisManager().getPool().getResource()) {
                jedis.select(RedisDatabaseIndex.NAME_UUID);
                if (oldName != null) jedis.del(oldName);
                jedis.set(name.toLowerCase(), uuid.toString());
            }
        }
    }

}
