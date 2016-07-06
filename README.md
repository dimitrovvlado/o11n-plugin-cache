# Cache plug-in for vRealize Orchestrator
The Cache plug-in for vRealize Orchestrator is a a plug-in which provides distributed in-memory data structures such as maps, queues sets etc.

Basic features of the plug-in:

 * Distributed in-memory map with TTL option
 * Distributed in-memory queue with TTL option
 * Unique ID generator
 * Automatic autodiscovery with other vRO nodes which belong to the cluster
 * Peer-to-peer comminication with the other vRO nodes

### Plugin download
[o11nplugin-cache-1.0.0.vmoapp](https://github.com/dimitrovvlado/o11n-plugin-cache/blob/master/dist/o11nplugin-cache.vmoapp?raw=true) 

### Supported platform version
The Cache plug-in supports vRO 6.0 and later.

### Sample scripting

#####Using lists:
```javascript
//Add a value in the default list
CacheManager.listService.add("my-value");

//Get the first value from the list
CacheManager.listService.get(0);

//Clear the default list
CacheManager.listService.clear();
```

#####Using maps:
```javascript
//Put a value in the default map without TTL
CacheManager.mapService.put("my-key", "my-value");

//Put a value in the default map with a TTL of 2 minutes
CacheManager.mapService.put("my-key", "my-other-value", 2, CacheTimeUnit.MINUTES);

//Get the value from the map
var value = CacheManager.mapService.get("my-key");
```

#####Using queues:
```javascript
//Offer a value in the default queue, waiting up to 2 minutes if necessary for space to become available
CacheManager.queueService.offer("my-value", 2, CacheTimeUnit.MINUTES);

//Offer a value in the default queue, waiting if necessary for space to become available
CacheManager.queueService.offer("my-value");

//Poll a value from the default queue
var value = CacheManager.queueService.poll();

//Put a value from the default queue, waiting if necessary for space to become available
CacheManager.queueService.put("my-value");

//Retrieve and remove the head of the queue. Throws an exception if queue is empty
val value = CacheManager.queueService.remove();
```

#####Using ID generators:
```javascript
//Create a unique ID with the default generator
var id = CacheManager.idGeneratorService.newId();

//Create a unique ID with a named ID generator
var id = CacheManager.idGeneratorService.newIdForGenerator("my-id-generator");
```