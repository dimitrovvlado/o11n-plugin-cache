package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

import java.util.Set;

public interface MapService {

    String put(String key, String value, long ttl, TimeUnit timeUnit);

    String putForMap(String mapName, String key, String value, long ttl, TimeUnit timeUnit);

    String get(String key);

    String getForMap(String mapName, String key);

    void delete(String key);

    void deleteForMap(String mapName, String key);

    Set<String> keys();

    Set<String> keysForMap(String mapName);

}
