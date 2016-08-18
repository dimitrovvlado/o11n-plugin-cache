package com.vmware.o11n.plugin.cache.singleton;

import com.vmware.o11n.plugin.cache.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class LockManager {

    @Autowired
    private LockService lockService;

    public LockService getLockService() {
        return lockService;
    }
}
