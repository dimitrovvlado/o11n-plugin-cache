package com.vmware.o11n.plugin.cache.service;

public interface QueueService {

    public boolean offerForQueue(String queueName, String value);

    public boolean offer(String value);

    public String pollForQueue(String queueName);

    public String poll();

    public void putForQueue(String queueName, String value);

    public void put(String value);

}
