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
package de.hybris.platform.yaasyprofileconnect.client;

import de.hybris.platform.yaasconfiguration.service.YaasServiceFactory;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;


/**
 *
 */
public class ProfileSecuredGraphClientAdapter implements ProfileSecuredGraphClient
{

	private final Scheduler scheduler = Schedulers.io();
	private YaasServiceFactory yaasServiceFactory;
	private static final Logger LOG = Logger.getLogger(ProfileSecuredGraphClientAdapter.class);

	@Override
	public List<NeighbourData> getNeighbours(final String namespace, final String type, final String id)
	{
		final List<NeighbourData> neighbours = new ArrayList<>();
		getAdaptee().getAsyncNeighbours(namespace, type, id)//
				.subscribeOn(scheduler)//
				.toBlocking().forEach(l -> neighbours.addAll(l));
		return neighbours;
	}

	@Override
	public Observable<List<NeighbourData>> getAsyncNeighbours(final String namespace, final String type, final String id)
	{
		return getAdaptee().getAsyncNeighbours(namespace, type, id);
	}

	public ProfileSecuredGraphClient getAdaptee()
	{
		return yaasServiceFactory.lookupService(ProfileSecuredGraphClient.class);
	}

	protected YaasServiceFactory getYaasServiceFactory()
	{
		return yaasServiceFactory;
	}

	@Required
	public void setYaasServiceFactory(final YaasServiceFactory yaasServiceFactory)
	{
		this.yaasServiceFactory = yaasServiceFactory;
	}
}
