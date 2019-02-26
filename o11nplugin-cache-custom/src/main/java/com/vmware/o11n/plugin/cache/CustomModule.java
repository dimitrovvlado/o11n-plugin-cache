package com.vmware.o11n.plugin.cache;



import com.google.inject.name.Names;
import com.vmware.o11n.sdk.modeldrivengen.mapping.*;

import com.vmware.o11n.sdk.modeldrivengen.model.*;
import com.google.inject.*;

import java.io.File;
import java.util.*;

public class CustomModule extends AbstractModule {

    private final Plugin plugin;

    @Override
    protected void configure() {
        File plugInSources = new File("o11nplugin-cache-core/src/main/java");

        bind(new TypeLiteral<List<File>>() {}).annotatedWith(Names.named("javadoc.locations")).toInstance(Arrays
                .asList(plugInSources));

        bind(AbstractMapping.class).toInstance(new CustomMapping());
        bind(Plugin.class).toInstance(plugin);


    }

    public CustomModule() {
        this.plugin = new Plugin();

        plugin.setApiPrefix("Cache");
        plugin.setIcon("cache.png");
        plugin.setDescription("Cache");
        plugin.setDisplayName("Cache");
        plugin.setName("Cache");
        plugin.setPackages(Collections.singletonList("o11nplugin-cache-package-${project.version}.package"));
        plugin.setAdaptorClassName(com.vmware.o11n.plugin.cache.CachePluginAdaptor.class);
    }
}