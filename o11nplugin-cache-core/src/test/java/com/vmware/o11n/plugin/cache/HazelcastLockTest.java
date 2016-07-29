package com.vmware.o11n.plugin.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.ServiceConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.vmware.o11n.plugin.cache.hazelcast.ILock;
import com.vmware.o11n.plugin.cache.hazelcast.LockServiceImpl;
import org.junit.*;

import java.util.concurrent.TimeUnit;

public class HazelcastLockTest {

    private static final String LOCK_NAME = "test_lock";

    private static final String UUID1 = "5b936c4d-870e-4656-b504-8585f33e05e6";
    private static final String UUID2 = "dbc38905-7475-4ac1-abd1-1ca53621c584";

    private static Config config;

    private static HazelcastInstance instance1;
    private static HazelcastInstance instance2;

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

        instance1 = Hazelcast.newHazelcastInstance(config);
        instance2 = Hazelcast.newHazelcastInstance(config);
    }

    @AfterClass
    public static void destroy() {
        instance1.shutdown();
        instance2.shutdown();
    }

    @After
    public void tearDown() {
        ILock lock = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);
        lock.forceUnlock();
    }

    @Test
    public void testSingleInstanceLocking() {
        ILock test = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);

        test.lock(UUID1);
        test.lock(UUID1);

        Assert.assertEquals(2, test.getLockCount());

        test.forceUnlock();

        Assert.assertEquals(0, test.getLockCount());

        test.lock(UUID2);
        test.lock(UUID2);

        Assert.assertEquals(2, test.getLockCount());

        test.unlock(UUID2);

        Assert.assertEquals(1, test.getLockCount());
    }

    @Test
    public void testLockingMultipleInstances() {
        ILock test1 = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, "test");
        ILock test2 = instance2.getDistributedObject(LockServiceImpl.SERVICE_NAME, "test");

        test1.lock(UUID1);

        Assert.assertEquals(1, test1.getLockCount());
        Assert.assertEquals(1, test2.getLockCount());

        test2.forceUnlock();

        Assert.assertEquals(0, test1.getLockCount());
        Assert.assertEquals(0, test2.getLockCount());

        test1.lock(UUID1);
        test1.lock(UUID1);

        Assert.assertEquals(2, test1.getLockCount());
        Assert.assertEquals(2, test2.getLockCount());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testNegativeUnlockFromSecondInstance() {
        ILock test1 = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);
        ILock test2 = instance2.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);

        test1.lock(UUID1);
        test2.unlock(UUID1);
    }

    @Test
    public void testIsLocked() {
        ILock test1 = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);

        test1.lock(UUID1);

        Assert.assertTrue(test1.isLocked());

        test1.unlock(UUID1);

        Assert.assertFalse(test1.isLocked());
    }

    @Test
    public void testTryLockWithTimeout() throws InterruptedException {
        ILock test1 = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);
        ILock test2 = instance2.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);

        test1.lock(UUID1);

        boolean lockResult = test2.tryLock(UUID1, 5, TimeUnit.SECONDS);

        Assert.assertFalse(lockResult);

        test1.unlock(UUID1);

        lockResult = test2.tryLock(UUID1, 5, TimeUnit.SECONDS);

        Assert.assertTrue(lockResult);
    }

    @Test
    public void testTryLockWithLease() throws InterruptedException {
        ILock test1 = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);
        ILock test2 = instance2.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);

        test1.lock(UUID1, 5, TimeUnit.SECONDS);

        boolean lockResult = test2.tryLock(UUID1, 10, TimeUnit.SECONDS);

        Assert.assertTrue(lockResult);
    }

    @Test
    public void testTryNegativeLockWithLease() throws InterruptedException {
        ILock test1 = instance1.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);
        ILock test2 = instance2.getDistributedObject(LockServiceImpl.SERVICE_NAME, LOCK_NAME);

        test1.lock(UUID1, 10, TimeUnit.SECONDS);

        boolean lockResult = test2.tryLock(UUID1, 2, TimeUnit.SECONDS);

        Assert.assertFalse(lockResult);
    }

}
