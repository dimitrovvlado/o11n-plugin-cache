package com.vmware.o11n.plugin.cache.service.hazelcast;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HazelcastListService implements ListService {

    @Value("#{systemProperties['cache-plugin.default.list.name'] ?: 'vro_default_list'}")
    private String defaultListName;

    @Autowired
    private HazelcastInstanceWrapper hazelcastWrapper;

    @Override
    public boolean addForList(String listName, String value) {
        return hazelcastWrapper.getInstance().getList(listName).add(value);
    }

    @Override
    public boolean add(String value) {
        return addForList(defaultListName, value);
    }

    @Override
    public String getForList(String listName, int index) {
        return (String) hazelcastWrapper.getInstance().getList(listName).get(index);
    }

    @Override
    public String get(int index) {
        return getForList(defaultListName, index);
    }

    @Override
    public int sizeForList(String listName) {
        return hazelcastWrapper.getInstance().getList(listName).size();
    }

    @Override
    public int size() {
        return sizeForList(defaultListName);
    }

    @Override
    public int indexOfForList(String listName, String value) {
        return hazelcastWrapper.getInstance().getList(listName).indexOf(value);
    }

    @Override
    public int indexOf(String value) {
        return indexOfForList(defaultListName, value);
    }

    @Override
    public void clearForList(String listName) {
        hazelcastWrapper.getInstance().getList(listName).clear();
    }

    @Override
    public void clear() {
        clearForList(defaultListName);
    }

    @Override
    public List<String> subListForList(String listName, int fromIndex, int toIndex) {
        return (List<String>)(List<?>)hazelcastWrapper.getInstance().getList(listName).subList(fromIndex, toIndex);
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        return subListForList(defaultListName, fromIndex, toIndex);
    }

    @Override
    public boolean removeValueForList(String listName, String value) {
        return hazelcastWrapper.getInstance().getList(listName).remove(value);
    }

    @Override
    public boolean removeValue(String value) {
        return removeValueForList(defaultListName, value);
    }

    @Override
    public String removeForList(String listName, int index) {
        return (String) hazelcastWrapper.getInstance().getList(listName).remove(index);
    }

    @Override
    public String remove(int index) {
        return removeForList(defaultListName, index);
    }

}
