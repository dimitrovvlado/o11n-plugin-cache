package com.vmware.o11n.plugin.cache.service.hazelcast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.plugin.cache.service.IdGeneratorService;

@Component
public class HazelcastIdGeneratorService implements IdGeneratorService {

    @Value("#{systemProperties['cache-plugin.default.generator.name'] ?: 'vro_default_id_generator'}")
    private String defaultIdGeneratorName;

    @Autowired
    private HazelcastInstanceWrapper hazelcastWrapper;

    @Override
    public long newIdForGenerator(String generatorName) {
        return hazelcastWrapper.getInstance().getIdGenerator(generatorName).newId();
    }

    @Override
    public long newId() {
        return newIdForGenerator(defaultIdGeneratorName);
    }
}
