package com.vmware.o11n.plugin.cache.hazelcast;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class ConditionInfo implements DataSerializable {

    private String conditionId;
    private Map<ConditionInfo.ConditionWaiter, ConditionInfo.ConditionWaiter> waiters = new HashMap<ConditionInfo.ConditionWaiter, ConditionInfo.ConditionWaiter>(2);

    public ConditionInfo() {
    }

    public ConditionInfo(String conditionId) {
        this.conditionId = conditionId;
    }

    public boolean addWaiter(String caller, long threadId) {
        ConditionInfo.ConditionWaiter waiter = new ConditionInfo.ConditionWaiter(caller, threadId);
        return waiters.put(waiter, waiter) == null;
    }

    public boolean removeWaiter(String caller, long threadId) {
        ConditionInfo.ConditionWaiter waiter = new ConditionInfo.ConditionWaiter(caller, threadId);
        return waiters.remove(waiter) != null;
    }

    public String getConditionId() {
        return conditionId;
    }

    public int getAwaitCount() {
        return waiters.size();
    }

    public boolean startWaiter(String caller, long threadId) {
        ConditionInfo.ConditionWaiter key = new ConditionInfo.ConditionWaiter(caller, threadId);
        ConditionInfo.ConditionWaiter waiter = waiters.get(key);
        if (waiter == null) {
            throw new IllegalStateException();
        }

        if (waiter.started) {
            return false;
        } else {
            waiter.started = true;
            return true;
        }
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(conditionId);
        int len = waiters.size();
        out.writeInt(len);
        if (len > 0) {
            for (ConditionInfo.ConditionWaiter w : waiters.values()) {
                out.writeUTF(w.caller);
                out.writeLong(w.threadId);
            }
        }
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        conditionId = in.readUTF();
        int len = in.readInt();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                ConditionInfo.ConditionWaiter waiter = new ConditionInfo.ConditionWaiter(in.readUTF(), in.readLong());
                waiters.put(waiter, waiter);
            }
        }
    }

    private static class ConditionWaiter {
        private final String caller;
        private final long threadId;
        private transient boolean started;

        ConditionWaiter(String caller, long threadId) {
            this.caller = caller;
            this.threadId = threadId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ConditionInfo.ConditionWaiter that = (ConditionInfo.ConditionWaiter) o;

            if (threadId != that.threadId) {
                return false;
            }
            if (caller != null ? !caller.equals(that.caller) : that.caller != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = caller != null ? caller.hashCode() : 0;
            result = 31 * result + (int) (threadId ^ (threadId >>> 32));
            return result;
        }
    }
}
