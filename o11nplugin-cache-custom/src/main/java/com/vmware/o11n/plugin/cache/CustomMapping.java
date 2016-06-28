package com.vmware.o11n.plugin.cache;

import com.vmware.o11n.plugin.cache.hazelcast.model.TimeUnit;
import com.vmware.o11n.plugin.cache.singleton.CacheManager;
import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;

public class CustomMapping extends AbstractMapping {
	@Override
	public void define() {
		singleton(CacheManager.class).as("Manager");
		enumerate(TimeUnit.class);
	}
}