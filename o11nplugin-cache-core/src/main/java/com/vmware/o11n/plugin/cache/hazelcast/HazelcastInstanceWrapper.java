package com.vmware.o11n.plugin.cache.hazelcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import ch.dunes.vso.sdk.endpoints.IEndpointConfiguration;
import ch.dunes.vso.sdk.endpoints.IEndpointConfigurationService;

@Component
public class HazelcastInstanceWrapper implements InitializingBean {

	@Autowired
	private IEndpointConfigurationService configurationService;
	
	private HazelcastInstance instance;
	private final Config config;
	
	private String localAddress;
	
	@Value("#{systemProperties['cache.connect.retries'] ?: 10}")
	private int retries;

	public HazelcastInstanceWrapper() {
		config = new Config();
		NetworkConfig networkConfig = config.getNetworkConfig();
		networkConfig.getJoin().getMulticastConfig().setEnabled(false);
		networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
		networkConfig.setPortAutoIncrement(true);
		networkConfig.getJoin().getTcpIpConfig().addMember("127.0.0.1");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		InetAddress localHost;
		try {
			localHost = InetAddress.getLocalHost();
			localAddress = localHost.getHostAddress();
			
			IEndpointConfiguration newEndpointConfiguration = configurationService.newEndpointConfiguration(localAddress);
			configurationService.saveEndpointConfiguration(newEndpointConfiguration);
			
			for (int i = 0; i < retries; i++) {
				if(connectSuccessfully()) {
					break;
				}
				//TODO log and throw an exception
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the hazelcast instance. Cannot be null.  
	 * @return
	 */
	public HazelcastInstance getInstance() {
		return instance;
	}
	
	public void destroy() {
		if (instance != null) {
			instance.shutdown();
			instance = null;
		}
	}

	/**
	 * Attempts to create/join a cluster
	 * @return false if attempt was not successful, returns true otherwise
	 */
	private boolean connectSuccessfully() {
		try {
			String oldVersion = configurationService.getVersion();
			
			List<String> members = new LinkedList<>();
			Collection<IEndpointConfiguration> endpointConfigurations = configurationService.getEndpointConfigurations();
			for (IEndpointConfiguration iEndpointConfiguration : endpointConfigurations) {
				String hostAddress = iEndpointConfiguration.getId();
				if (!hostAddress.equals(localAddress)) {
					members.add(hostAddress);
				}
			}
			config.getNetworkConfig().getJoin().getTcpIpConfig().setMembers(members);
			
			instance = Hazelcast.newHazelcastInstance(config);
			
			String newVersion = configurationService.getVersion();
			
			if (!oldVersion.equals(newVersion)) {
				destroy();
				return false;
			}
		} catch (IOException e) {
			//TODO log
			return false;
		}
		return true;
	}

}
