package com.vmware.o11n.plugin.cache.service.hazelcast;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.service.RingbufferService;
import org.apache.commons.collections.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class HazelcastRingbufferService implements RingbufferService {

    @Value("#{systemProperties['cache-plugin.default.ringbuffer.name'] ?: 'vro_default_ringbuffer'}")
    private String defaultRingbufferName;

    @Autowired
    private HazelcastInstanceWrapper hazelcastWrapper;

    @Override
    public long headSequenceForName(String bufferName) {
        return hazelcastWrapper.getInstance().getRingbuffer(bufferName).headSequence();
    }

    @Override
    public long headSequence() {
        return headSequenceForName(defaultRingbufferName);
    }

    @Override
    public long tailSequenceForName(String bufferName) {
        return hazelcastWrapper.getInstance().getRingbuffer(bufferName).tailSequence();
    }

    @Override
    public long tailSequence() {
        return tailSequenceForName(defaultRingbufferName);
    }

    @Override
    public long addForName(String bufferName, String value) {
        return hazelcastWrapper.getInstance().getRingbuffer(bufferName).add(value);
    }

    @Override
    public long add(String value) {
        return addForName(defaultRingbufferName, value);
    }

    @Override
    public String readOneForName(String bufferName, long sequence) {
        try {
            return (String)hazelcastWrapper.getInstance().getRingbuffer(bufferName).readOne(sequence);
        } catch (InterruptedException e) {
            //TODO log error
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readOne(long sequence) {
        return readOneForName(defaultRingbufferName, sequence);
    }

    @Override
    public long remainingCapacityForName(String bufferName) {
        return hazelcastWrapper.getInstance().getRingbuffer(bufferName).remainingCapacity();
    }

    @Override
    public long remainingCapacity() {
        return remainingCapacityForName(defaultRingbufferName);
    }

    @Override
    public long sizeForName(String bufferName) {
        return hazelcastWrapper.getInstance().getRingbuffer(bufferName).size();
    }

    @Override
    public long size() {
        return sizeForName(defaultRingbufferName);
    }

}
