package com.vmware.o11n.plugin.cache.service.hazelcast;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.hazelcast.ILock;
import com.vmware.o11n.plugin.cache.hazelcast.LockServiceImpl;
import com.vmware.o11n.plugin.cache.model.TimeUnit;
import com.vmware.o11n.plugin.cache.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.IllegalStateException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class HazelcastLockService implements LockService {

    @Value("#{systemProperties['cache-plugin.default.lock.name'] ?: 'vro_default_lock'}")
    private String defaultLockName;

    private final HazelcastInstanceWrapper hazelcastWrapper;

    @Autowired
    public HazelcastLockService(HazelcastInstanceWrapper hazelcastWrapper) {
        Assert.notNull(hazelcastWrapper, "hazelcastWrapper cannot be null.");
        this.hazelcastWrapper = hazelcastWrapper;
    }

    @Override
    public void lockForName(String lockName, String workflowToken, long leaseTime, TimeUnit timeUnit) {
        Assert.notNull(lockName, "lockName cannot be null.");
        Assert.notNull(workflowToken, "workflowToken cannot be null.");
        Assert.isTrue(leaseTime >= 0, "leaseTime must be greater or equal than 0.");
        Assert.notNull(timeUnit, "timeUnit cannot be null.");

        getLock(lockName).lock(workflowToken, leaseTime, timeUnit.convertToConcurrentTimeUnit());
    }

    @Override
    public void lock(String workflowToken, long leaseTime, TimeUnit timeUnit) {
        lockForName(defaultLockName, workflowToken, leaseTime, timeUnit);
    }

    @Override
    public void unlockForName(String lockName, String workflowToken) {
        Assert.notNull(lockName, "lockName cannot be null.");
        Assert.notNull(workflowToken, "workflowToken cannot be null.");

        try {
            getLock(lockName).unlock(workflowToken);
        } catch(IllegalMonitorStateException e) {
            //Fixing the error message of the exception, until the lock operations are moved to the plug-in.
            if (e.getMessage().contains("Current thread is not owner of the lock")) {
                throw new IllegalMonitorStateException("Current workflow token is not owner of the lock");
            }
            throw e;
        }
    }

    @Override
    public void unlock(String workflowToken) {
        unlockForName(defaultLockName, workflowToken);
    }

    @Override
    public void forceUnlockForName(String lockName) {
        Assert.notNull(lockName, "lockName cannot be null.");

        getLock(lockName).forceUnlock();
    }

    @Override
    public void forceUnlock() {
        forceUnlockForName(defaultLockName);
    }

    @Override
    public int getLockCountForName(String lockName) {
        Assert.notNull(lockName, "lockName cannot be null.");
        return getLock(lockName).getLockCount();
    }

    @Override
    public int getLockCount() {
        return getLockCountForName(defaultLockName);
    }

    @Override
    public boolean tryLockForName(String lockName, String workflowToken, long time, TimeUnit timeUnit) {
        Assert.notNull(lockName, "lockName cannot be null.");
        ILock lock = getLock(lockName);
        if (timeUnit == null) {
            return lock.tryLock(workflowToken);
        }
        try {
            return lock.tryLock(workflowToken, time, timeUnit.convertToConcurrentTimeUnit());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryLock(String workflowToken, long time, TimeUnit timeUnit) {
        return tryLockForName(defaultLockName, workflowToken, time, timeUnit);
    }

    @Override
    public boolean tryLockForNameWithLease(String lockName, String workflowToken, long waitTime, TimeUnit
            waitTimeUnit, long leaseTime, TimeUnit leaseTimeUnit) {
        Assert.notNull(lockName, "lockName cannot be null.");
        Assert.isTrue(waitTime >= 0, "waitTime must be greater or equal than 0.");
        Assert.notNull(waitTimeUnit, "waitTimeUnit cannot be null.");
        Assert.isTrue(leaseTime >= 0, "leaseTime must be greater or equal than 0.");
        Assert.notNull(leaseTimeUnit, "leaseTimeUnit cannot be null.");

        try {
            return getLock(lockName).tryLock(workflowToken, waitTime, waitTimeUnit.convertToConcurrentTimeUnit(),
                    leaseTime, leaseTimeUnit.convertToConcurrentTimeUnit());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryLockWithLease(String workflowToken, long waitTime, TimeUnit waitTimeUnit, long leaseTime,
                                    TimeUnit leaseTimeUnit) {
        return tryLockForNameWithLease(defaultLockName, workflowToken, waitTime, waitTimeUnit, leaseTime, leaseTimeUnit);
    }

    @Override
    public long getRemainingLeaseTimeForName(String lockName) {
        return getLock(lockName).getRemainingLeaseTime();
    }

    @Override
    public long getRemainingLeaseTime() {
        return getRemainingLeaseTimeForName(defaultLockName);
    }

    @Override
    public boolean isLockedForName(String lockName) {
        return getLock(lockName).isLocked();
    }

    @Override
    public boolean isLocked() {
        return isLockedForName(defaultLockName);
    }

    private ILock getLock(String lockName) {
        return hazelcastWrapper.getInstance().getDistributedObject(LockServiceImpl.SERVICE_NAME, lockName);
    }


}
