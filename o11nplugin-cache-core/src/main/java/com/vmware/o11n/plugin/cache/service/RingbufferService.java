package com.vmware.o11n.plugin.cache.service;

import com.hazelcast.ringbuffer.OverflowPolicy;

import java.util.List;

public interface RingbufferService {

    /**
     * Returns the sequence of the head. The head is the side of the ringbuffer where the oldest items in the
     * ringbuffer are found.
     *
     * If the RingBuffer is empty, the head will be one more than the tail.
     *
     * The initial value of the head is 0 (1 more than tail).
     *
     * @param bufferName name of the buffer.
     *
     * @return the sequence of the head.
     */
    long headSequenceForName(String bufferName);

    /**
     * Returns the sequence of the head. The head is the side of the ringbuffer where the oldest items in the
     * ringbuffer are found.
     *
     * If the RingBuffer is empty, the head will be one more than the tail.
     *
     * The initial value of the head is 0 (1 more than tail).
     *
     * @return the sequence of the head.
     */
    long headSequence();

    /**
     * Returns the sequence of the tail. The tail is the side of the ringbuffer where the items are added to.
     *
     * The initial value of the tail is -1.
     *
     * @param bufferName name of the buffer.
     *
     * @return the sequence of the tail.
     */
    long tailSequenceForName(String bufferName);

    /**
     * Returns the sequence of the tail. The tail is the side of the ringbuffer where the items are added to.
     *
     * The initial value of the tail is -1.
     *
     * @return the sequence of the tail.
     */
    long tailSequence();

    /**
     * Adds an item to the tail of the Ringbuffer. If there is no space in the Ringbuffer, the add will overwrite the
     * oldest item in the ringbuffer no matter what the ttl is. For more control on this behavior.
     *
     * The returned value is the sequence of the added item. Using this sequence you can read the added item.
     *
     * @param bufferName name of the buffer.
     *
     * @param value the item to add.
     *
     * @return the sequence of the added item.
     */
    long addForName(String bufferName, String value);

    /**
     * Adds an item to the tail of the Ringbuffer. If there is no space in the Ringbuffer, the add will overwrite the
     * oldest item in the ringbuffer no matter what the ttl is. For more control on this behavior.
     *
     * The returned value is the sequence of the added item. Using this sequence you can read the added item.
     *
     * @param value the item to add.
     *
     * @return the sequence of the added item.
     */
    long add(String value);

    /**
     * Reads one item from the Ringbuffer.
     *
     * If the sequence is one beyond the current tail, this call blocks until an item is added.
     *
     * @param bufferName name of the buffer.
     *
     * @param sequence the sequence of the item to read.
     *
     * @return the read item.
     */
    String readOneForName(String bufferName, long sequence);

    /**
     * Reads one item from the Ringbuffer.
     *
     * If the sequence is one beyond the current tail, this call blocks until an item is added.
     *
     * @param sequence the sequence of the item to read.
     *
     * @return the read item.
     */
    String readOne(long sequence);

    /**
     * Returns the remaining capacity of the ringbuffer.
     *
     * The returned value could be stale as soon as it is returned.
     *
     * @param bufferName name of the buffer.
     *
     * @return the remaining capacity.
     */
    long remainingCapacityForName(String bufferName);

    /**
     * Returns the remaining capacity of the ringbuffer.
     *
     * The returned value could be stale as soon as it is returned.
     *
     * @return the remaining capacity.
     */
    long remainingCapacity();

    /**
     * Returns number of items in the ringbuffer.
     *
     * If no ttl is set, the size will always be equal to capacity after the head completed the first loop around the
     * ring. This is because no items are getting retired.
     *
     * @param bufferName name of the buffer.
     *
     * @return the size.
     */
    long sizeForName(String bufferName);

    /**
     * Returns number of items in the ringbuffer.
     *
     * If no ttl is set, the size will always be equal to capacity after the head completed the first loop around the
     * ring. This is because no items are getting retired.
     *
     * @return the size.
     */
    long size();
}
