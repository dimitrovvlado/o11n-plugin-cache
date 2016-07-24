package com.vmware.o11n.plugin.cache.hazelcast;

import com.hazelcast.concurrent.lock.LockEvictionProcessor;
import com.hazelcast.concurrent.lock.LockStoreInfo;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.ObjectNamespace;
import com.hazelcast.util.ConcurrencyUtil;
import com.hazelcast.util.ConstructorFunction;
import com.hazelcast.util.scheduler.EntryTaskScheduler;
import com.hazelcast.util.scheduler.EntryTaskSchedulerFactory;
import com.hazelcast.util.scheduler.ScheduleType;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;

public final class LockStoreContainer {

    private final LockServiceImpl lockService;
    private final int partitionId;

    private final ConcurrentMap<ObjectNamespace, LockStoreImpl> lockStores =
            new ConcurrentHashMap<ObjectNamespace, LockStoreImpl>();
    private final ConstructorFunction<ObjectNamespace, LockStoreImpl> lockStoreConstructor =
            new ConstructorFunction<ObjectNamespace, LockStoreImpl>() {
                public LockStoreImpl createNew(ObjectNamespace namespace) {
                    final ConstructorFunction<ObjectNamespace, LockStoreInfo> ctor =
                            lockService.getConstructor(namespace.getServiceName());
                    if (ctor != null) {
                        LockStoreInfo info = ctor.createNew(namespace);
                        if (info != null) {
                            int backupCount = info.getBackupCount();
                            int asyncBackupCount = info.getAsyncBackupCount();
                            EntryTaskScheduler entryTaskScheduler = createScheduler(namespace);
                            return new LockStoreImpl(lockService, namespace, entryTaskScheduler, backupCount, asyncBackupCount);
                        }
                    }
                    throw new IllegalArgumentException("No LockStore constructor is registered!");
                }
            };

    public LockStoreContainer(LockServiceImpl lockService, int partitionId) {
        this.lockService = lockService;
        this.partitionId = partitionId;
    }

    void clearLockStore(ObjectNamespace namespace) {
        LockStoreImpl lockStore = lockStores.remove(namespace);
        if (lockStore != null) {
            lockStore.clear();
        }
    }

    LockStoreImpl getOrCreateLockStore(ObjectNamespace namespace) {
        return ConcurrencyUtil.getOrPutIfAbsent(lockStores, namespace, lockStoreConstructor);
    }

    LockStoreImpl getLockStore(ObjectNamespace namespace) {
        return lockStores.get(namespace);
    }

    public Collection<LockStoreImpl> getLockStores() {
        return Collections.unmodifiableCollection(lockStores.values());
    }

    void clear() {
        for (LockStoreImpl lockStore : lockStores.values()) {
            lockStore.clear();
        }
        lockStores.clear();
    }

    int getPartitionId() {
        return partitionId;
    }

    public void put(LockStoreImpl ls) {
        ls.setLockService(lockService);
        EntryTaskScheduler entryTaskScheduler = createScheduler(ls.getNamespace());
        ls.setEntryTaskScheduler(entryTaskScheduler);
        lockStores.put(ls.getNamespace(), ls);
    }

    private EntryTaskScheduler createScheduler(ObjectNamespace namespace) {
        NodeEngine nodeEngine = lockService.getNodeEngine();
        LockEvictionProcessor entryProcessor = new LockEvictionProcessor(nodeEngine, namespace);
        ScheduledExecutorService scheduledExecutor =
                nodeEngine.getExecutionService().getDefaultScheduledExecutor();
        return EntryTaskSchedulerFactory
                .newScheduler(scheduledExecutor, entryProcessor, ScheduleType.FOR_EACH);
    }
}