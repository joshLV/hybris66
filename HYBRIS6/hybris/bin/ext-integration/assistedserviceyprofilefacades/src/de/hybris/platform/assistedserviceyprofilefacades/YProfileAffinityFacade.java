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
package de.hybris.platform.assistedserviceyprofilefacades;


import de.hybris.platform.assistedserviceyprofilefacades.data.CategoryAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.CategoryAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.CustomerLoyaltyParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceFingerprintAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceFingerprintAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.KeywordSearchAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.KeywordSearchAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProductAffinityData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProductAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyHistoryData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyStatisticData;

import java.util.List;


/**
 * Interface provides ability to get affinity data for a profile.
 */
public interface YProfileAffinityFacade
{
	/**
	 * Returns list of products affinities from all profile nodes, belongs to customer with populated product, affinity,
	 * view count data.
	 *
	 * @param productAffinityParameterData
	 *           holds parameters to be used for data retrieval if needed
	 *
	 * @return List<YProfileProductAffinityData>
	 */
	List<ProductAffinityData> getProductAffinities(ProductAffinityParameterData productAffinityParameterData);

	/**
	 * Returns list of categories affinities associated to the current session customer
	 *
	 * @param categoryAffinityParameterData
	 *           holds parameters to be used for data retrieval if needed
	 *
	 * @return List<YProfileCategoryAffinityData>
	 */
	List<CategoryAffinityData> getCategoryAffinities(CategoryAffinityParameterData categoryAffinityParameterData);

	/**
	 * Returns list of keyword search affinities associated to the current session customer
	 *
	 * @param keywordSearchAffinityParameterData
	 *           holds parameters to be used for data retrieval if needed
	 * @return List<KeywordSearchAffinityData>
	 */
	List<KeywordSearchAffinityData> getKeywordSearchAffinities(
			KeywordSearchAffinityParameterData keywordSearchAffinityParameterData);

	/**
	 * Returns list of device affinities associated to the current session customer
	 *
	 * @param deviceAffinityParameterData
	 *           holds parameters to be used for data retrieval if needed
	 * @return List<DeviceAffinityParameterData>
	 */
	List<DeviceAffinityData> getDeviceAffinities(DeviceAffinityParameterData deviceAffinityParameterData);

	/**
	 * Returns list of device fingerprint affinities associated to the current session customer
	 *
	 * @param deviceFingerprintAffinityParameterData
	 *           holds parameters to be used for data retrieval if needed
	 * @return List<DeviceFingerprintAffinityData>
	 */
	List<DeviceFingerprintAffinityData> getDeviceFingerprintAffinities(
			DeviceFingerprintAffinityParameterData deviceFingerprintAffinityParameterData);

	/**
	 * Returns list of loyalty history affinities associated to the current session customer
	 *
	 * @param customerLoyaltyParameterData
	 *           holds parameters to be used for data retrieval if needed
	 * @return List<ProfileLoyaltyHistoryData>
	 */
	List<ProfileLoyaltyHistoryData> getProfileLoyaltyHistoryAffinity(CustomerLoyaltyParameterData customerLoyaltyParameterData);

	/**
	 * Returns list of loyalty statistics affinities associated to the current session customer
	 *
	 * @param customerLoyaltyParameterData
	 *           holds parameters to be used for data retrieval if needed
	 * @return List<ProfileLoyaltyStatisticData>
	 */
	List<ProfileLoyaltyStatisticData> getProfileLoyaltyStatisticAffinity(
			CustomerLoyaltyParameterData customerLoyaltyParameterData);
}