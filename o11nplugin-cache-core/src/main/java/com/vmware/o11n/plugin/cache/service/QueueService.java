package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

public interface QueueService {

    public boolean offerForQueue(String queueName, String value, long timeout, TimeUnit timeUnit);

    public boolean offer(String value, long timeout, TimeUnit timeUnit);

    public String pollForQueue(String queueName, long timeout, TimeUnit timeUnit);

    public String poll(long timeout, TimeUnit timeUnit);

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     * @param queueName the name of the queue where the value should be put
     * @param value the string value to be put in the queue
     */
    public void putForQueue(String queueName, String value);

    /**
     * Inserts the specified element into this queue, waiting if necessary
     * for space to become available.
     * @param value the string value to be put in the queue
     */
    public void put(String value);

    public String takeForQueue(String queueName);

    public String take();

    public void removeForQueue(String queueName);

    public void remove();

    public int getSizeForQueue(String queueName);

    public int getSize();

    public int getRemainingCapacityForQueue(String queueName);

    public int getRemainingCapacity();
}
