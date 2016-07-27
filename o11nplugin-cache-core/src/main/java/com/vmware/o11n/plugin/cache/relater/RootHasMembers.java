package com.vmware.o11n.plugin.cache.relater;

import com.hazelcast.core.Member;
import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.sdk.modeldriven.ObjectRelater;
import com.vmware.o11n.sdk.modeldriven.PluginContext;
import com.vmware.o11n.sdk.modeldriven.Sid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RootHasMembers implements ObjectRelater<Member> {

    @Autowired
    private HazelcastInstanceWrapper instanceWrapper;

    @Override
    public List<Member> findChildren(PluginContext pluginContext, String s, String s1, Sid sid) {
        return instanceWrapper.getMembers();
    }
}
