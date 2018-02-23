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
package de.hybris.platform.secaddon.services;

import de.hybris.platform.secaddon.constants.SecaddonConstants;
import de.hybris.platform.secaddon.data.CustomerInfo;
import de.hybris.platform.util.Config;
import de.hybris.platform.yaasconfiguration.service.YaasServiceFactory;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import rx.Observable;


/**
 *
 * Customer Mapping adapter for retrieving customer id from YaaS in a blocking way but not to affect other YaaS Client
 *
 */
public class CustomerMappingAdapter implements CustomerMappingClient
{
	private SecYaasServiceFactory yaasServiceFactory;

	@Override
	public Observable<List<CustomerInfo>> getCustomer(final String lang, final String mixinQuery, final String id)
	{
		return getAdaptee().getCustomer(lang, mixinQuery, id);
	}

	public Observable<List<CustomerInfo>> getCustomer(final String lang, final String id)
	{
		final Map<String, String> configMap = yaasServiceFactory
				.getMap(de.hybris.platform.secaddon.services.CustomerMappingClient.class);

		if (MapUtils.isNotEmpty(configMap) && configMap.containsKey(SecaddonConstants.MIXIN_QUERY_BACKOFFICE_PROPERTY_KEY))
		{
			return getCustomer(lang, configMap.get(SecaddonConstants.MIXIN_QUERY_BACKOFFICE_PROPERTY_KEY), id);
		}

		return getCustomer(lang, StringUtils.defaultIfEmpty(Config.getParameter(SecaddonConstants.MIXIN_QUERY_KEY),
				SecaddonConstants.MIXIN_QUERY_DEFAULT_VALUE), id);
	}

	@Required
	public void setYaasServiceFactory(final YaasServiceFactory yaasServiceFactory)
	{
		this.yaasServiceFactory = (SecYaasServiceFactory) yaasServiceFactory;
	}

	public CustomerMappingClient getAdaptee()
	{
		return yaasServiceFactory.lookupService(de.hybris.platform.secaddon.services.CustomerMappingClient.class);
	}
}