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

import com.hybris.charon.RawResponse;
import de.hybris.platform.secaddon.data.*;
import de.hybris.platform.yaasconfiguration.service.YaasServiceFactory;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import rx.Observable;

/**
 * Impl for non-blocking calls for TicketServiceClient
 */
public class TicketServiceClientAdapter implements TicketServiceClient
{
	private YaasServiceFactory yaasServiceFactory;
	private static final Logger LOG = Logger.getLogger(TicketServiceClientAdapter.class);

	@Override
	public Observable<TicketData> getTicketDetails(String lang, String ticketId)
	{
		return getAdaptee().getTicketDetails(lang, ticketId);
	}

	@Override
	public Observable<RawResponse<List<TicketData>>> getTickets(String lang, String sort, int pageNumber, int pageSize,
			String customerId)
	{
		return getAdaptee().getTickets(lang, sort, pageNumber, pageSize, customerId);
	}

	@Override
	public Observable<RawResponse> createTicket(final String lang, final TicketSecData ticketData)
	{
		return getAdaptee().createTicket(lang, ticketData).map(ticketResponse -> {
			return ticketResponse;
		}).doOnError(error -> logError(error));
	}

	protected static void logError(final Throwable error)
	{
		LOG.error("Error during ticket operation", error);
	}


	@Override
	public Observable<RawResponse> addMessage(String lang, String ticketId, TranscriptSec transcript) {
		return getAdaptee().addMessage(lang, ticketId,transcript);
	}

	@Override
	public Observable<List<TicketType>> getTicketTypes(String lang)
	{
		return getAdaptee().getTicketTypes(lang);
	}

	public TicketServiceClient getAdaptee()
	{
		return yaasServiceFactory.lookupService(de.hybris.platform.secaddon.services.TicketServiceClient.class);
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
