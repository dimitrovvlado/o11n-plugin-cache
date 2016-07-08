package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

import java.util.Set;

/**
 * Service for working with distributed map instances.
 */
public interface MapService {

    /**
     * Puts an entry into the default map.  If timeUnit is provided, puts the entry for the specified time.
     *
     * @param key key of the entry.
     * @param value value of the entry.
     * @param ttl maximum time for this entry to stay in the map 0 means infinite.
     * @param timeUnit time unit for the {@code ttl}.
     *
     * @return old value of the entry.
     */
    String put(String key, String value, long ttl, TimeUnit timeUnit);

    /**
     * Puts an entry into the default map.  If timeUnit is provided, puts the entry for the specified time.
     *
     * @param mapName name of the distributed map
     * @param key key of the entry.
     * @param value value of the entry.
     * @param ttl maximum time for this entry to stay in the map 0 means infinite.
     * @param timeUnit time unit for the {@code ttl}.
     *
     * @return old value of the entry.
     */
    String putForMap(String mapName, String key, String value, long ttl, TimeUnit timeUnit);

    /**
     * This method returns a clone of the original value.
     *
     * @param key the key of the entry.
     *
     * @return the value
     */
    String get(String key);

    /**
     * This method returns a clone of the original value.
     *
     * @param mapName name of the map.
     * @param key the key of the entry.
     *
     * @return the value
     */
    String getForMap(String mapName, String key);

    /**
     * Removes the mapping for a key from the default map if it is present.
     *
     * @param key the key of the mapping which is to be removed from the map.
     */
    void delete(String key);

    /**
     * Removes the mapping for a key from the specified map if it is present.
     *
     * @param mapName name of the map.
     * @param key the key of the mapping which is to be removed from the map.
     */
    void deleteForMap(String mapName, String key);

    /**
     * Returns a set clone of the keys contained in the default map.
     *
     * @return a set of the existing keys.
     */
    Set<String> keys();

    /**
     * Returns a set clone of the keys contained in the specified map.
     *
     * @param mapName name of the map.
     *
     * @return a set of the existing keys.
     */
    Set<String> keysForMap(String mapName);

}
