package com.vmware.o11n.plugin.cache.service;

public interface IdGeneratorService {

    long newIdForGenerator(String generatorName);

    long newId();

}
