package com.vmware.o11n.plugin.cache.service.hazelcast;

import com.vmware.o11n.plugin.cache.model.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.service.QueueService;

@Component
public class HazelcastQueueService implements QueueService {

    @Value("#{systemProperties['cache-plugin.default.queue.name'] ?: 'vro_default_queue'}")
    private String defaultQueueName;

    @Autowired
    private HazelcastInstanceWrapper hazelcastWrapper;

    @Override
    public boolean offerForQueue(String queueName, String value, long timeout, TimeUnit timeUnit) {
        if (timeUnit == null) {
            return hazelcastWrapper.getInstance().getQueue(queueName).offer(value);
        }
        try {
            // Wrap in runtime exception, as the orchestrator doesn't care about
            // the type of the exception
            return hazelcastWrapper.getInstance().getQueue(queueName).offer(value, timeout, timeUnit.convertToConcurrentTimeUnit());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean offer(String value, long timeout, TimeUnit timeUnit) {
        return offerForQueue(defaultQueueName, value, timeout, timeUnit);
    }

    @Override
    public String pollForQueue(String queueName, long timeout, TimeUnit timeUnit) {
        if (timeUnit == null) {
            return (String) hazelcastWrapper.getInstance().getQueue(queueName).poll();
        }
        try {
            // Wrap in runtime exception, as the orchestrator doesn't care about
            // the type of the exception
            return (String) hazelcastWrapper.getInstance().getQueue(queueName).poll(timeout, timeUnit.convertToConcurrentTimeUnit());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String poll(long timeout, TimeUnit timeUnit) {
        return pollForQueue(defaultQueueName, timeout, timeUnit);
    }

    @Override
    public void putForQueue(String queueName, String value) {
        try {
            // Wrap in runtime exception, as the orchestrator doesn't care about
            // the type of the exception
            hazelcastWrapper.getInstance().getQueue(queueName).put(value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(String value) {
        putForQueue(defaultQueueName, value);
    }

    @Override
    public String takeForQueue(String queueName) {
        try {
            // Wrap in runtime exception, as the orchestrator doesn't care about
            // the type of the exception
            return (String)hazelcastWrapper.getInstance().getQueue(queueName).take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String take() {
        return takeForQueue(defaultQueueName);
    }

    @Override
    public String removeForQueue(String queueName) {
        return (String) hazelcastWrapper.getInstance().getQueue(queueName).remove();
    }

    @Override
    public String remove() {
        return removeForQueue(defaultQueueName);
    }

    @Override
    public int sizeForQueue(String queueName) {
        return hazelcastWrapper.getInstance().getQueue(queueName).size();
    }

    @Override
    public int size() {
        return sizeForQueue(defaultQueueName);
    }

    @Override
    public int remainingCapacityForQueue(String queueName) {
        return hazelcastWrapper.getInstance().getQueue(queueName).remainingCapacity();
    }

    @Override
    public int remainingCapacity() {
        return remainingCapacityForQueue(defaultQueueName);
    }
}
