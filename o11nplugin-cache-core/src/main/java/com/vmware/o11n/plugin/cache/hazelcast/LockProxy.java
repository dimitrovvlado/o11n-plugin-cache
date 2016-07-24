package com.vmware.o11n.plugin.cache.hazelcast;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.hazelcast.concurrent.lock.*;
import com.hazelcast.core.ICondition;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.AbstractDistributedObject;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.ObjectNamespace;
import com.hazelcast.util.ThreadUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import static com.hazelcast.util.Preconditions.checkNotNull;
import static com.hazelcast.util.Preconditions.checkPositive;

public class LockProxy extends AbstractDistributedObject<LockServiceImpl> implements ILock {

    private final String name;
    private final LockProxySupport lockSupport;
    private final Data key;
    private final int partitionId;

    public LockProxy(NodeEngine nodeEngine, LockServiceImpl lockService, String name) {
        super(nodeEngine, lockService);
        this.name = name;
        this.key = getNameAsPartitionAwareData();
        this.lockSupport = new LockProxySupport(new InternalLockNamespace(name), lockService.getMaxLeaseTimeInMillis());
        this.partitionId = getNodeEngine().getPartitionService().getPartitionId(key);
    }

    @Override
    public boolean isLocked() {
        return lockSupport.isLocked(getNodeEngine(), key);
    }

    @Override
    public boolean isLockedByCurrentThread() {
        return lockSupport.isLockedByCurrentThread(getNodeEngine(), key);
    }

    @Override
    public int getLockCount() {
        return lockSupport.getLockCount(getNodeEngine(), key);
    }

    @Override
    public long getRemainingLeaseTime() {
        return lockSupport.getRemainingLeaseTime(getNodeEngine(), key);
    }

    @Override
    public void lock(String workflowToken) {
        setThreadId(workflowToken);
        lockSupport.lock(getNodeEngine(), key);
    }

    @Override
    public void lock(String workflowToken, long leaseTime, TimeUnit timeUnit) {
        checkPositive(leaseTime, "leaseTime should be positive");
        setThreadId(workflowToken);
        lockSupport.lock(getNodeEngine(), key, timeUnit.toMillis(leaseTime));
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lockSupport.lockInterruptly(getNodeEngine(), key);
    }

    @Override
    public boolean tryLock(String workflowToken) {
        setThreadId(workflowToken);
        return lockSupport.tryLock(getNodeEngine(), key);
    }

    @Override
    public boolean tryLock(String workflowToken, long time, TimeUnit unit) throws InterruptedException {
        setThreadId(workflowToken);
        checkNotNull(unit, "unit can't be null");

        return lockSupport.tryLock(getNodeEngine(), key, time, unit);
    }

    @Override
    public boolean tryLock(String workflowToken, long time, TimeUnit unit, long leaseTime, TimeUnit leaseUnit) throws
            InterruptedException {
        checkNotNull(unit, "unit can't be null");
        checkNotNull(leaseUnit, "lease unit can't be null");
        setThreadId(workflowToken);
        return lockSupport.tryLock(getNodeEngine(), key, time, unit, leaseTime, leaseUnit);
    }

    @Override
    public void unlock(String workflowToken) {
        setThreadId(workflowToken);
        lockSupport.unlock(getNodeEngine(), key);
    }

    @Override
    public void forceUnlock() {
        lockSupport.forceUnlock(getNodeEngine(), key);
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Use ICondition.newCondition(String name) instead!");
    }

    @Override
    public ICondition newCondition(String name) {
        checkNotNull(name, "Condition name can't be null");
        return new ConditionImpl(this, name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServiceName() {
        return LockService.SERVICE_NAME;
    }

    @Deprecated
    public Object getKey() {
        return getName();
    }

    public Data getKeyData() {
        return key;
    }

    public int getPartitionId() {
        return partitionId;
    }

    ObjectNamespace getNamespace() {
        return lockSupport.getNamespace();
    }

    @Override
    public String toString() {
        return "ILock{name='" + name + '\'' + '}';
    }

    private void setThreadId(String workflowToken) {
        long hash = HashUtil.asLong(workflowToken);
        ThreadUtil.setThreadId(hash);
    }
}
