package com.vmware.o11n.plugin.cache.extension;

import com.hazelcast.core.Member;
import com.hazelcast.instance.HazelcastInstanceImpl;
import com.hazelcast.instance.Node;
import com.hazelcast.instance.NodeState;
import com.vmware.o11n.plugin.cache.util.HazelcastUtils;
import com.vmware.o11n.sdk.modeldriven.PluginContext;
import com.vmware.o11n.sdk.modeldriven.extension.Extension;
import com.vmware.o11n.sdk.modeldriven.extension.ExtensionMethod;

import java.lang.reflect.Field;

public class MemberExtension implements Extension<Member>{

    private Member target;
    private PluginContext pluginContext;

    @Override
    public <T extends Member> void setTarget(T target) {
        this.target = target;
    }

    @Override
    public void setContext(PluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    @ExtensionMethod
    public String getDisplayName() {
        String localPostfix = getLocalMember() ? " this" : "";
        return String.format("%s:%d [%s]%s", target.getAddress().getHost(), target.getAddress().getPort(), getState()
                , localPostfix);
    }

    @ExtensionMethod
    public String getHost() {
        return target.getAddress().getHost();
    }

    @ExtensionMethod
    public int getPort() {
        return target.getAddress().getPort();
    }

    @ExtensionMethod
    public String getState() {
        return HazelcastUtils.extractMemberState(target);
    }

    @ExtensionMethod
    public boolean getLocalMember() {
        return HazelcastUtils.extractMemberLocality(target);
    }
}
