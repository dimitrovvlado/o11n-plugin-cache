package com.vmware.o11n.plugin.cache.hazelcast;

import com.hazelcast.concurrent.lock.LockResource;
import com.hazelcast.concurrent.lock.LockStore;
import com.hazelcast.concurrent.lock.LockStoreInfo;
import com.hazelcast.spi.ObjectNamespace;
import com.hazelcast.spi.SharedService;
import com.hazelcast.util.ConstructorFunction;

import java.util.Collection;

public interface LockService extends SharedService {

    String SERVICE_NAME = "o11n:impl:lockService";

    void registerLockStoreConstructor(String serviceName,
                                      ConstructorFunction<ObjectNamespace, LockStoreInfo> constructorFunction);

    LockStore createLockStore(int partitionId, ObjectNamespace namespace);

    void clearLockStore(int partitionId, ObjectNamespace namespace);

    Collection<LockResource> getAllLocks();

    long getMaxLeaseTimeInMillis();
}
