package com.vmware.o11n.plugin.cache.service.hazelcast;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.service.SetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class HazelcastSetService implements SetService {

    @Value("#{systemProperties['cache-plugin.default.set.name'] ?: 'vro_set_queue'}")
    private String defaultSetName;

    @Autowired
    private HazelcastInstanceWrapper hazelcastWrapper;

    @Override
    public boolean addForSet(String setName, String value) {
        return hazelcastWrapper.getInstance().getSet(setName).add(value);
    }

    @Override
    public boolean add(String value) {
        return addForSet(defaultSetName, value);
    }

    @Override
    public boolean removeForSet(String setName, String value) {
        return hazelcastWrapper.getInstance().getSet(setName).remove(value);
    }

    @Override
    public boolean remove(String value) {
        return removeForSet(defaultSetName, value);
    }

    @Override
    public void clearForSet(String setName) {
        hazelcastWrapper.getInstance().getSet(setName).clear();
    }

    @Override
    public void clear() {
        clearForSet(defaultSetName);
    }

    @Override
    public String[] elementsForSet(String setName) {
        Object[] objects = hazelcastWrapper.getInstance().getSet(setName).toArray();
        String[] elements = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            elements[i] = objects[i].toString();
        }
        return elements;
    }

    @Override
    public String[] elements() {
        return elementsForSet(defaultSetName);
    }

    @Override
    public boolean containsForSet(String setName, String value) {
        return hazelcastWrapper.getInstance().getSet(setName).contains(value);
    }

    @Override
    public boolean contains(String value) {
        return containsForSet(defaultSetName, value);
    }

    @Override
    public int sizeForSet(String setName) {
        return hazelcastWrapper.getInstance().getSet(setName).size();
    }

    @Override
    public int size() {
        return sizeForSet(defaultSetName);
    }
}
