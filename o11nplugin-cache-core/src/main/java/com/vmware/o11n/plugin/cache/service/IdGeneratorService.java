package com.vmware.o11n.plugin.cache.service;

/**
 * Service for working with cluster-wide unique IDs.
 */
public interface IdGeneratorService {

    /**
     * Generates and returns a cluster-wide unique id. Generated ids are guaranteed to be unique for the entire
     * cluster as long as the cluster is live. If the cluster restarts, then id generation will start from 0.
     *
     * @param generatorName name of the generator.
     *
     * @return the cluster-wide new unique id.
     */
    long newIdForGenerator(String generatorName);

    /**
     * Generates and returns a cluster-wide unique id. Generated ids are guaranteed to be unique for the entire
     * cluster as long as the cluster is live. If the cluster restarts, then id generation will start from 0.
     *
     * @return the cluster-wide new unique id.
     */
    long newId();

}
