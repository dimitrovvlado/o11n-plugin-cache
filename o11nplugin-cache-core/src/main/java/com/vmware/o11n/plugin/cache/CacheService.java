package com.vmware.o11n.plugin.cache;

import com.vmware.o11n.plugin.cache.hazelcast.model.TimeUnit;

public interface CacheService {

	public String put(String key, String value);
	
	public String put(String key, String value, long ttl, TimeUnit timeUnit);
	
	public String get(String key);
}
