package com.vmware.o11n.plugin.cache.singleton;

import com.vmware.o11n.plugin.cache.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class CacheManager {

    @Autowired
    private ListService listService;

    @Autowired
    private SetService setService;

    @Autowired
    private MapService mapService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private RingbufferService ringbufferService;

    public ListService getListService() {
        return listService;
    }

    public SetService getSetService() {
        return setService;
    }

    public MapService getMapService() {
        return mapService;
    }

    public QueueService getQueueService() {
        return queueService;
    }

    public RingbufferService getRingbufferService() {
        return ringbufferService;
    }

}
