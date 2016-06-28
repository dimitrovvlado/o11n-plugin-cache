package com.vmware.o11n.plugin.cache.singleton;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vmware.o11n.plugin.cache.CacheService;

@Component
@Scope(value = "prototype")
public class CacheManager {
	
	@Autowired
	private CacheService service;
	
	public String put(String key, String value, long ttl, TimeUnit timeUnit) {
		if (timeUnit != null) {
			return service.put(key, value, ttl, timeUnit);
		}
		return service.put(key, value); 
	}
	
	public String get(String key) {
		return service.get(key);
	}
	
}
