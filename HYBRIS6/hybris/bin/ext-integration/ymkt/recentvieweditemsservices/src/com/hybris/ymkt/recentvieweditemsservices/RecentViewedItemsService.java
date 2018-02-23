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
package com.hybris.ymkt.recentvieweditemsservices;

import java.util.List;


/**
 * This service accumulate and provide recently visited product and category codes.
 * 
 */
public interface RecentViewedItemsService
{

	/**
	 * Register productCode and/or categoryCode visited.
	 * 
	 * @param productCode
	 *           product code visited.
	 * @param categoryCode
	 *           category code visited.
	 */
	void productVisited(String productCode, String categoryCode);

	/**
	 * @return {@link List} of visited product codes.
	 */
	List<String> getRecentViewedProducts();

	/**
	 * @return {@link List} of visited category codes.
	 */
	List<String> getRecentViewedCategories();

}
