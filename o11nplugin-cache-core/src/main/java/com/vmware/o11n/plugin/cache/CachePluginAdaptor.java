package com.vmware.o11n.plugin.cache;

import com.vmware.o11n.sdk.modeldriven.AbstractModelDrivenAdaptor;

public class CachePluginAdaptor extends AbstractModelDrivenAdaptor {
    private static final String[] CONFIG_LOCATIONS = { "classpath:com/vmware/o11n/plugin/cache/plugin.xml" };

    private static final String RUNTIME_PROPERTIES_LOCATION = "com/vmware/o11n/plugin/cache_gen/runtime-config.properties";

    @Override
    protected String[] getConfigLocations() {
        return CONFIG_LOCATIONS;
    }

    @Override
    protected String getRuntimeConfigurationPath() {
        return RUNTIME_PROPERTIES_LOCATION;
    }
}