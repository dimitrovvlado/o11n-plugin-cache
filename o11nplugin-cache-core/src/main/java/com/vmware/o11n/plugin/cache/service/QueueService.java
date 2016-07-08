package com.vmware.o11n.plugin.cache.service;

import com.vmware.o11n.plugin.cache.model.TimeUnit;

/**
 * Service for working with distributed queue instances.
 */
public interface QueueService {

    /**
     * Inserts the specified element into the specified queue, waiting up to the specified wait time if necessary for
     * space to become available. If {@code timeUnit} is not provided, no timeout will be set.
     *
     * @param queueName name of the queue.
     * @param value the element to add.
     * @param timeout how long to wait before giving up.
     * @param timeUnit time unit for the {@code timeout}.
     *
     * @return {@code true} if successful, or {@code false} if the specified waiting time elapses before space is
     * available
     */
    boolean offerForQueue(String queueName, String value, long timeout, TimeUnit timeUnit);

    /**
     * Inserts the specified element into the default queue, waiting up to the specified wait time if necessary for
     * space to become available. If {@code timeUnit} is not provided, no timeout will be set.
     *
     * @param value the element to add.
     * @param timeout how long to wait before giving up.
     * @param timeUnit time unit for the {@code timeout}.
     *
     * @return {@code true} if successful, or {@code false} if the specified waiting time elapses before space is
     * available
     */
    boolean offer(String value, long timeout, TimeUnit timeUnit);

    /**
     * Retrieves and removes the head of the specified queue, waiting up to the specified wait time if necessary for an
     * element to become available.
     *
     * @param queueName name of the queue.
     * @param timeout how long to wait before giving up.
     * @param timeUnit time unit for the {@code timeout}.
     *
     * @return the head of this queue, or {@code null} if the specified waiting time elapses before an element is
     * available
     */
    String pollForQueue(String queueName, long timeout, TimeUnit timeUnit);

    /**
     * Retrieves and removes the head of the default queue, waiting up to the specified wait time if necessary for an
     * element to become available.
     *
     * @param timeout how long to wait before giving up.
     * @param timeUnit time unit for the {@code timeout}.
     *
     * @return he head of this queue, or {@code null} if the specified waiting time elapses before an element is
     * available
     */
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

    /**
     * Retrieves and removes the head of the specified queue, waiting if necessary until an element becomes available.
     *
     * @param queueName name of the queue.
     *
     * @return the head of the specified queue.
     */
    String takeForQueue(String queueName);

    /**
     * Retrieves and removes the head of the default queue, waiting if necessary until an element becomes available.
     *
     * @return the head of the default queue.
     */
    String take();

    /**
     * Removes a single instance of the specified element from the specified queue, if it is present.
     *
     * @param queueName name of the queue.
     *
     * @return the element.
     */
    String removeForQueue(String queueName);

    /**
     * Removes a single instance of the specified element from the default queue, if it is present.
     *
     * @return the element.
     */
    String remove();

    /**
     * Returns the number of elements in the specified collection.
     *
     * @param queueName name of the queue.
     *
     * @return the size of the collection.
     */
    int sizeForQueue(String queueName);

    /**
     * Returns the number of elements in the default collection.
     *
     * @return the size of the collection.
     */
    int size();

    /**
     * Returns the number of additional elements that the default queue can ideally (in the absence of memory or
     * resource constraints) accept without blocking.
     *
     * @param queueName name of the queue.
     *
     * @return the remaining capacity.
     */
    int remainingCapacityForQueue(String queueName);

    /**
     * Returns the number of additional elements that the default queue can ideally (in the absence of memory or
     * resource constraints) accept without blocking.
     *
     * @return the remaining capacity.
     */
    int remainingCapacity();
}
