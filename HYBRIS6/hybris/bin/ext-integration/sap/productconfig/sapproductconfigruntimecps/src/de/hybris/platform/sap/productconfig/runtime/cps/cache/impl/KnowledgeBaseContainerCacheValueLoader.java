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
package de.hybris.platform.sap.productconfig.runtime.cps.cache.impl;

import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.sap.productconfig.runtime.cps.client.MasterDataClient;
import de.hybris.platform.sap.productconfig.runtime.cps.client.MasterDataClientBase;
import de.hybris.platform.sap.productconfig.runtime.cps.impl.CPSTimer;
import de.hybris.platform.sap.productconfig.runtime.cps.model.masterdata.CPSMasterDataKnowledgeBase;
import de.hybris.platform.sap.productconfig.runtime.cps.model.masterdata.cache.CPSMasterDataKnowledgeBaseContainer;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.yaasconfiguration.service.YaasServiceFactory;

import org.springframework.beans.factory.annotation.Required;

import rx.Scheduler;
import rx.schedulers.Schedulers;

import com.hybris.charon.exp.HttpException;


/**
 * Querries the CPS master data service to fill the CPS master data cache.
 */
public class KnowledgeBaseContainerCacheValueLoader implements CacheValueLoader<CPSMasterDataKnowledgeBaseContainer>
{
	private MasterDataClientBase clientSetExternally = null;
	private YaasServiceFactory yaasServiceFactory;
	private final CPSTimer timer = new CPSTimer();
	private final Scheduler scheduler = Schedulers.io();

	private Converter<CPSMasterDataKnowledgeBase, CPSMasterDataKnowledgeBaseContainer> knowledgeBaseConverter;

	@Override
	public CPSMasterDataKnowledgeBaseContainer load(final CacheKey paramCacheKey)
	{
		if (!(paramCacheKey instanceof MasterDataCacheKey))
		{
			throw new CacheValueLoadException("CacheKey is not instance of MasterDataCacheKey");
		}
		final MasterDataCacheKey key = (MasterDataCacheKey) paramCacheKey;

		return getKnowledgeBaseConverter().convert(getKbFromService(key.getKbId(), key.getLang()));
	}

	protected CPSMasterDataKnowledgeBase getKbFromService(final String kbId, final String lang)
	{
		try
		{
			timer.start("getKnowledgebase/" + kbId);
			final CPSMasterDataKnowledgeBase masterData = getClient().getKnowledgebase(kbId, lang).subscribeOn(getScheduler())
					.toBlocking().first();
			timer.stop();
			return masterData;
		}
		catch (final HttpException ex)
		{
			throw new CacheValueLoadException("Could not get knowledge base from service", ex);
		}
	}

	protected MasterDataClientBase getClient()
	{
		if (clientSetExternally != null)
		{
			return clientSetExternally;
		}
		else
		{
			return getYaasServiceFactory().lookupService(MasterDataClient.class);
		}
	}

	/**
	 * Set Charon client from outside (only used for testing)
	 *
	 * @param newClient
	 */
	public void setClient(final MasterDataClientBase newClient)
	{
		clientSetExternally = newClient;
	}

	protected Converter<CPSMasterDataKnowledgeBase, CPSMasterDataKnowledgeBaseContainer> getKnowledgeBaseConverter()
	{
		return knowledgeBaseConverter;
	}

	/**
	 * @param knowledgeBaseConverter
	 *           the knowledgeBaseConverter to set
	 */
	@Required
	public void setKnowledgeBaseConverter(
			final Converter<CPSMasterDataKnowledgeBase, CPSMasterDataKnowledgeBaseContainer> knowledgeBaseConverter)
	{
		this.knowledgeBaseConverter = knowledgeBaseConverter;
	}

	protected Scheduler getScheduler()
	{
		return scheduler;
	}

	protected YaasServiceFactory getYaasServiceFactory()
	{
		return yaasServiceFactory;
	}

	/**
	 * @param yaasServiceFactory
	 *           the yaasServiceFactory to set
	 */
	@Required
	public void setYaasServiceFactory(final YaasServiceFactory yaasServiceFactory)
	{
		this.yaasServiceFactory = yaasServiceFactory;
	}

}
