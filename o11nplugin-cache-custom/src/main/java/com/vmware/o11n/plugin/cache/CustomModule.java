package com.vmware.o11n.plugin.cache;



import com.vmware.o11n.sdk.modeldrivengen.mapping.*;

import com.vmware.o11n.sdk.modeldrivengen.model.*;
import com.google.inject.*;
import java.util.*;

public class CustomModule extends AbstractModule {

    private final Plugin plugin;

    @Override
    protected void configure() {
        bind(AbstractMapping.class).toInstance(new CustomMapping());
        bind(Plugin.class).toInstance(plugin);
        
    }

    public CustomModule() {
        this.plugin = new Plugin();

        plugin.setApiPrefix("Cache");
        plugin.setIcon("default-32x32.png");
        plugin.setDescription("Cache");
        plugin.setDisplayName("Cache");
        plugin.setName("Cache");
        plugin.setPackages(Collections.singletonList("o11nplugin-example-package-${project.version}.package"));
        plugin.setAdaptorClassName(com.vmware.o11n.plugin.cache.CachePluginAdaptor.class);
    }
}