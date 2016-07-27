package com.vmware.o11n.plugin.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.ServiceConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.instance.NodeState;
import com.vmware.o11n.plugin.cache.hazelcast.LockServiceImpl;
import com.vmware.o11n.plugin.cache.util.HazelcastUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

public class HazelcastUtilsTest {

    private static Config config;

    private static HazelcastInstance instance;

    @BeforeClass
    public static void setup() {
        config = new Config();
        NetworkConfig network = config.getNetworkConfig();
        network.getJoin().getMulticastConfig().setEnabled(false);
        network.getJoin().getTcpIpConfig().setEnabled(true);
        network.setPortAutoIncrement(true);
        network.getJoin().getTcpIpConfig().addMember("127.0.0.1");

        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setName(LockServiceImpl.SERVICE_NAME);
        serviceConfig.setEnabled(true);
        serviceConfig.setClassName(LockServiceImpl.class.getCanonicalName());

        config.getServicesConfig().addServiceConfig(serviceConfig);

        instance = Hazelcast.newHazelcastInstance(config);
        Hazelcast.newHazelcastInstance(config);
    }

    @Test
    public void testExtractState() {
        Set<Member> members = instance.getCluster().getMembers();
        for (Member m : members) {
            String state = HazelcastUtils.extractMemberState(m);
            Assert.assertEquals(NodeState.ACTIVE.toString(), state);
        }
    }

    @Test
    public void testExtractLocality() {
        Set<Member> members = instance.getCluster().getMembers();
        boolean atLeastOne = false;
        for (Member m : members) {
            boolean isLocal = HazelcastUtils.extractMemberLocality(m);
            if (isLocal) {
                //Avoid using hamcrest for now.
                atLeastOne = true;
            }
        }
        Assert.assertTrue("At least one member should be the local one.", atLeastOne);
    }
}
