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
package de.hybris.platform.cmsfacades.namedquery.service.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.exception.InvalidNamedQueryException;
import de.hybris.platform.cmsfacades.exception.SearchExecutionNamedQueryException;
import de.hybris.platform.cmsfacades.namedquery.service.NamedQueryService;
import de.hybris.platform.cmswebservices.namedquery.NamedQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


/**
 * NamedQueryService implemented as an adapter for Flexible Search Service. This implementation receives requests to
 * perform the search and uses the {@link FlexibleSearchService} service to leverage the final search.
 *
 * @deprecated since 6.4, please use
 *             {@link de.hybris.platform.cms2.namedquery.service.impl.FlexibleSearchNamedQueryService} instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public class FlexibleSearchNamedQueryService
		extends de.hybris.platform.cms2.namedquery.service.impl.FlexibleSearchNamedQueryService implements NamedQueryService
{

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.4, please use
	 *             {@link de.hybris.platform.cms2.namedquery.service.impl.FlexibleSearchNamedQueryService#search(de.hybris.platform.cms2.namedquery.NamedQuery)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.4")
	@Override
	public <T> List<T> search(final NamedQuery namedQuery)
	{
		return super.search(namedQuery);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 6.4, please use
	 *             {@link de.hybris.platform.cms2.namedquery.service.impl.FlexibleSearchNamedQueryService#getSearchResult(de.hybris.platform.cms2.namedquery.NamedQuery)}
	 *             instead.
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.4")
	@Override
	public <T> SearchResult<T> getSearchResult(final NamedQuery namedQuery)
			throws InvalidNamedQueryException, SearchExecutionNamedQueryException
	{
		return super.getSearchResult(namedQuery);
	}

}

