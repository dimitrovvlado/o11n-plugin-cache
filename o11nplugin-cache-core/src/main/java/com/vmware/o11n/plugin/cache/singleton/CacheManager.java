package com.vmware.o11n.plugin.cache.singleton;

import com.vmware.o11n.plugin.cache.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vmware.o11n.plugin.cache.service.IdGeneratorService;
import com.vmware.o11n.plugin.cache.service.MapService;
import com.vmware.o11n.plugin.cache.service.QueueService;

@Component
@Scope(value = "prototype")
public class CacheManager {

    @Autowired
    private ListService listService;

    @Autowired
    private MapService mapService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    public ListService getListService() {
        return listService;
    }

    public MapService getMapService() {
        return mapService;
    }

    public QueueService getQueueService() {
        return queueService;
    }

    public IdGeneratorService getIdGeneratorService() {
        return idGeneratorService;
    }

}
