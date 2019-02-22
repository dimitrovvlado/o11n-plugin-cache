package com.vmware.o11n.plugin.cache;

import java.lang.reflect.Method;

import com.hazelcast.core.Member;
import com.vmware.o11n.plugin.cache.extension.MemberExtension;
import com.vmware.o11n.plugin.cache.finder.MemberFinder;
import com.vmware.o11n.plugin.cache.model.TimeUnit;
import com.vmware.o11n.plugin.cache.relater.RootHasMembers;
import com.vmware.o11n.plugin.cache.service.*;
import com.vmware.o11n.plugin.cache.singleton.CacheManager;
import com.vmware.o11n.plugin.cache.singleton.IdGeneratorManager;
import com.vmware.o11n.plugin.cache.singleton.LockManager;
import com.vmware.o11n.plugin.cache.util.Base64Encoder;
import com.vmware.o11n.sdk.modeldrivengen.mapping.AbstractMapping;
import com.vmware.o11n.sdk.modeldrivengen.mapping.MethodRenamePolicy;

public class CustomMapping extends AbstractMapping {

	@Override
	public void define() {
		singleton(CacheManager.class).as("#CacheManager");
		singleton(LockManager.class).as("#LockManager");
		singleton(IdGeneratorManager.class).as("#IdGeneratorManager");
		singleton(Base64Encoder.class).as("#Base64");

		wrap(ListService.class).as("List");
        wrap(SetService.class).as("Set");
		wrap(MapService.class).as("Map");
		wrap(QueueService.class).as("Queue");
        wrap(RingbufferService.class).as("Ringbuffer");
		wrap(IdGeneratorService.class).as("IdGenerator");
        wrap(LockService.class).as("Lock");
		enumerate(TimeUnit.class);

		MethodRenamePolicy renamePolicy = new MethodRenamePolicy() {

			@Override
			public String rename(Method method) {
				if (method.getName().equals("localMember")) {
					return "getLocalMemberInternal";
				}
				return method.getName();
			}
			
		};
		wrap(Member.class).
                extendWith(MemberExtension.class).
                rename(renamePolicy).
                andFind().
                using(MemberFinder.class).
                withIcon("item-16x16.png");

        relateRoot().
                to(Member.class).
                using(RootHasMembers.class).
                as("members");
	}
}