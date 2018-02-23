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
package de.hybris.platform.cmsfacades.namedquery.service;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.exception.InvalidNamedQueryException;


/**
 * NamedQuery Factory for NamedQueryService retrieve the NamedQueries
 *
 * @deprecated since 6.4, please use {@link de.hybris.platform.cms2.namedquery.service.NamedQueryFactory} instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public interface NamedQueryFactory extends de.hybris.platform.cms2.namedquery.service.NamedQueryFactory
{

	@Override
	@Deprecated
	/**
	 * Gets the query for this queryName
	 *
	 * @param queryName
	 *           - the query name
	 * @return a String object representing the Query
	 * @throws InvalidNamedQueryException
	 *            when the queryName does not match any existing named queries
	 *
	 * @deprecated since 6.4, please use
	 *             {@link de.hybris.platform.cms2.namedquery.service.NamedQueryFactory#getNamedQuery(String)} instead.
	 */
	@HybrisDeprecation(sinceVersion = "6.4")
	String getNamedQuery(final String queryName) throws InvalidNamedQueryException;

}
