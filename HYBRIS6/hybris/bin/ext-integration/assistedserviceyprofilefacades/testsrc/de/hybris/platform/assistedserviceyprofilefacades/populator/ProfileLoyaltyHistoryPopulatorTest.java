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
package de.hybris.platform.assistedserviceyprofilefacades.populator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyHistoryData;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProfileLoyaltyHistoryPopulatorTest extends AbstractProfileAffinityTest
{
	protected ProfileLoyaltyHistoryPopulator profileLoyaltyHistoryPopulator = new ProfileLoyaltyHistoryPopulator<>();
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
				.filter(n -> n.getSchema().contains(YaasyprofileconnectConstants.Loyalty.SCHEMA_LOYALTY_POINTS_HISTORY))
				.collect(Collectors.toList());

		assertEquals(1, affinityList.size());

		final NeighbourData neighbourData = affinityList.get(0);
		final ProfileLoyaltyHistoryData deviceAffinityData = new ProfileLoyaltyHistoryData();
		profileLoyaltyHistoryPopulator.populate(neighbourData, deviceAffinityData);

		assertTrue(deviceAffinityData.getCurrentLoyaltyBalance()
				.equalsIgnoreCase(neighbourData.getProperties().get(YaasyprofileconnectConstants.Loyalty.CURRENT_LOYALTY_BALANCE)));
	}
}
