package com.vmware.o11n.plugin.cache.finder;

import com.hazelcast.core.Member;
import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import com.vmware.o11n.sdk.modeldriven.FoundObject;
import com.vmware.o11n.sdk.modeldriven.ObjectFinder;
import com.vmware.o11n.sdk.modeldriven.PluginContext;
import com.vmware.o11n.sdk.modeldriven.Sid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class MemberFinder implements ObjectFinder<Member> {

    @Autowired
    private HazelcastInstanceWrapper instanceWrapper;

    @Override
    public Member find(PluginContext pluginContext, String s, Sid sid) {
        List<Member> members = instanceWrapper.getMembers();
        for (Member m : members) {
            if (m.getUuid().equals(sid.getString("uuid"))) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<FoundObject<Member>> query(PluginContext pluginContext, String s, String s1) {
        return Collections.emptyList();
    }

    @Override
    public Sid assignId(Member member, Sid sid) {
        return Sid.empty().with("uuid", member.getUuid());
    }
}
