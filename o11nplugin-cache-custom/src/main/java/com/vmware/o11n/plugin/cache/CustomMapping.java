package com.vmware.o11n.plugin.cache;

import com.hazelcast.core.Member;
import com.vmware.o11n.plugin.cache.extension.MemberExtension;
import com.vmware.o11n.plugin.cache.finder.MemberFinder;
import com.vmware.o11n.plugin.cache.model.TimeUnit;
import com.vmware.o11n.plugin.cache.relater.RootHasMembers;
import com.vmware.o11n.plugin.cache.service.*;
import com.vmware.o11n.plugin.cache.singleton.CacheManager;
import com.vmware.o11n.plugin.cache.singleton.IdGeneratorManager;
import com.vmware.o11n.plugin.cache.singleton.LockManager;
import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;

public class CustomMapping extends AbstractMapping {

	@Override
	public void define() {
		singleton(CacheManager.class).as("#CacheManager");
		singleton(LockManager.class).as("#LockManager");
		singleton(IdGeneratorManager.class).as("#IdGeneratorManager");

		wrap(ListService.class).as("List");
        wrap(SetService.class).as("Set");
		wrap(MapService.class).as("Map");
		wrap(QueueService.class).as("Queue");
        wrap(RingbufferService.class).as("Ringbuffer");
		wrap(IdGeneratorService.class).as("IdGenerator");
        wrap(LockService.class).as("Lock");
		enumerate(TimeUnit.class);

		wrap(Member.class).
                extendWith(MemberExtension.class).
                andFind().
                using(MemberFinder.class).
                withIcon("item-16x16.png");

        relateRoot().
                to(Member.class).
                using(RootHasMembers.class).
                as("members");
	}
}