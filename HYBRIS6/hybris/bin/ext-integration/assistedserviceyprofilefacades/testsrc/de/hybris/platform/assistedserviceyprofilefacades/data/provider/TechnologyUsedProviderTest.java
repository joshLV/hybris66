/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.assistedserviceyprofilefacades.data.provider;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedserviceyprofilefacades.YProfileAffinityFacade;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceFingerprintAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.SummaryTechnologyUsedData;
import de.hybris.platform.assistedserviceyprofilefacades.data.TechnologyUsedData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@UnitTest
public class TechnologyUsedProviderTest
{
	private static final String PHONE = "Phone";
	private static final String TABLET = "TABLET";
	private static final String DEVICE_VIEW_COUNT_10 = "10";
	private static final String COMPUTER = "Computer";
	private static final String WINDOWS = "Windows";
	private static final String CHROME = "Chrome";
	private static final String EXPECTED_USER = "EXPECTED_USER";

	@InjectMocks
	private final TechnologyUsedProvider provider = new TechnologyUsedProvider();

	@Mock
	private YProfileAffinityFacade yProfileAffinityFacade;

	@Mock
	private UserService userService;

	@Mock
	private UserModel user;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(userService.getCurrentUser()).thenReturn(user);
		Mockito.when(user.getName()).thenReturn(EXPECTED_USER);
	}

	@Test
	public void getModelTest()
	{
		Mockito.when(yProfileAffinityFacade.getDeviceFingerprintAffinities(Mockito.any()))
				.thenReturn(prepareDeviceFingerprintDataList());

		Mockito.when(yProfileAffinityFacade.getDeviceAffinities(Mockito.any())).thenReturn(prepareDeviceAfinityDataList());

		final SummaryTechnologyUsedData data = provider.getModel(null);

		Assert.assertNotNull(data);

		checkDeviceData(data.getTechnologyUsedData());
		Assert.assertEquals(EXPECTED_USER, data.getName());
	}

	protected void checkDeviceData(final List<TechnologyUsedData>  data)
	{
		Assert.assertTrue(data.size() == 2);
		final Iterator<TechnologyUsedData> technologyUsedDataIterator = data.iterator();
		final TechnologyUsedData computerData = technologyUsedDataIterator.next();
		Assert.assertEquals(CHROME, computerData.getBrowser());
		Assert.assertEquals(WINDOWS, computerData.getOperatingSystem());
		Assert.assertEquals(COMPUTER, computerData.getDevice());

		final TechnologyUsedData tabletData = technologyUsedDataIterator.next();
		Assert.assertEquals(null, tabletData.getBrowser());
		Assert.assertEquals(null, tabletData.getOperatingSystem());
		Assert.assertEquals(TABLET, tabletData.getDevice());
	}


	protected List<DeviceFingerprintAffinityData> prepareDeviceFingerprintDataList()
	{
		final List<DeviceFingerprintAffinityData> fingerprintAffinityDataList = new ArrayList<>();
		fingerprintAffinityDataList.add(createFingerprintAffinityData(CHROME, WINDOWS, COMPUTER));
		fingerprintAffinityDataList.add(createFingerprintAffinityData(null, null, TABLET));
		fingerprintAffinityDataList.add(createFingerprintAffinityData(CHROME, null, TABLET));
		fingerprintAffinityDataList.add(createFingerprintAffinityData(CHROME, WINDOWS, null));
		return fingerprintAffinityDataList;
	}

	protected DeviceFingerprintAffinityData createFingerprintAffinityData(final String browser, final String os,
			final String device)
	{
		final DeviceFingerprintAffinityData fingerprintAffinityData = new DeviceFingerprintAffinityData();
		fingerprintAffinityData.setBrowser(browser);
		fingerprintAffinityData.setOperatingSystem(os);
		fingerprintAffinityData.setDevice(device);
		return fingerprintAffinityData;
	}

	protected final List<DeviceAffinityData> prepareDeviceAfinityDataList()
	{
		final List<DeviceAffinityData> deviceAffinityDataList = new ArrayList<>();
		deviceAffinityDataList.add(createDeviceAffinityData(COMPUTER, DEVICE_VIEW_COUNT_10));
		deviceAffinityDataList.add(createDeviceAffinityData(TABLET, DEVICE_VIEW_COUNT_10));
		deviceAffinityDataList.add(createDeviceAffinityData(PHONE, DEVICE_VIEW_COUNT_10));
		return deviceAffinityDataList;
	}

	protected DeviceAffinityData createDeviceAffinityData(final String device, final String count)
	{
		final DeviceAffinityData deviceAffinityData = new DeviceAffinityData();
		deviceAffinityData.setDevice(device);
		deviceAffinityData.setViewCount(count);
		return deviceAffinityData;
	}
}
