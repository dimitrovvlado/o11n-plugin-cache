package com.vmware.o11n.plugin.cache.service;

import java.util.List;

public interface ListService {

    boolean addForList(String listName, String value);

    boolean add(String value);

    String getForList(String listName, int index);

    String get(int index);

    int sizeForList(String listName);

    int size();

    int indexOfForList(String listName, String value);

    int indexOf(String value);

    void clearForList(String listName);

    void clear();

    List<String> subListForList(String listName, int fromIndex, int toIndex);

    List<String> subList(int fromIndex, int toIndex);

    boolean removeValueForList(String listName, String value);

    boolean removeValue(String value);

    String removeForList(String listName, int index);

    String remove(int index);
}
