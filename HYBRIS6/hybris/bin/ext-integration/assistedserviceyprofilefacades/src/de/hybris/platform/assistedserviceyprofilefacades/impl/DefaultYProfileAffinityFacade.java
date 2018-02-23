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
package de.hybris.platform.assistedserviceyprofilefacades.impl;

import de.hybris.platform.assistedservicefacades.customer360.exception.AIFProviderDataLoadingException;
import de.hybris.platform.assistedserviceyprofilefacades.YProfileAffinityFacade;
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
import de.hybris.platform.assistedserviceyprofilefacades.data.RecentlyUpdatedComparator;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.yaasyprofileconnect.client.ProfileSecuredGraphClientAdapter;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;
import com.hybris.charon.exp.HttpException;


/**
 * Default implementation of {@link YProfileAffinityFacade} that uses graph data from yprofile
 * {@link ProfileSecuredGraphClientAdapter#getNeighbours(String, String, String)}
 */
public class DefaultYProfileAffinityFacade implements YProfileAffinityFacade
{
	private static final Logger LOG = Logger.getLogger(DefaultYProfileAffinityFacade.class);

	private UserService userService;
	private ProfileSecuredGraphClientAdapter profileSecuredGraphClientAdapter;

	private Converter<NeighbourData, ProductAffinityData> productAffinityConverter;
	private Converter<NeighbourData, CategoryAffinityData> categoryAffinityConverter;
	private Converter<NeighbourData, KeywordSearchAffinityData> keywordSearchAffinityConverter;
	private Converter<NeighbourData, DeviceAffinityData> deviceAffinityConverter;
	private Converter<NeighbourData, DeviceFingerprintAffinityData> deviceFingerprintAffinityConverter;
	private Converter<NeighbourData, ProfileLoyaltyStatisticData> profileLoyaltyStatisticConverter;
	private Converter<NeighbourData, ProfileLoyaltyHistoryData> profileLoyaltyHistoryConverter;

	@Override
	public List<ProductAffinityData> getProductAffinities(final ProductAffinityParameterData affinityParameterData)
	{

		final List<NeighbourData> sortedAndLimitedNeighbours = fetchAndSortAffinities(affinityParameterData.getSizeLimit(),
				YaasyprofileconnectConstants.SCHEMA_COMMERCE_PRODUCT_AFFINITY);

		//now convert the most recent NeighbourData to products list
		return Converters.convertAll(sortedAndLimitedNeighbours, getProductAffinityConverter());
	}

	@Override
	public List<CategoryAffinityData> getCategoryAffinities(final CategoryAffinityParameterData affinityParameterData)
	{
		final List<NeighbourData> sortedAndLimitedNeighbours = fetchAndSortAffinities(affinityParameterData.getSizeLimit(),
				YaasyprofileconnectConstants.SCHEMA_COMMERCE_CATEGORY_AFFINITY);

		//now convert the most recent NeighbourData to categories list
		return Converters.convertAll(sortedAndLimitedNeighbours, getCategoryAffinityConverter());
	}

	@Override
	public List<KeywordSearchAffinityData> getKeywordSearchAffinities(
			final KeywordSearchAffinityParameterData affinityParameterData)
	{

		final List<NeighbourData> sortedAndLimitedNeighbours = fetchAndSortAffinities(affinityParameterData.getSizeLimit(),
				YaasyprofileconnectConstants.SCHEMA_COMMERCE_KEYWORD_SEARCH_AFFINITY);

		//now convert the most recent NeighbourData to search keywords list
		return Converters.convertAll(sortedAndLimitedNeighbours, getKeywordSearchAffinityConverter());
	}

	@Override
	public List<DeviceAffinityData> getDeviceAffinities(final DeviceAffinityParameterData deviceAffinityParameterData)
	{
		final List<NeighbourData> sortedAndLimitedNeighbours = fetchAndSortAffinities(deviceAffinityParameterData.getSizeLimit(),
				YaasyprofileconnectConstants.SCHEMA_COMMERCE_DEVICE_AFFINITY);

		//now convert the most recent NeighbourData to search keywords list
		return Converters.convertAll(sortedAndLimitedNeighbours, getDeviceAffinityConverter());
	}

