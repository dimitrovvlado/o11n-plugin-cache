package com.vmware.o11n.plugin.cache;

import ch.dunes.vso.sdk.endpoints.IEndpointConfiguration;
import ch.dunes.vso.sdk.endpoints.IEndpointConfigurationService;
import com.vmware.o11n.plugin.cache.hazelcast.HazelcastInstanceWrapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

public class HazelcastInstanceWrapperTest {

    private HazelcastInstanceWrapper hazelcastInstanceWrapper1;
    private HazelcastInstanceWrapper hazelcastInstanceWrapper2;

    @Test (expected = IllegalStateException.class)
    public void testUnsuccessfulConnect() throws Exception {
        IEndpointConfigurationService configurationService = mock(IEndpointConfigurationService.class);
        IEndpointConfiguration configuration = mock(IEndpointConfiguration.class);
        when(configuration.getString("host")).thenReturn("127.0.0.1");

        hazelcastInstanceWrapper1 = new HazelcastInstanceWrapper(configurationService);
        ReflectionTestUtils.setField(hazelcastInstanceWrapper1, "retries", 10);
        ReflectionTestUtils.setField(hazelcastInstanceWrapper1, "port", 5701);

        when(configurationService.getEndpointConfiguration(anyString())).thenReturn(configuration);

        //Return different configuration versions until the retry limit is hit
        when(configurationService.getVersion()).thenReturn("1").thenReturn("2").thenReturn("3").thenReturn("4")
                .thenReturn("5").thenReturn("6").thenReturn("7").thenReturn("8").thenReturn("9").thenReturn("10")
                .thenReturn("11").thenReturn("12").thenReturn("13").thenReturn("14").thenReturn("15").thenReturn
                ("16").thenReturn("17").thenReturn("18").thenReturn("19").thenReturn("20");

        hazelcastInstanceWrapper1.afterPropertiesSet();
    }

    @Test
    public void testSuccessfulConnect() throws Exception {
        IEndpointConfigurationService configurationService = mock(IEndpointConfigurationService.class);
        IEndpointConfiguration configuration = mock(IEndpointConfiguration.class);
        when(configuration.getString("host")).thenReturn("127.0.0.1");

        hazelcastInstanceWrapper1 = new HazelcastInstanceWrapper(configurationService);
        ReflectionTestUtils.setField(hazelcastInstanceWrapper1, "retries", 10);
        ReflectionTestUtils.setField(hazelcastInstanceWrapper1, "port", 5701);

        when(configurationService.getEndpointConfiguration(anyString())).thenReturn(configuration);

        //Return different configuration versions several times, but not enough to exhaust the retries
        when(configurationService.getVersion()).thenReturn("1").thenReturn("2").thenReturn("3");

        hazelcastInstanceWrapper1.afterPropertiesSet();
    }

    @Test
    public void testSuccessfulConnectOfMultipleInstances() throws Exception {
        final List<String> ids = new LinkedList<>();
        final int version = 1;

        IEndpointConfigurationService configurationService = mock(IEndpointConfigurationService.class);

        final IEndpointConfiguration configuration1 = mock(IEndpointConfiguration.class);
        final IEndpointConfiguration configuration2 = mock(IEndpointConfiguration.class);

        hazelcastInstanceWrapper1 = spy(new HazelcastInstanceWrapper(configurationService));
        ReflectionTestUtils.setField(hazelcastInstanceWrapper1, "retries", 10);
        ReflectionTestUtils.setField(hazelcastInstanceWrapper1, "port", 5701);

        hazelcastInstanceWrapper2 = spy(new HazelcastInstanceWrapper(configurationService));
        ReflectionTestUtils.setField(hazelcastInstanceWrapper2, "retries", 10);
        ReflectionTestUtils.setField(hazelcastInstanceWrapper2, "port", 5701);

        when(configurationService.getEndpointConfiguration(anyString())).thenReturn(null);

        //Mocking the newEndpointConfiguration operations
        when(configurationService.newEndpointConfiguration(anyString())).thenAnswer(new Answer<IEndpointConfiguration>() {
            @Override
            public IEndpointConfiguration answer(InvocationOnMock invocationOnMock) throws Throwable {
                ids.add((String)invocationOnMock.getArguments()[0]);
                return configuration1;
            }
        }).thenAnswer(new Answer<IEndpointConfiguration>() {
            @Override
            public IEndpointConfiguration answer(InvocationOnMock invocationOnMock) throws Throwable {
                ids.add((String)invocationOnMock.getArguments()[0]);
                return configuration2;
            }
        });

        //Mocking the getId operations
        when(configuration1.getId()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return ids.get(0);
            }
        });

        when(configuration2.getId()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return ids.get(1);
            }
        });


        when(configurationService.getEndpointConfigurations()).thenReturn(Collections.singleton(configuration1));

        //Return different configuration versions several times, but not enough to exhaust the retries
        when(configurationService.getVersion()).thenReturn("1").thenReturn("2").thenReturn("3");

        hazelcastInstanceWrapper1.afterPropertiesSet();
        hazelcastInstanceWrapper2.afterPropertiesSet();

        verify(hazelcastInstanceWrapper1, times(1)).destroy();
        verify(hazelcastInstanceWrapper2, times(0)).destroy();
    }

    @After
    public void tearDown() {
        if (hazelcastInstanceWrapper1 != null) {
            hazelcastInstanceWrapper1.destroy();
            hazelcastInstanceWrapper1 = null;
        }
        if (hazelcastInstanceWrapper2 != null) {
            hazelcastInstanceWrapper2.destroy();
            hazelcastInstanceWrapper2 = null;
        }
    }

}
