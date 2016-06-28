package com.vmware.o11n.plugin.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;

@Component
public class HazelcastCacheService implements CacheService, InitializingBean {

	private String defaultMapName = "vro_default";

	@Autowired
	private HazelcastInstanceWrapper hazelcastWrapper;
	
	@Override
	public String put(String key, String value) {
		return (String) hazelcastWrapper.getInstance().getMap(defaultMapName).put(key, value);
	}

	@Override
	public String put(String key, String value, long ttl, TimeUnit timeUnit) {
		return (String) hazelcastWrapper.getInstance().getMap(defaultMapName).put(key, value, ttl, timeUnit);
	}

	@Override
	public String get(String key) {
		return (String) hazelcastWrapper.getInstance().getMap(defaultMapName).get(key);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

}
