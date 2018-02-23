/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.hmc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.sap.core.configuration.jalo.SAPRFCDestination;
import de.hybris.platform.sap.productconfig.hmc.constants.SapproductconfighmcConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.dataloader.settings.IDataloaderSource;


/**
 *
 */
@UnitTest
public class DataloaderInitialLoadItemActionTest
{
	private static final String TEST_SAP_DESTINATION = "testSapDestination";
	private static final String TEST_PWD = "testPwd";
	private static final String TEST_USER = "testUser";

	private DataloaderInitialLoadItemAction classUnderTest;
	@Mock
	private SAPConfiguration mockedConfiguration;
	@Mock
	private SAPRFCDestination mockedDestination;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest = new DataloaderInitialLoadItemAction();
		when(mockedConfiguration.getProperty(SapproductconfighmcConstants.CONFIGURATION_DATALOADER_SAP_SERVER))
				.thenReturn(mockedDestination);
		when(mockedConfiguration.getProperty(SapproductconfighmcConstants.CONFIGURATION_DATALOADER_SAP_RFC_DEST))
				.thenReturn(TEST_SAP_DESTINATION);
	}




	protected void mockSapRfcDest(final boolean loadBalacing)
	{
		when(Boolean.valueOf(mockedDestination.isConnectionTypeAsPrimitive())).thenReturn(Boolean.valueOf(!loadBalacing));
		if (loadBalacing)
		{
			when(mockedDestination.getSid()).thenReturn("SID");
			when(mockedDestination.getMessageServer()).thenReturn("testMessageServer");
			when(mockedDestination.getGroup()).thenReturn("PUBLIC");
		}
		else
		{
			when(mockedDestination.getInstance()).thenReturn("10");
			when(mockedDestination.getTargetHost()).thenReturn("testTargetHost");

		}
		when(mockedDestination.getUserid()).thenReturn(TEST_USER);
		when(mockedDestination.getPassword()).thenReturn(TEST_PWD);
		when(mockedDestination.getClient()).thenReturn("001");

		when(mockedDestination.getRfcDestinationName()).thenReturn("testDestinationName");

	}

	@Test
	public void createDataloaderSourceSSC23NoLoadBalance()
	{
		mockSapRfcDest(false);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkDirectDestination(dlSource);
	}



	@Test
	public void createDataloaderSourceSSC23LoadBalance()
	{
		mockSapRfcDest(true);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkLoadBalancedDestination(dlSource);
	}


	@Test
	public void createDataloaderSourceSSC24LoadBalance()
	{
		mockSapRfcDest(true);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkLoadBalancedDestination(dlSource);
	}

	@Test
	public void createDataloaderSourceSSC24NoLoadBalance()
	{
		mockSapRfcDest(false);
		final IDataloaderSource dlSource = classUnderTest.getSAPSource(mockedConfiguration);
		assertNotNull(dlSource);
		checkDirectDestination(dlSource);
	}

	protected void checkLoadBalancedDestination(final IDataloaderSource dlSource)
	{
		assertEquals(TEST_SAP_DESTINATION, dlSource.getRfcDestination());
		assertEquals("001", dlSource.getClientSetting().getClient());
		assertEquals(TEST_USER, dlSource.getClientSetting().getUser());
		assertEquals(TEST_PWD, dlSource.getClientSetting().getPassword());
		assertEquals("SID", dlSource.getEccSetting().getSid());
		assertEquals("testMessageServer", dlSource.getEccSetting().getMessageServer());
		assertEquals("PUBLIC", dlSource.getEccSetting().getGroup());
	}

	protected void checkDirectDestination(final IDataloaderSource dlSource)
	{
		assertEquals(TEST_SAP_DESTINATION, dlSource.getRfcDestination());
		assertEquals("001", dlSource.getClientSetting().getClient());
		assertEquals(TEST_USER, dlSource.getClientSetting().getUser());
		assertEquals(TEST_PWD, dlSource.getClientSetting().getPassword());
		assertEquals("10", dlSource.getEccSetting().getSid());
		assertEquals("testTargetHost", dlSource.getEccSetting().getMessageServer());
	}
}
