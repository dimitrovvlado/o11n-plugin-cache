package com.vmware.o11n.plugin.cache.service;

public interface SetService {
    boolean addForSet(String setName, String value);

    boolean add(String value);

    boolean removeForSet(String setName, String value);

    boolean remove(String value);

    void clearForSet(String setName);

    void clear();

    String[] elementsForSet(String setName);

    String[] elements();

    boolean containsForSet(String setName, String value);

    boolean contains(String value);

    int sizeForSet(String setName);

    int size();
}
