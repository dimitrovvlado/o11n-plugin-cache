package com.vmware.o11n.plugin.cache.hazelcast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.hazelcast.config.ServiceConfig;
import com.hazelcast.core.Member;
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

    private final IEndpointConfigurationService configurationService;

    private HazelcastInstance instance;
    private Config config;
    private String localAddress;

    @Value("#{systemProperties['cache-plugin.connect.port'] ?: 5701}")
    private int port;

    @Value("#{systemProperties['cache-plugin.connect.retries'] ?: 10}")
    private int retries;

    @Value("#{systemProperties['cache-plugin.connect.retries']}")
    private String outboundPorts;

    @Autowired
    public HazelcastInstanceWrapper(IEndpointConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        networkConfig.setPortAutoIncrement(true);
        networkConfig.setPort(port);
        networkConfig.getJoin().getTcpIpConfig().addMember("127.0.0.1");
        if (outboundPorts != null) {
            networkConfig.addOutboundPortDefinition(outboundPorts);
        }

        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setName(LockServiceImpl.SERVICE_NAME);
        serviceConfig.setEnabled(true);
        serviceConfig.setClassName(LockServiceImpl.class.getCanonicalName());

        config.getServicesConfig().addServiceConfig(serviceConfig);


        InetAddress localHost;
        try {
            localHost = InetAddress.getLocalHost();
            localAddress = localHost.getHostAddress();

            IEndpointConfiguration endpointConfiguration = configurationService.getEndpointConfiguration(localAddress);
            if (endpointConfiguration == null) {
                endpointConfiguration = configurationService.newEndpointConfiguration(localAddress);
            }
            endpointConfiguration.setString("host", localAddress);
            endpointConfiguration.setInt("port", port);

            log.debug("Saving address {} as an endpoint configuration.", localAddress);
            configurationService.saveEndpointConfiguration(endpointConfiguration);

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
     * Return the members of the cluster.
     *
     * @return the members of the cluster.
     */
    public List<Member> getMembers() {
        return new ArrayList<>(getInstance().getCluster().getMembers());
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
