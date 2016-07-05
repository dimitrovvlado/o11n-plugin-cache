package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

import java.util.Set;

public interface MapService {

    public String put(String key, String value, long ttl, TimeUnit timeUnit);

    public String putForMap(String mapName, String key, String value, long ttl, TimeUnit timeUnit);

    public String get(String key);

    public String getForMap(String mapName, String key);

    public void delete(String key);

    public void deleteForMap(String mapName, String key);

    public Set<String> keys();

    public Set<String> keysForMap(String mapName);

}
