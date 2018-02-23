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
package de.hybris.platform.assistedserviceyprofilefacades.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceFingerprintAffinityData;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DeviceFingerprintAffinityPopulatorTest extends AbstractProfileAffinityTest
{
	protected DeviceFingerprintAffinityPopulator deviceFingerprintAffinityPopulator = new DeviceFingerprintAffinityPopulator<>();
	private List<NeighbourData> parsedNeighbours;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		parsedNeighbours = getDataFromJson(AbstractProfileAffinityTest.Y_PROFIL_JSON_PATH);
	}

	@Test
	public void getAffinityTest()
	{
		final List<NeighbourData> affinityList = parsedNeighbours.stream()
				.filter(n -> n.getSchema().contains(YaasyprofileconnectConstants.SCHEMA_COMMERCE_DEVICEFINGERPRINT_AFFINITY))
				.collect(Collectors.toList());

		assertEquals(1, affinityList.size());

		final NeighbourData neighbourData = affinityList.get(0);
		final DeviceFingerprintAffinityData deviceAffinityData = new DeviceFingerprintAffinityData();
		deviceFingerprintAffinityPopulator.populate(neighbourData, deviceAffinityData);

		assertTrue(deviceAffinityData.getDevice()
				.equalsIgnoreCase(neighbourData.getProperties().get(YaasyprofileconnectConstants.DEVICE_TYPE_FIELD)));
		assertTrue(deviceAffinityData.getViewCount()
				.equalsIgnoreCase(neighbourData.getProperties().get(YaasyprofileconnectConstants.DEVICE_USED_COUNT_FIELD)));
		assertTrue(deviceAffinityData.getBrowser()
				.equalsIgnoreCase(neighbourData.getProperties().get(YaasyprofileconnectConstants.DEVICE_BROWSER_FIELD)));
		assertTrue(deviceAffinityData.getOperatingSystem()
				.equalsIgnoreCase(neighbourData.getProperties().get(YaasyprofileconnectConstants.DEVICE_OS_FIELD)));
	}
}
