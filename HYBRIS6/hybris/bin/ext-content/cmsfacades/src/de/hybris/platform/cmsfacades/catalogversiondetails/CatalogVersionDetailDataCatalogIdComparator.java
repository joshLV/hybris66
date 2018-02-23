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
package de.hybris.platform.cmsfacades.catalogversiondetails;


import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.CatalogVersionDetailData;

import java.util.Comparator;


/**
 * Implementation of a {@link Comparator} which uses the natural ordering of uid in a {@link CatalogVersionDetailData}
 * dto.
 * 
 * @deprecated since version 6.4, please use the {@code CatalogFacade} in cmssmarteditwebservices extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public class CatalogVersionDetailDataCatalogIdComparator implements Comparator<CatalogVersionDetailData>
{

	@Override
	public int compare(final CatalogVersionDetailData that, final CatalogVersionDetailData other)
	{
		return that.getCatalogId().compareTo(other.getCatalogId());
	}
}