	@Override
	public List<DeviceFingerprintAffinityData> getDeviceFingerprintAffinities(
			final DeviceFingerprintAffinityParameterData deviceFingerprintAffinityParameterData)
	{
		final List<NeighbourData> sortedAndLimitedNeighbours = fetchAndSortAffinities(
				deviceFingerprintAffinityParameterData.getSizeLimit(),
				YaasyprofileconnectConstants.SCHEMA_COMMERCE_DEVICEFINGERPRINT_AFFINITY);
		//now convert the most recent NeighbourData to search keywords list
		return Converters.convertAll(sortedAndLimitedNeighbours, getDeviceFingerprintAffinityConverter());
	}

	@Override
	public List<ProfileLoyaltyStatisticData> getProfileLoyaltyStatisticAffinity(
			final CustomerLoyaltyParameterData customerLoyaltyParameterData)
	{
		final int sizeLimit = customerLoyaltyParameterData.getSizeLimit();
		final List<NeighbourData> sortedAndLimitedNeighbours = fetchAndSortAffinities(sizeLimit,
				YaasyprofileconnectConstants.Loyalty.SCHEMA_LOYALTY_ACTIVITY_STATISTICS);
		//now convert the most recent NeighbourData to search keywords list
		return Converters.convertAll(sortedAndLimitedNeighbours, getProfileLoyaltyStatisticConverter());
	}

	@Override
	public List<ProfileLoyaltyHistoryData> getProfileLoyaltyHistoryAffinity(
			final CustomerLoyaltyParameterData customerLoyaltyParameterData)
	{
		final int sizeLimit = customerLoyaltyParameterData.getSizeLimit();
		final List<NeighbourData> sortedAndLimitedNeighbours = fetchAndSortAffinities(sizeLimit,
				YaasyprofileconnectConstants.Loyalty.SCHEMA_LOYALTY_POINTS_HISTORY);
		//now convert the most recent NeighbourData to search keywords list
		return Converters.convertAll(sortedAndLimitedNeighbours, getProfileLoyaltyHistoryConverter());
	}

	/**
	 *
	 * This method is responsible for fetch neighbordata, in our case all affinities associated to profile and the
	 * affinity type and then sort based on most recently updated affinity
	 *
	 * @param sizeLimit
	 *           number of neighbors to return
	 * @param affinitySchema
	 *           type of affinity to return
	 *
	 * @return list of neighbor data after sorting and limit
	 *
	 */
	protected List<NeighbourData> fetchAndSortAffinities(final int sizeLimit, final String affinitySchema)
	{
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		final List<NeighbourData> neighbours = Lists.newArrayList();
		try
		{
			//fetch all NeighbourData based on profile and affinity schema
			getProfileNeighboursByIdentity(encodeUid(currentCustomer.getUid())).forEach(
					profileNode -> getAffinitiesNeighboursByProfile(profileNode.getId(), affinitySchema).forEach(neighbours::add));
		}
		//exception happened during communicating with yprofile
		catch (final HttpException err) //NOSONAR
		{
			LOG.error("Exception happened during communicating with YaaS while trying to get affinity with schema [" + affinitySchema
					+ "] for customer [" + currentCustomer.getUid() + "]. Error: " + err);
			if (null != err.getServerMessage())
			{
				err.getServerMessage().subscribe(message -> LOG
						.error("Exception happened during communicating with YaaS while trying to get affinity with schema ["
								+ affinitySchema + "] for customer [" + currentCustomer.getUid() + "] with server message " + message));
			}
		}

		//now from the list of top X recent affinities now we need to sort again to limit only to top X across all profiles
		final List<NeighbourData> sortedAndLimitedNeighbours = neighbours.stream()
				.parallel().sorted((final NeighbourData affinityData1,
						final NeighbourData affinityData2) -> new RecentlyUpdatedComparator().compare(affinityData1, affinityData2))
				.limit(sizeLimit).collect(Collectors.toList());

		return sortedAndLimitedNeighbours;

	}

	/**
	 * The customer uid need to encode twice. possible because it is email address format.
	 *
	 * @param uid
	 * @return
	 */
	protected String encodeUid(final String uid)
	{
		try
		{
			return URLEncoder.encode(URLEncoder.encode(uid, StandardCharsets.UTF_8.toString()), StandardCharsets.UTF_8.toString());
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new AIFProviderDataLoadingException("Encoding is unsupported!", e);
		}
	}

