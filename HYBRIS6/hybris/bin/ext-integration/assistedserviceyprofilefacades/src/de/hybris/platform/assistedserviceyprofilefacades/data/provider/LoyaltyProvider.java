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
import de.hybris.platform.assistedserviceyprofilefacades.data.CustomerLoyaltyParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.LoyaltyData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyHistoryData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyStatisticData;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;


/**
 * YProfile Loyalty fragment provider
 */
public class LoyaltyProvider implements FragmentModelProvider<LoyaltyData>
{
	private static final int LOYALTY_LIMIT = 1;

	private YProfileAffinityFacade yProfileAffinityFacade;
	private NavigableMap<Integer, String> activityScoreMappingMap;

	@Override
	public LoyaltyData getModel(final Map<String, String> parameters)
	{
		return prepareProfileLoyaltyData();
	}


	protected LoyaltyData prepareProfileLoyaltyData()
	{
		final CustomerLoyaltyParameterData customerLoyaltyParameterData = new CustomerLoyaltyParameterData();
		customerLoyaltyParameterData.setSizeLimit(LOYALTY_LIMIT);

		final List<ProfileLoyaltyStatisticData> profileLoyaltyStatisticDataList = getyProfileAffinityFacade()
				.getProfileLoyaltyStatisticAffinity(customerLoyaltyParameterData);

		final List<ProfileLoyaltyHistoryData> profileLoyaltyHistoryDataList = getyProfileAffinityFacade()
				.getProfileLoyaltyHistoryAffinity(customerLoyaltyParameterData);

		return populateLoaltyData(profileLoyaltyStatisticDataList, profileLoyaltyHistoryDataList);
	}

	protected LoyaltyData populateLoaltyData(final List<ProfileLoyaltyStatisticData> profileLoyaltyStatisticData,
			final List<ProfileLoyaltyHistoryData> profileLoyaltyHistoryData)
	{
		final LoyaltyData loyaltyData = new LoyaltyData();

		if (profileLoyaltyStatisticData.iterator().hasNext())
		{
			final ProfileLoyaltyStatisticData statisticData = profileLoyaltyStatisticData.iterator().next();
			loyaltyData.setActivityScore(getActivityScore(statisticData));
		}

		if (profileLoyaltyHistoryData.iterator().hasNext())
		{
			final ProfileLoyaltyHistoryData historyData = profileLoyaltyHistoryData.iterator().next();
			loyaltyData.setCurrentLoyaltyBalance(historyData.getCurrentLoyaltyBalance());
		}

		//TODO populate properties by enricher graph
		loyaltyData.setGoldBadgeActive(loyaltyData.getActivityScore() != null ? true : false);
		loyaltyData.setSilverBadgeActive(false);
		loyaltyData.setPlatinumBadgeActive(false);
		return loyaltyData;
	}

	String getActivityScore(final ProfileLoyaltyStatisticData statisticData)
	{
		final Entry<Integer, String> scoreData = getActivityScoreMappingMap()
				.floorEntry(Integer.valueOf(statisticData.getActivityScore()));

		if (scoreData == null)
		{
			throw new ConfigurationException(
					"Broken spring configuration for profile activity score mapping. Check activityScoreMapping bean definition.");
		}
		return scoreData.getValue();
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

	protected NavigableMap<Integer, String> getActivityScoreMappingMap()
	{
		return activityScoreMappingMap;
	}

	@Required
	public void setActivityScoreMappingMap(final NavigableMap<Integer, String> activityScoreMappingMap)
	{
		this.activityScoreMappingMap = activityScoreMappingMap;
	}
}