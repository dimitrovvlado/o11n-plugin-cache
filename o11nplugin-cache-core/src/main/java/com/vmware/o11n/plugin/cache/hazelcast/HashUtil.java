package com.vmware.o11n.plugin.cache.hazelcast;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.springframework.util.Assert;

public class HashUtil {


    /**
     * Evaluate the hash of the workflow token id.
     * @param workflowToken the workflow token id, cannot be null.
     * @return the long representation of the workflow token id.
     */
    public static long asLong(String workflowToken) {
        Assert.notNull(workflowToken);
        Hasher hasher = Hashing.murmur3_128().newHasher();
        hasher.putString(workflowToken, Charsets.UTF_8);
        HashCode hash = hasher.hash();
        return hash.asLong();
    }
}
