package com.vmware.o11n.plugin.cache.hazelcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.hazelcast.config.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(HazelcastInstanceWrapper.class);

    @Autowired
    private IEndpointConfigurationService configurationService;

    private HazelcastInstance instance;
    private final Config config;

    private String localAddress;

    @Value("#{systemProperties['cache-plugin.connect.retries'] ?: 10}")
    private int retries;

    public HazelcastInstanceWrapper() {
        config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        networkConfig.setPortAutoIncrement(true);
        networkConfig.getJoin().getTcpIpConfig().addMember("127.0.0.1");

        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setName(LockServiceImpl.SERVICE_NAME);
        serviceConfig.setEnabled(true);
        serviceConfig.setClassName(LockServiceImpl.class.getCanonicalName());

        config.getServicesConfig().addServiceConfig(serviceConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
            localAddress = localHost.getHostAddress();

            IEndpointConfiguration newEndpointConfiguration = configurationService
                    .newEndpointConfiguration(localAddress);
            log.debug("Saving address {} as an endpoint configuration.", localAddress);
            configurationService.saveEndpointConfiguration(newEndpointConfiguration);

            boolean successfulConnect = false;
            for (int i = 0; i < retries; i++) {
                if (connectSuccessfully()) {
                    successfulConnect = true;
                    break;
                }
            }
            if (!successfulConnect) {
                String msg = String.format(
                        "Couldn't establish a connection with the remote hazelcast instances in %d retries", retries);
                log.error(msg);
                throw new IllegalStateException(msg);
            }
        } catch (UnknownHostException e) {
            log.error("Could not get local host", e);
            throw new RuntimeException("Could not get local host", e);
        } catch (IOException e) {
            log.error("Could not save endpoint configuration", e);
            throw new RuntimeException("Could not save endpoint configuration", e);
        }
    }

    /**
     * Returns the hazelcast instance. Cannot be null.
     * 
     * @return
     */
    public HazelcastInstance getInstance() {
        return instance;
    }

    /**
     * Destroys the hazelcast instance.
     */
    public void destroy() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
    }

    /**
     * Attempts to create/join a cluster
     * 
     * @return false if attempt was not successful, returns true otherwise
     */
    private boolean connectSuccessfully() {
        try {
            String oldVersion = configurationService.getVersion();

            List<String> members = new LinkedList<>();
            Collection<IEndpointConfiguration> endpointConfigurations = configurationService
                    .getEndpointConfigurations();
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
            log.warn("Error reading the endpoint configurations. Will retry.", e);
            return false;
        }
        return true;
    }

}
