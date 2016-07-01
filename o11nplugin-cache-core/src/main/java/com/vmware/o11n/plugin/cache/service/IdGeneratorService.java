package com.vmware.o11n.plugin.cache.service;

public interface IdGeneratorService {

    public long newIdForGenerator(String generatorName);

    public long newId();

}