	/**
	 * Returns Profile Nodes for provided Identity
	 *
	 * @param customerUid
	 * @return
	 */
	protected List<NeighbourData> getProfileNeighboursByIdentity(final String customerUid)
	{

		return getProfileSecuredGraphClientAdapter()
				.getNeighbours(YaasyprofileconnectConstants.NAMESPACE_CORE, YaasyprofileconnectConstants.TYPE_IDENTITY, customerUid)
				.stream().filter(n -> n.getSchema().contains(YaasyprofileconnectConstants.SCHEMA_CORE_PROFILE))
				.collect(Collectors.toList());

	}

	protected List<NeighbourData> getAffinitiesNeighboursByProfile(final String profileId, final String affinitySchema)
	{
		return getProfileSecuredGraphClientAdapter()
				.getNeighbours(YaasyprofileconnectConstants.NAMESPACE_CORE, YaasyprofileconnectConstants.TYPE_PROFILE, profileId)
				.stream().filter(n -> n.getSchema().contains(affinitySchema)).collect(Collectors.toList());
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

	protected ProfileSecuredGraphClientAdapter getProfileSecuredGraphClientAdapter()
	{
		return profileSecuredGraphClientAdapter;
	}

	@Required
	public void setProfileSecuredGraphClientAdapter(final ProfileSecuredGraphClientAdapter profileSecuredGraphClientAdapter)
	{
		this.profileSecuredGraphClientAdapter = profileSecuredGraphClientAdapter;
	}

	protected Converter<NeighbourData, ProductAffinityData> getProductAffinityConverter()
	{
		return productAffinityConverter;
	}

	@Required
	public void setProductAffinityConverter(final Converter<NeighbourData, ProductAffinityData> productAffinityConverter)
	{
		this.productAffinityConverter = productAffinityConverter;
	}

	protected Converter<NeighbourData, CategoryAffinityData> getCategoryAffinityConverter()
	{
		return categoryAffinityConverter;
	}

	@Required
	public void setCategoryAffinityConverter(final Converter<NeighbourData, CategoryAffinityData> categoryAffinityConverter)
	{
		this.categoryAffinityConverter = categoryAffinityConverter;
	}

	protected Converter<NeighbourData, KeywordSearchAffinityData> getKeywordSearchAffinityConverter()
	{
		return keywordSearchAffinityConverter;
	}

	@Required
	public void setKeywordSearchAffinityConverter(
			final Converter<NeighbourData, KeywordSearchAffinityData> keywordSearchAffinityConverter)
	{
		this.keywordSearchAffinityConverter = keywordSearchAffinityConverter;
	}

	protected Converter<NeighbourData, DeviceAffinityData> getDeviceAffinityConverter()
	{
		return deviceAffinityConverter;
	}

	@Required
	public void setDeviceAffinityConverter(final Converter<NeighbourData, DeviceAffinityData> deviceAffinityConverter)
	{
		this.deviceAffinityConverter = deviceAffinityConverter;
	}

	protected Converter<NeighbourData, DeviceFingerprintAffinityData> getDeviceFingerprintAffinityConverter()
	{
		return deviceFingerprintAffinityConverter;
	}

	@Required
	public void setDeviceFingerprintAffinityConverter(
			final Converter<NeighbourData, DeviceFingerprintAffinityData> deviceFingerprintAffinityConverter)
	{
		this.deviceFingerprintAffinityConverter = deviceFingerprintAffinityConverter;
	}

	public Converter<NeighbourData, ProfileLoyaltyStatisticData> getProfileLoyaltyStatisticConverter()
	{
		return profileLoyaltyStatisticConverter;
	}

	@Required
	public void setProfileLoyaltyStatisticConverter(
			final Converter<NeighbourData, ProfileLoyaltyStatisticData> profileLoyaltyStatisticConverter)
	{
		this.profileLoyaltyStatisticConverter = profileLoyaltyStatisticConverter;
	}

	public Converter<NeighbourData, ProfileLoyaltyHistoryData> getProfileLoyaltyHistoryConverter()
	{
		return profileLoyaltyHistoryConverter;
	}

	@Required
	public void setProfileLoyaltyHistoryConverter(
			final Converter<NeighbourData, ProfileLoyaltyHistoryData> profileLoyaltyHistoryConverter)
	{
		this.profileLoyaltyHistoryConverter = profileLoyaltyHistoryConverter;
	}
}