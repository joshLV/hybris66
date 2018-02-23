/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedserviceyprofilefacades.data.provider;

import de.hybris.platform.assistedservicefacades.customer360.FragmentModelProvider;
import de.hybris.platform.assistedserviceyprofilefacades.YProfileAffinityFacade;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.SummaryTechnologyUsedData;
import de.hybris.platform.assistedserviceyprofilefacades.data.TechnologyUsedData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceFingerprintAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceFingerprintAffinityData;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Required;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;


/**
 * YProfile Technology fragment provider
 */
public class TechnologyUsedProvider implements FragmentModelProvider<SummaryTechnologyUsedData>
{
	private int deviceFetchLimit = 100;
	private int deviceLimit = 3;

	private YProfileAffinityFacade yProfileAffinityFacade;
	private UserService userService;

	@Override
	public SummaryTechnologyUsedData getModel(final Map<String, String> parameters)
	{
		final List<TechnologyUsedData> technologyUsedDataList = new ArrayList<>(deviceLimit);
		final Map<String, TechnologyUsedData> mostPopularTechnologyByDeviceMap = getTechnologyUsedByDevice();
		final Map<String, Integer> usedDeviceCounts = getUsedDeviceCounts();
		final int totalUsedDevice = getTotalUsedCount(usedDeviceCounts);

		usedDeviceCounts.entrySet().stream().sorted(comparingByValue(Collections.reverseOrder())).limit(deviceLimit)
				.collect(Collectors.toList()).forEach(entry -> {
			final TechnologyUsedData technologyUsedData = mostPopularTechnologyByDeviceMap.get(entry.getKey());
			if (technologyUsedData != null)
			{
				technologyUsedData.setPercentage(entry.getValue() * 100 / totalUsedDevice);
				technologyUsedDataList.add(technologyUsedData);
			}
		});

		final SummaryTechnologyUsedData summaryTechnologyUsedData = new SummaryTechnologyUsedData();

		summaryTechnologyUsedData.setTechnologyUsedData(technologyUsedDataList);
		summaryTechnologyUsedData.setName(getUserService().getCurrentUser().getName());
		
		return summaryTechnologyUsedData;
	}

	protected Map<String, Integer> getUsedDeviceCounts()
	{
		final DeviceAffinityParameterData deviceAffinityParameterData = new DeviceAffinityParameterData();
		deviceAffinityParameterData.setSizeLimit(deviceFetchLimit);
		// get all device nodes up to recent deviceFetchLimit
		final List<DeviceAffinityData> deviceTypeList = getyProfileAffinityFacade()
				.getDeviceAffinities(deviceAffinityParameterData);

		// create Map Device -> Number of usage
		final Map<String, Integer> deviceUsedCounts = new HashMap<>();
		// can't use Java8 streams here because of total which should be also counted in one loop
		for (final DeviceAffinityData deviceAffinityData : deviceTypeList)
		{
			final Integer usedCount = Integer.valueOf(deviceAffinityData.getViewCount());
			deviceUsedCounts.computeIfPresent(deviceAffinityData.getDevice(),
					(device, totalDeviceCount) -> totalDeviceCount + usedCount);
			deviceUsedCounts.putIfAbsent(deviceAffinityData.getDevice(), usedCount);
		}
		return deviceUsedCounts;
	}

	protected Map<String, TechnologyUsedData> getTechnologyUsedByDevice()
	{
		final DeviceFingerprintAffinityParameterData deviceFingerprintAffinityParameterData = new DeviceFingerprintAffinityParameterData();
		deviceFingerprintAffinityParameterData.setSizeLimit(deviceFetchLimit);
		// get all device fingerprint nodes up to recent deviceFetchLimit
		final List<DeviceFingerprintAffinityData> deviceFingerprintList = getyProfileAffinityFacade()
				.getDeviceFingerprintAffinities(deviceFingerprintAffinityParameterData);

		// create Map Device -> latest OS + browser for this device
		final Map<String, TechnologyUsedData> mostPopularTechnologyByDevice = new HashMap<>();
		deviceFingerprintList.forEach(deviceFingerprintAffinityData -> {
			if (deviceFingerprintAffinityData.getDevice() != null
					&& !mostPopularTechnologyByDevice.containsKey(deviceFingerprintAffinityData.getDevice()))
			{
				final TechnologyUsedData technologyUsedData = new TechnologyUsedData();
				technologyUsedData.setBrowser(deviceFingerprintAffinityData.getBrowser());
				technologyUsedData.setOperatingSystem(deviceFingerprintAffinityData.getOperatingSystem());
				technologyUsedData.setDevice(deviceFingerprintAffinityData.getDevice());
				mostPopularTechnologyByDevice.put(deviceFingerprintAffinityData.getDevice(), technologyUsedData);
			}
		});
		return mostPopularTechnologyByDevice;
	}

	protected int getTotalUsedCount(final Map<String, Integer> deviceUsedCounts)
	{
		return deviceUsedCounts.values().stream().mapToInt(Integer::intValue).sum();
	}


	protected YProfileAffinityFacade getyProfileAffinityFacade()
	{
		return yProfileAffinityFacade;
	}

	@Required
	public void setyProfileAffinityFacade(final YProfileAffinityFacade yProfileAffinityFacade)
	{
		this.yProfileAffinityFacade = yProfileAffinityFacade;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public int getDeviceFetchLimit()
	{
		return deviceFetchLimit;
	}

	public void setDeviceFetchLimit(final int deviceFetchLimit)
	{
		this.deviceFetchLimit = deviceFetchLimit;
	}

	public void setDeviceLimit(final int deviceLimit)
	{
		this.deviceLimit = deviceLimit;
	}

	public int getDeviceLimit()
	{
		return deviceLimit;
	}
}