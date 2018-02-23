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
package de.hybris.platform.assistedserviceservices.events;


import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ticket.enums.EventType;
import de.hybris.platform.ticketsystem.events.SessionEvent;
import de.hybris.platform.ticketsystem.events.model.SessionEventModel;

import java.util.Date;

/**
 * Customer Support events service.
 */
public interface CustomerSupportEventService
{
	/**
	 * creates session event
	 *
	 * @param asmEventData the event data
	 */
	void registerSessionEvent(SessionEvent asmEventData);

	/**
	 * Search for agent-specific events
	 *
	 * @param agent
	 *           AS agent
	 * @param eventType
	 *           type
	 * @param startDate
	 *           start date
	 * @param endDate
	 *           end date
	 * @param pageableData
	 *           pageable data
	 * @param limit
	 *           limit of returned customers
	 *
	 * @return SearchPageData<CsSessionEventModel>
	 */
	SearchPageData<SessionEventModel> getAllEventsForAgent(EmployeeModel agent, EventType eventType, Date startDate,
			Date endDate, PageableData pageableData, int limit);

	/**
	 * Search for customer based on event parameters.
	 *
	 * @param agent AS agent
	 * @param eventType type
	 * @param startDate start date
	 * @param endDate end date
	 * @param pageableData pageable data
	 * @param limit limit of returned customers
	 * @param <T>
	 * @return CustomerModel based on event parameters
	 */
	<T extends CustomerModel> SearchPageData<T> findAllCustomersByEventsAndAgent(final EmployeeModel agent,
			final EventType eventType, final Date startDate, final Date endDate, final PageableData pageableData, final int limit);
}