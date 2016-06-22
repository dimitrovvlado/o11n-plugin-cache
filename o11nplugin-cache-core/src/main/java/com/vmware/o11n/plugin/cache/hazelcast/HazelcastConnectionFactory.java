package com.vmware.o11n.plugin.cache.hazelcast;

import org.springframework.stereotype.Component;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Component
public class HazelcastConnectionFactory {
	
	public HazelcastConnectionFactory() {
	}

	public HazelcastInstance newInstance(Config config) {
		return Hazelcast.newHazelcastInstance(config);
	}

}
