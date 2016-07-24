package com.vmware.o11n.plugin.cache.hazelcast;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.ICondition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public interface ILock extends Lock, DistributedObject {

    /**
     * Returns the lock object, the key for this lock instance.
     *
     * @return lock object.
     * @deprecated use {@link #getName()} instead.
     */
    @Deprecated
    Object getKey();

    /**
     * {@inheritDoc}
     */
    void lock(String workflowToken);

    /**
     * {@inheritDoc}
     */
    boolean tryLock(String workflowToken);

    /**
     * {@inheritDoc}
     */
    boolean tryLock(String workflowToken, long time, TimeUnit unit) throws InterruptedException;

    /**
     * Tries to acquire the lock for the specified lease time.
     * <p>After lease time, the lock will be released.
     * <p/>
     * <p>If the lock is not available, then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of two things happens:
     * <ul>
     * <li>the lock is acquired by the current thread, or
     * <li>the specified waiting time elapses.
     * </ul>
     * <p/>
     *
     * @param time     maximum time to wait for the lock.
     * @param unit time unit of the <tt>time</tt> argument.
     * @param leaseTime time to wait before releasing the lock.
     * @param leaseUnit unit of time to specify lease time.
     * @return <tt>true</tt> if the lock was acquired and <tt>false</tt>
     * if the waiting time elapsed before the lock was acquired.
     */
    boolean tryLock(String workflowToken, long time, TimeUnit unit, long leaseTime, TimeUnit leaseUnit) throws
            InterruptedException;

    /**
     * Releases the lock.
     */
    void unlock(String workflowToken);

    /**
     * Acquires the lock for the specified lease time.
     * <p>After lease time, lock will be released..
     * <p/>
     * <p>If the lock is not available then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until the lock has been acquired.
     * <p/>
     *
     * @param leaseTime time to wait before releasing the lock.
     * @param timeUnit unit of time for the lease time.
     *
     * @throws IllegalMonitorStateException if the current thread does not
     * hold this lock
     */
    void lock(String workflowToken, long leaseTime, TimeUnit timeUnit);

    /**
     * Releases the lock regardless of the lock owner.
     * It always successfully unlocks, never blocks, and returns immediately.
     */
    void forceUnlock();

    /**
     * This method is not implemented! Use {@link #newCondition(String)} instead.
     *
     * @throws UnsupportedOperationException
     */
    Condition newCondition();

    /**
     * Returns a new {@link ICondition} instance that is bound to this
     * {@code ILock} instance with given name.
     *
     * <p>Before waiting on the condition the lock must be held by the
     * current thread.
     * A call to {@link ICondition#await()} will atomically release the lock
     * before waiting and re-acquire the lock before the wait returns.
     *
     * @param name identifier of the new condition instance
     * @return A new {@link ICondition} instance for this {@code ILock} instance
     * @throws java.lang.NullPointerException if name is null.
     */
    ICondition newCondition(String name);

    /**
     * Returns whether this lock is locked or not.
     *
     * @return {@code true} if this lock is locked, {@code false} otherwise.
     */
    boolean isLocked();

    /**
     * Returns whether this lock is locked by current thread or not.
     *
     * @return {@code true} if this lock is locked by current thread, {@code false} otherwise.
     */
    boolean isLockedByCurrentThread();

    /**
     * Returns re-entrant lock hold count, regardless of lock ownership.
     *
     * @return the lock hold count.
     */
    int getLockCount();

    /**
     * Returns remaining lease time in milliseconds.
     * If the lock is not locked then -1 will be returned.
     *
     * @return remaining lease time in milliseconds.
     */
    long getRemainingLeaseTime();

}
