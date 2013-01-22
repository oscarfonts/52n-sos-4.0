/**
 * Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.ds.hibernate;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.sos.cache.CapabilitiesCache;
import org.n52.sos.ds.hibernate.SosCacheFeederDAO.CacheCreationStrategy;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.test.AbstractSosTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class SosCacheFeederDAOTest extends AbstractSosTestCase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SosCacheFeederDAOTest.class);
	private static SosCacheFeederDAO instance;
	
	@BeforeClass
	public static void init()
	{
		instance = new SosCacheFeederDAO(); 
	}
	
	@Test
	public void constructorReturnsInstance()
	{
		assertNotNull("instance returned by constructor", instance);
	}
	
	@Test
	public void updateCacheFillsCapabilitiesCache() throws OwsExceptionReport
	{
		CapabilitiesCache cache = new CapabilitiesCache();
		instance.updateCache(cache);
		testCacheResult(cache);
	}

	private void testCacheResult(CapabilitiesCache cache)
	{
		assertNotNull("cache is null", cache);
		assertNotNull("envelope of features is null",cache.getGlobalEnvelope());
		assertNotNull("feature types is null",cache.getFeatureOfInterestTypes());
		assertFalse("feature types is empty",cache.getFeatureOfInterestTypes().isEmpty());
		assertNotNull("offering envelopes map is null",cache.getKOfferingVEnvelope());
		assertNotNull("offering max times map is null",cache.getKOfferingVMaxTime());
		assertNotNull("offering min times map is null",cache.getKOfferingVMinTime());
		assertNotNull("max event time is null",cache.getMaxEventTime());
		assertNotNull("min event time is null",cache.getMinEventTime());
		assertNotNull("observation types is null",cache.getObservationTypes());
		assertFalse("observation types is emtpy",cache.getObservationTypes().isEmpty());
		assertNotNull("result templates is null",cache.getResultTemplates());
	}
	
	@Test(expected=OwsExceptionReport.class) 
	public void updateNullThrowsOwsExceptionReport() throws OwsExceptionReport
	{
		instance.updateCache(null);
	}
	
	@Test
	public void updateCacheWithStrategyNullFillsCapabilitiesCache() throws OwsExceptionReport
	{
		CapabilitiesCache cache = new CapabilitiesCache();
		instance.updateCache(cache, null);
		testCacheResult(cache);
	}
	
	@Test
	public void updateCacheUsingComplexDBQueriesFillsCapabilitiesCache() throws OwsExceptionReport
	{
		CapabilitiesCache cache = new CapabilitiesCache();
		instance.updateCache(cache, CacheCreationStrategy.COMPLEX_DB_QUERIES);
		testCacheResult(cache);
	}
	
	@Test
	public void updateCacheUsingMultiThreadingFillsCapabilitiesCache() throws OwsExceptionReport
	{
		CapabilitiesCache cache = new CapabilitiesCache();
		instance.updateCache(cache, CacheCreationStrategy.MULTI_THREAD);
		testCacheResult(cache);
	}
	
	@Test 
	public void strategyMultiEqualsSingle() throws OwsExceptionReport
	{
		CapabilitiesCache cacheSingleThread = new CapabilitiesCache(),
				cacheMultithread = new CapabilitiesCache();
		instance.updateCache(cacheSingleThread, CacheCreationStrategy.SINGLE_THREAD);
		instance.updateCache(cacheMultithread, CacheCreationStrategy.MULTI_THREAD);
		assertEquals("single != multi threaded",cacheSingleThread, cacheMultithread);
	}
	
	@Test 
	public void strategyComplexDBQueriesEqualsSingle() throws OwsExceptionReport
	{
		CapabilitiesCache cacheSingleThread = new CapabilitiesCache(),
				cacheComplexDBQueries = new CapabilitiesCache();
		instance.updateCache(cacheSingleThread, CacheCreationStrategy.SINGLE_THREAD);
		instance.updateCache(cacheComplexDBQueries, CacheCreationStrategy.COMPLEX_DB_QUERIES);
		assertEquals("single != complex db queries",cacheSingleThread, cacheComplexDBQueries);
	}
	
	@Test
	public void strategyMultiEqualsComplexDBQueries() throws OwsExceptionReport
	{
		CapabilitiesCache cacheComplexDBQueries = new CapabilitiesCache(),
				cacheMultithread = new CapabilitiesCache();
		instance.updateCache(cacheComplexDBQueries, CacheCreationStrategy.COMPLEX_DB_QUERIES);
		instance.updateCache(cacheMultithread, CacheCreationStrategy.MULTI_THREAD);
		assertEquals("multi threaded != complex db queries",cacheMultithread,cacheComplexDBQueries);
	}
	
	@Test
	public void runTimeComparisonBetweenSingleAndMultiThread() throws OwsExceptionReport
	{
		int times = 10;
		long[] singleThreadTimes = new long[10], multiThreadTimes = new long[10];
		for (int i = 0; i < times; i++) {
			long singleThreadStart, multiThreadStart, singleThreadEnd, multiThreadEnd;
			singleThreadStart = System.currentTimeMillis();
			CapabilitiesCache cache = new CapabilitiesCache();
			instance.updateCache(cache, CacheCreationStrategy.SINGLE_THREAD);
			singleThreadEnd = System.currentTimeMillis();
			cache = new CapabilitiesCache();
			multiThreadStart = System.currentTimeMillis();
			instance.updateCache(cache, CacheCreationStrategy.MULTI_THREAD);
			multiThreadEnd = System.currentTimeMillis();
			long singleThreadTime = singleThreadEnd - singleThreadStart, multiThreadTime = multiThreadEnd - multiThreadStart;
			singleThreadTimes[i] = singleThreadTime;
			multiThreadTimes[i] = multiThreadTime;
			assertTrue(String.format("single thread is faster? single thread: %sms; multi thread: %sms",singleThreadTime,multiThreadTime), multiThreadTime < singleThreadTime);
			LOGGER.debug("single thread is faster?\n\t\tsingle thread: {}ms\n\t\tmulti thread: {}ms",singleThreadTime,multiThreadTime);
		}
		LOGGER.debug("Average time results of {} runs:\n\t\tsingleThread: {}ms\n\t\tmulti thread: {}ms",times,average(singleThreadTimes),average(multiThreadTimes));
	}

	private long average(long[] singleThreadTimes)
	{
		long sum = 0;
		for (long l : singleThreadTimes) {
			sum += l;
		}
		return sum / singleThreadTimes.length;
	}
}
