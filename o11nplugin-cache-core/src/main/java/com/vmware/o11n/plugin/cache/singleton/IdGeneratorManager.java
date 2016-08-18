package com.vmware.o11n.plugin.cache.singleton;

import com.vmware.o11n.plugin.cache.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
public class IdGeneratorManager<E extends Serializable> {

    @Autowired
    private IdGeneratorService idGeneratorService;

    public IdGeneratorService getIdGeneratorService() {
        return idGeneratorService;
    }

}
