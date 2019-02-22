package com.vmware.o11n.plugin.cache.hazelcast;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import com.hazelcast.concurrent.lock.LockDataSerializerHook;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.spi.Operation;

public class LockReplicationOperation extends Operation
        implements IdentifiedDataSerializable {

    private final Collection<LockStoreImpl> locks = new LinkedList<LockStoreImpl>();

    public LockReplicationOperation() {
    }

    public LockReplicationOperation(LockStoreContainer container, int partitionId, int replicaIndex) {
        this.setPartitionId(partitionId).setReplicaIndex(replicaIndex);

        Collection<LockStoreImpl> lockStores = container.getLockStores();
        for (LockStoreImpl ls : lockStores) {
            if (ls.getTotalBackupCount() < replicaIndex) {
                continue;
            }
            locks.add(ls);
        }
    }

    @Override
    public void run() {
        LockServiceImpl lockService = getService();
        LockStoreContainer container = lockService.getLockContainer(getPartitionId());
        for (LockStoreImpl ls : locks) {
            container.put(ls);
        }
    }

    @Override
    public String getServiceName() {
        return LockServiceImpl.SERVICE_NAME;
    }

    public boolean isEmpty() {
        return locks.isEmpty();
    }

    @Override
    public int getFactoryId() {
        return LockDataSerializerHook.F_ID;
    }

    @Override
    public int getId() {
        return LockDataSerializerHook.LOCK_REPLICATION;
    }

    @Override
    protected void writeInternal(final ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        int len = locks.size();
        out.writeInt(len);
        if (len > 0) {
            for (LockStoreImpl ls : locks) {
                ls.writeData(out);
            }
        }
    }

    @Override
    protected void readInternal(final ObjectDataInput in) throws IOException {
        super.readInternal(in);
        int len = in.readInt();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                LockStoreImpl ls = new LockStoreImpl();
                ls.readData(in);
                locks.add(ls);
            }
        }
    }
}
