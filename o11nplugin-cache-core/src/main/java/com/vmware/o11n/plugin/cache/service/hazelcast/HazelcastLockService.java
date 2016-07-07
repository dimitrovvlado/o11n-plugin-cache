package com.vmware.o11n.plugin.cache.service.hazelcast;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.model.TimeUnit;
import com.vmware.o11n.plugin.cache.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HazelcastLockService implements LockService {

    @Value("#{systemProperties['cache-plugin.default.lock.name'] ?: 'vro_default_lock'}")
    private String defaultLockName;

    @Autowired
    private HazelcastInstanceWrapper hazelcastWrapper;

    @Override
    public void lockForName(String lockName, long leaseTime, TimeUnit timeUnit) {
        if (timeUnit == null) {
            hazelcastWrapper.getInstance().getLock(lockName).lock();
        } else {
            hazelcastWrapper.getInstance().getLock(lockName).lock(leaseTime, timeUnit.convertToConcurrentTimeUnit());
        }
    }

    @Override
    public void lock(long leaseTime, TimeUnit timeUnit) {
        lockForName(defaultLockName, leaseTime, timeUnit);
    }

    @Override
    public void unlockForName(String lockName) {
        hazelcastWrapper.getInstance().getLock(lockName).unlock();
    }

    @Override
    public void unlock() {
        unlockForName(defaultLockName);
    }

    @Override
    public void forceUnlockForName(String lockName) {
        hazelcastWrapper.getInstance().getLock(lockName).forceUnlock();
    }

    @Override
    public void forceUnlock() {
        forceUnlockForName(defaultLockName);
    }

    @Override
    public int getLockCountForName(String lockName) {
        return hazelcastWrapper.getInstance().getLock(lockName).getLockCount();
    }

    @Override
    public int getLockCount() {
        return getLockCountForName(defaultLockName);
    }

    @Override
    public boolean tryLockForName(String lockName, long time, TimeUnit timeUnit) {
        if (timeUnit == null) {
            return hazelcastWrapper.getInstance().getLock(lockName).tryLock();
        }
        try {
            return hazelcastWrapper.getInstance().getLock(lockName).tryLock(time, timeUnit
                    .convertToConcurrentTimeUnit());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit timeUnit) {
        return tryLockForName(defaultLockName, time, timeUnit);
    }

    @Override
    public boolean tryLockForNameWithLease(String lockName, long time, TimeUnit timeUnit, long leaseTime, TimeUnit
            leaseUnit) {
        try {
            return hazelcastWrapper.getInstance().getLock(lockName).tryLock(time, timeUnit
                    .convertToConcurrentTimeUnit(), leaseTime, leaseUnit.convertToConcurrentTimeUnit());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryLockWithLease(long time, TimeUnit timeUnit, long leaseTime, TimeUnit leaseUnit) {
        return tryLockForNameWithLease(defaultLockName, time, timeUnit, leaseTime, leaseUnit);
    }

    @Override
    public long getRemainingLeaseTimeForName(String lockName) {
        return hazelcastWrapper.getInstance().getLock(lockName).getRemainingLeaseTime();
    }

    @Override
    public long getRemainingLeaseTime() {
        return getRemainingLeaseTimeForName(defaultLockName);
    }

    @Override
    public boolean isLockedForName(String lockName) {
        return hazelcastWrapper.getInstance().getLock(lockName).isLocked();
    }

    @Override
    public boolean isLocked() {
        return isLockedForName(defaultLockName);
    }
}
