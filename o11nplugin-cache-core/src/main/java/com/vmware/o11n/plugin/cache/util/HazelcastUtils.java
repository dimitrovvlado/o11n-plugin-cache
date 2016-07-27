package com.vmware.o11n.plugin.cache.util;

import com.hazelcast.core.Member;
import com.hazelcast.instance.HazelcastInstanceImpl;
import com.hazelcast.instance.Node;
import com.hazelcast.instance.NodeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

/**
 * Utility class for extending the Hazelcast framework.
 */
public class HazelcastUtils {

    private static final Logger log = LoggerFactory.getLogger(HazelcastUtils.class);

    /**
     * Utility method for extracting the state of a cluster member.
     *
     * @param target the member from the cluster, cannot be null.
     *
     * @return the state of the member or {@code unknown} if the method fails.
     */
    public static String extractMemberState(Member target) {
        Assert.notNull(target, "member cannot be null.");
        try {
            HazelcastInstanceImpl instance = (HazelcastInstanceImpl) getPrivateField(target, "instance");
            Node node = (Node) getPrivateField(instance, "node");
            NodeState state = node.getState();
            return state.toString();
        } catch (ReflectiveOperationException e) {
            return "unknown";
        }
    }

    /**
     * Utility method for extracting the locality of a member.
     *
     * @param target the member from the cluster, cannot be null.
     *
     * @return {@code true} if the the provided member is the local one, {@code false} otherwise.
     */
    public static boolean extractMemberLocality(Member target) {
        Assert.notNull(target, "member cannot be null.");
        try {
            return (Boolean)getPrivateField(target, "localMember");
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    private static Object getPrivateField(Object obj, String fieldName) throws ReflectiveOperationException {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (ReflectiveOperationException e) {
            log.debug("Error extracting private field '{}' from object of type '{}'.", fieldName, obj.getClass());
            throw e;
        }
    }

}
