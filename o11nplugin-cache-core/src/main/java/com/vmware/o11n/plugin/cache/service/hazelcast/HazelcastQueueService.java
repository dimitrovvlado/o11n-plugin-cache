package com.vmware.o11n.plugin.cache.service.hazelcast;

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
    public boolean offerForQueue(String queueName, String value) {
        return hazelcastWrapper.getInstance().getQueue(queueName).offer(value);
    }

    @Override
    public boolean offer(String value) {
        return offerForQueue(defaultQueueName, value);
    }

    @Override
    public String pollForQueue(String queueName) {
        return (String) hazelcastWrapper.getInstance().getQueue(queueName).poll();
    }

    @Override
    public String poll() {
        return pollForQueue(defaultQueueName);
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

}
