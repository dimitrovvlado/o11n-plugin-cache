package com.vmware.o11n.plugin.cache.hazelcast;

import com.hazelcast.concurrent.lock.*;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.ObjectNamespace;

import java.util.Collections;
import java.util.Set;

public final class LockStoreProxy implements LockStore {

    private final LockStoreContainer container;
    private final ObjectNamespace namespace;

    public LockStoreProxy(LockStoreContainer container, ObjectNamespace namespace) {
        this.container = container;
        this.namespace = namespace;
    }

    @Override
    public boolean lock(Data key, String caller, long threadId, long referenceId, long leaseTime) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.lock(key, caller, threadId, referenceId, leaseTime);
    }

    @Override
    public boolean txnLock(Data key, String caller, long threadId, long referenceId, long leaseTime, boolean blockReads) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.txnLock(key, caller, threadId, referenceId, leaseTime, blockReads);
    }

    @Override
    public boolean extendLeaseTime(Data key, String caller, long threadId, long leaseTime) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.extendLeaseTime(key, caller, threadId, leaseTime);
    }

    @Override
    public boolean unlock(Data key, String caller, long threadId, long referenceId) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.unlock(key, caller, threadId, referenceId);
    }

    @Override
    public boolean isLocked(Data key) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.isLocked(key);
    }

    @Override
    public boolean isLockedBy(Data key, String caller, long threadId) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.isLockedBy(key, caller, threadId);
    }

    @Override
    public int getLockCount(Data key) {
        LockStore lockStore = getLockStoreOrNull();
        if (lockStore == null) {
            return 0;
        }
        return lockStore.getLockCount(key);
    }

    @Override
    public long getRemainingLeaseTime(Data key) {
        LockStore lockStore = getLockStoreOrNull();
        if (lockStore == null) {
            return 0;
        }
        return lockStore.getRemainingLeaseTime(key);
    }

    @Override
    public boolean canAcquireLock(Data key, String caller, long threadId) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.canAcquireLock(key, caller, threadId);
    }

    //TODO: check this @Override
    public boolean isTransactionallyLocked(Data key) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.isLocked(key);
    }

    @Override
    public Set<Data> getLockedKeys() {
        LockStore lockStore = getLockStoreOrNull();
        if (lockStore == null) {
            return Collections.emptySet();
        }
        return lockStore.getLockedKeys();
    }

    @Override
    public boolean forceUnlock(Data key) {
        LockStore lockStore = getLockStoreOrNull();
        return lockStore != null && lockStore.forceUnlock(key);
    }

    @Override
    public String getOwnerInfo(Data dataKey) {
        LockStore lockStore = getLockStoreOrNull();
        if (lockStore == null) {
            return "<not-locked>";
        }
        return lockStore.getOwnerInfo(dataKey);
    }

    private LockStore getLockStoreOrNull() {
        return container.getLockStore(namespace);
    }

	@Override
	public boolean localLock(Data key, String caller, long threadId, long referenceId, long leaseTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLockedEntryCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean shouldBlockReads(Data key) {
		// TODO Auto-generated method stub
		return false;
	}
}

