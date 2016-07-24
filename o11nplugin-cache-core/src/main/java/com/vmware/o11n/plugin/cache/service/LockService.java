package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

/**
 * Service for working with distributed locks.
 */
public interface LockService {

    /**
     * Acquires a lock if no time unit is provided. If timeUnit is provided, acquires the lock for the specified
     * lease time.
     *
     * If the lock is not available then the current thread becomes disabled for thread scheduling purposes and lies
     * dormant until the lock has been acquired.
     *
     * @param lockName name of the lock instance.
     *
     * @param workflowToken workflow token id, cannot be null.
     *
     * @param leaseTime time to wait before releasing the lock, if null the lock will be acquired until someone
     *                  releases it from the same thread or invokes the {@link #forceUnlock()} method.
     * @param timeUnit unit of time for the lease time, cannot be null.
     */
    void lockForName(String lockName, String workflowToken, long leaseTime, TimeUnit timeUnit);

    /**
     * Acquires a lock if no time unit is provided. If timeUnit is provided, acquires the lock for the
     * specified lease time.
     *
     * If the lock is not available then the current thread becomes disabled for thread scheduling purposes and lies
     * dormant until the lock has been acquired.
     *
     * @param workflowToken workflow token id, cannot be null.
     *
     * @param leaseTime time to wait before releasing the lock, if null the lock will be acquired until someone
     *                  unlocks it from the workflowToken or invokes the {@link #forceUnlock()} method
     *
     * @param timeUnit unit of time for the lease time
     */
    void lock(String workflowToken, long leaseTime, TimeUnit timeUnit);

    /**
     * Releases the lock.
     *
     * @param lockName name of the lock instance.
     *
     * @param workflowToken workflow token id, cannot be null.
     */
    void unlockForName(String lockName, String workflowToken);

    /**
     * Releases the lock.
     *
     * @param workflowToken workflow token id, cannot be null.
     */
    void unlock(String workflowToken);

    /**
     * Releases the lock regardless of the lock owner.
     * It always unlocks successfully, won't block, and  will return immediately.
     *
     * @param lockName name of the lock instance, cannot be null.
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
     * @param lockName the name of the lock instance, cannot be null.
     *
     * @return the lock hold count.
     */
    int getLockCountForName(String lockName);

    /**
     * Returns re-entrant lock hold count, for all lock owners for the default lock name.
     *
     * @return the lock hold count.
     */
    int getLockCount();

    /**
     * * Acquires the lock only if it is free at the time of invocation. If timeUnit is provided, will try within the
     * given time. If the lock is not available and the timeUnit is null, then this method will return immediately with
     * the value {@code false}.
     *
     * @param lockName the name of the lock instance, cannot be null.
     *
     * @param workflowToken workflow token id, cannot be null.
     *
     * @param time the maximum time to wait for the lock.   @return {@code true} if the lock was acquired and {@code
     * false} otherwise.
     *
     *             @param timeUnit the time unit of the {@code time} argument, can be null.
     */
    boolean tryLockForName(String lockName, String workflowToken, long time, TimeUnit timeUnit);

    /**
     * Acquires the lock only if it is free at the time of invocation. If timeUnit is provided, will try within the
     * given time. If the lock is not available and the timeUnit is null, then this method will return immediately with
     * the value {@code false}.
     *
     *
     * @param workflowToken workflow token id, cannot be null.
     *
     * @param time the maximum time to wait for the lock.
     *
     * @param timeUnit the time unit of the {@code time} argument, can be null.
     *
     * @return {@code true} if the lock was acquired and {@code false} otherwise.
     */
    boolean tryLock(String workflowToken, long time, TimeUnit timeUnit);


    /**
     * Tries to acquire a lock for the specified lease time for a lock instance. After the lease time expires, the lock
     * will be released.
     *
     * @param lockName name of the lock instance, cannot be null.
     *
     * @param workflowToken workflow token id, cannot be null.
     *
     * @param waitTime maximum time to wait for the lock.
     *
     * @param waitTimeUnit time unit of the {@code waitTime} argument.
     *
     * @param leaseTime time to wait before releasing the lock.
     *
     * @param leaseTimeUnit unit of time to specify lease time.
     *
     *
     *
     * @return {@code true} if the lock was acquired and {@code false} if the waiting time elapsed before the lock
     * was acquired.
     */
    boolean tryLockForNameWithLease(String lockName, String workflowToken, long waitTime, TimeUnit
            waitTimeUnit, long leaseTime, TimeUnit leaseTimeUnit);

    /**
     * Tries to acquire a lock for the specified lease time. After the lease time expires, the lock will be released.
     *
     *
     * @param workflowToken workflow token id, cannot be null.
     *
     * @param waitTime maximum time to wait for the lock.
     *
     * @param waitTimeUnit time unit of the {@code waitTime} argument.
     *
     * @param leaseTime time to wait before releasing the lock.
     *
     * @param leaseTimeUnit unit of time to specify lease time.
     *
     * @return {@code true} if the lock was acquired and {@code false} if the waiting time elapsed before the lock
     * was acquired.
     */
    boolean tryLockWithLease(String workflowToken, long waitTime, TimeUnit waitTimeUnit, long leaseTime,
                             TimeUnit leaseTimeUnit);

    /**
     * Returns remaining lease time in milliseconds for a lock instance. Returns {@code -1} if the lock is not locked.
     *
     * @param lockName name of the lock instance, cannot be null.
     *
     * @return remaining lease time in milliseconds.
     */
    long getRemainingLeaseTimeForName(String lockName);

    /**
     * Returns remaining lease time in milliseconds. Returns {@code -1} if the lock is not locked.
     *
     * @return remaining lease time in milliseconds.
     */
    long getRemainingLeaseTime();

    /**
     * Returns whether this lock for a lock instance name is locked or not.
     *
     * @param lockName name of the lock instance.
     *
     * @return {@code true} if this lock is locked, {@code false} otherwise.
     */
    boolean isLockedForName(String lockName);

    /**
     * Returns whether this lock is locked or not.
     *
     * @return {@code true} if this lock is locked, {@code false} otherwise.
     */
    boolean isLocked();
}
