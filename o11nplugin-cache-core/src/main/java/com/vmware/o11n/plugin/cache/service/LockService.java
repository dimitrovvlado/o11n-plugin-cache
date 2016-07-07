package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

public interface LockService {
    void lockForName(String lockName, long leaseTime, TimeUnit timeUnit);

    void lock(long leaseTime, TimeUnit timeUnit);

    void unlockForName(String lockName);

    void unlock();

    /**
     * Releases the lock regardless of the lock owner.
     * It always unlocks successfully, won't block, and  will return immediately.
     *
     * @param lockName the name of the lock
     */
    void forceUnlockForName(String lockName);

    /**
     * Releases the lock regardless of the lock owner.
     * It always unlocks successfully, won't block, and  will return immediately.
     */
    void forceUnlock();

    /**
     * Returns re-entrant lock hold count, for all lock owners.
     *
     * @param lockName the name of the lock
     * @return the lock hold count.
     */
    int getLockCountForName(String lockName);

    /**
     * Returns re-entrant lock hold count, for all lock owners for the default lock name.
     *
     * @return the lock hold count.
     */
    int getLockCount();

    boolean tryLockForName(String lockName, long time, TimeUnit timeUnit);

    boolean tryLock(long time, TimeUnit timeUnit);

    boolean tryLockForNameWithLease(String lockName, long time, TimeUnit timeUnit, long leaseTime, TimeUnit leaseUnit);

    boolean tryLockWithLease(long time, TimeUnit timeUnit, long leaseTime, TimeUnit leaseUnit);

    long getRemainingLeaseTimeForName(String lockName);

    long getRemainingLeaseTime();

    boolean isLockedForName(String lockName);

    boolean isLocked();
}
