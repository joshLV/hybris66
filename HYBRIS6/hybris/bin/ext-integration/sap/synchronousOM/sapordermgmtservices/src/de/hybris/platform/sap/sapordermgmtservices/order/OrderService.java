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
package de.hybris.platform.sap.sapordermgmtservices.order;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;

import java.util.List;


/**
 * Service representation of an order and order history for SAP synchronous order management
 */
public interface OrderService
{
	/**
	 * Fetches an order from the back end
	 * 
	 * @param code
	 *           Technical ID of the order
	 * @return hybris order representation
	 */
	OrderData getOrderForCode(String code);

	/**
	 * Perform an order search, taking pagination and sorting into account
	 * 
	 * @param pageableData
	 *           Contains paging and sorting attributes
	 * @param statuses
	 *           Order statuses the search should be performed for
	 * @return Search result, including sorting and pagination
	 */
	SearchPageData<OrderHistoryData> getPagedOrderHistoryForStatuses(final PageableData pageableData,
			final OrderStatus... statuses);

	/**
	 * Perform an order search without pagination and sorting
	 * 
	 * @param statuses
	 *           Order statuses the search should be performed for
	 * @return List of orders
	 */
	List<OrderHistoryData> getOrderHistoryForStatuses(final OrderStatus... statuses);

	/**
	 * Creates a runtime configuration from the configuration attached to an item and returns it.
	 * This method is meant for a on-demand access, the configuration engine session is directly released
	 * after accessing it.
	 * @param code Order code
	 * @param itemNumber ECC item number (corresponds to entry number in OrderEntry)
	 * @return Runtime configuration
	 */
	ConfigModel getConfiguration(String code, String itemNumber);



}
