package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

public interface QueueService {

    boolean offerForQueue(String queueName, String value, long timeout, TimeUnit timeUnit);

    boolean offer(String value, long timeout, TimeUnit timeUnit);

    String pollForQueue(String queueName, long timeout, TimeUnit timeUnit);

    String poll(long timeout, TimeUnit timeUnit);

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     * @param queueName the name of the queue where the value should be put
     * @param value the string value to be put in the queue
     */
    void putForQueue(String queueName, String value);

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     * @param value the string value to be put in the queue
     */
    void put(String value);

    String takeForQueue(String queueName);

    String take();

    void removeForQueue(String queueName);

    void remove();

    int getSizeForQueue(String queueName);

    int getSize();

    int getRemainingCapacityForQueue(String queueName);

    int getRemainingCapacity();
}
