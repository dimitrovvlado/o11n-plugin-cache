package com.vmware.o11n.plugin.cache;

import org.junit.Assert;
import org.junit.Test;

import com.vmware.o11n.plugin.cache.hazelcast.model.TimeUnit;

public class TimeUnitTest {

	@Test
	public void testTimeUnitConvertion() {
		java.util.concurrent.TimeUnit converted = TimeUnit.HOURS.convertToConcurrentTimeUnit();
		Assert.assertEquals(converted, java.util.concurrent.TimeUnit.HOURS);
		
		converted = TimeUnit.DAYS.convertToConcurrentTimeUnit();
		Assert.assertEquals(converted, java.util.concurrent.TimeUnit.DAYS);
		
		converted = TimeUnit.MINUTES.convertToConcurrentTimeUnit();
		Assert.assertEquals(converted, java.util.concurrent.TimeUnit.MINUTES);
		
		converted = TimeUnit.SECONDS.convertToConcurrentTimeUnit();
		Assert.assertEquals(converted, java.util.concurrent.TimeUnit.SECONDS);
	}
	
}
