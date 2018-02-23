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
import de.hybris.platform.assistedserviceyprofilefacades.data.LoyaltyData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyHistoryData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyStatisticData;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.ArrayList;


@UnitTest
public class LoyaltyProviderTest
{
	private static final String MISSING_CONFIG_VALUE_FOR_MAPPING = "2";
	private static final String LOYALTY_BALANCE_250 = "250";
	private static final String ACTIVITY_SCORE_10 = "10";

	@InjectMocks
	private final LoyaltyProvider provider = new LoyaltyProvider();
	@Mock
	private YProfileAffinityFacade yProfileAffinityFacade;

	private NavigableMap<Integer, String> activityScoreMappingMap;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		prepareActivityScoreMappings();
		provider.setActivityScoreMappingMap(activityScoreMappingMap);
	}

	@Test
	public void getModelTest()
	{
		Mockito.when(yProfileAffinityFacade.getProfileLoyaltyStatisticAffinity(Mockito.any()))
				.thenReturn(prepareLoyaltyStatisticDataList());

		Mockito.when(yProfileAffinityFacade.getProfileLoyaltyHistoryAffinity(Mockito.any()))
				.thenReturn(prepareHistoricalDataList());

		final LoyaltyData data = provider.getModel(null);

		Assert.assertNotNull(data);

		checkLoyaltyData(data);
	}


	protected void checkLoyaltyData(final LoyaltyData loyaltyData)
	{
		Assert.assertEquals((activityScoreMappingMap.floorEntry(Integer.valueOf(ACTIVITY_SCORE_10)).getValue()),
				loyaltyData.getActivityScore());
		Assert.assertEquals(LOYALTY_BALANCE_250, loyaltyData.getCurrentLoyaltyBalance());
		Assert.assertTrue(loyaltyData.isGoldBadgeActive());
		Assert.assertFalse(loyaltyData.isSilverBadgeActive());
		Assert.assertFalse(loyaltyData.isPlatinumBadgeActive());
	}
	@Test(expected = ConfigurationException.class)
	public void getActivityScoreTestForMisingConfiguration()
	{
		final ProfileLoyaltyStatisticData data = new ProfileLoyaltyStatisticData();
		data.setActivityScore(MISSING_CONFIG_VALUE_FOR_MAPPING);
		provider.getActivityScore(data);
	}

	@Test
	public void populateLoaltyDataMissingDataTest()
	{
		final LoyaltyData loyaltyData = provider.populateLoaltyData(Collections.EMPTY_LIST, Collections.EMPTY_LIST);

		Assert.assertNull(loyaltyData.getActivityScore());
		Assert.assertNull(loyaltyData.getCurrentLoyaltyBalance());
		Assert.assertFalse(loyaltyData.isGoldBadgeActive());
		Assert.assertFalse(loyaltyData.isSilverBadgeActive());
		Assert.assertFalse(loyaltyData.isPlatinumBadgeActive());
	}

	protected void prepareActivityScoreMappings()
	{
		activityScoreMappingMap = new TreeMap<>();
		activityScoreMappingMap.put(Integer.valueOf(10), "low");
		activityScoreMappingMap.put(Integer.valueOf(20), "medium");
		activityScoreMappingMap.put(Integer.valueOf(30), "max");
	}

	protected List<ProfileLoyaltyStatisticData> prepareLoyaltyStatisticDataList()
	{
		final List<ProfileLoyaltyStatisticData> statisticDataList = new ArrayList<>();
		final ProfileLoyaltyStatisticData statisticData = new ProfileLoyaltyStatisticData();
		statisticData.setActivityScore(ACTIVITY_SCORE_10);
		statisticDataList.add(statisticData);
		return statisticDataList;
	}

	protected List<ProfileLoyaltyHistoryData> prepareHistoricalDataList()
	{
		final List<ProfileLoyaltyHistoryData> historyDataList = new ArrayList<>();
		final ProfileLoyaltyHistoryData profileLoyaltyHistoryData = new ProfileLoyaltyHistoryData();
		profileLoyaltyHistoryData.setCurrentLoyaltyBalance(LOYALTY_BALANCE_250);
		historyDataList.add(profileLoyaltyHistoryData);
		return historyDataList;
	}
}
