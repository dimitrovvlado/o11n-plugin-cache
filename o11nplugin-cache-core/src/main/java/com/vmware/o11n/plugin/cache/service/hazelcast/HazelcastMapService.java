package com.vmware.o11n.plugin.cache.service.hazelcast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.model.TimeUnit;
import com.vmware.o11n.plugin.cache.service.MapService;

@Component
public class HazelcastMapService implements MapService {

    @Value("#{systemProperties['cache-plugin.default.map.name'] ?: 'vro_default_map'}")
    private String defaultMapName;

    @Autowired
    private HazelcastInstanceWrapper hazelcastWrapper;

    @Override
    public String put(String key, String value, long ttl, TimeUnit timeUnit) {
        return putForMap(defaultMapName, key, value, ttl, timeUnit);
    }

    @Override
    public String get(String key) {
        return getForMap(defaultMapName, key);
    }

    @Override
    public String putForMap(String mapName, String key, String value, long ttl, TimeUnit timeUnit) {
        if (timeUnit == null) {
            return (String) hazelcastWrapper.getInstance().getMap(mapName).put(key, value);
        }
        return (String) hazelcastWrapper.getInstance().getMap(mapName).put(key, value, ttl,
                timeUnit.convertToConcurrentTimeUnit());
    }

    @Override
    public String getForMap(String mapName, String key) {
        return (String) hazelcastWrapper.getInstance().getMap(mapName).get(key);
    }

    @Override
    public void deleteForMap(String mapName, String key) {
        hazelcastWrapper.getInstance().getMap(mapName).delete(key);
    }

    @Override
    public void delete(String key) {
        deleteForMap(defaultMapName, key);
    }

}
