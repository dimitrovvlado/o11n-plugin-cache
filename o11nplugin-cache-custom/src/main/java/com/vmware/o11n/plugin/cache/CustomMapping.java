package com.vmware.o11n.plugin.cache;

import com.vmware.o11n.plugin.cache.model.TimeUnit;
import com.vmware.o11n.plugin.cache.service.IdGeneratorService;
import com.vmware.o11n.plugin.cache.service.MapService;
import com.vmware.o11n.plugin.cache.service.QueueService;
import com.vmware.o11n.plugin.cache.singleton.CacheManager;
import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;

public class CustomMapping extends AbstractMapping {
	@Override
	public void define() {
		singleton(CacheManager.class).as("Manager");
		wrap(MapService.class).as("Map");
		wrap(QueueService.class).as("Queue");
		wrap(IdGeneratorService.class).as("IdGenerator");
		enumerate(TimeUnit.class);
	}
}