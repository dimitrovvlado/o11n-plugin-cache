package com.vmware.o11n.plugin.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Component
public class HazelcastCacheService implements CacheService, InitializingBean {

	private String defaultMapName = "vro_default";
	
	private HazelcastInstance instance;
	
	@Override
	public String put(String key, String value) {
		return (String) instance.getMap(defaultMapName).put(key, value);
	}

	@Override
	public String put(String key, String value, long ttl, TimeUnit timeUnit) {
		return (String) instance.getMap(defaultMapName).put(key, value, ttl, timeUnit);
	}

	@Override
	public String get(String key) {
		return (String) instance.getMap(defaultMapName).get(key);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		instance = Hazelcast.newHazelcastInstance(new Config());
	}

}
