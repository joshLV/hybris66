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
package de.hybris.platform.cmsfacades.exception;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.namedquery.service.NamedQueryFactory;


/**
 * Exception used when the {@link NamedQueryFactory} implementation does not match any existing named queries
 * @deprecated since 6.4, please use {@link de.hybris.platform.cms2.exceptions.InvalidNamedQueryException} instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public class InvalidNamedQueryException extends de.hybris.platform.cms2.exceptions.InvalidNamedQueryException
{
	private static final long serialVersionUID = -6615030091084809142L;

	public InvalidNamedQueryException(final String queryName)
	{
		super(queryName);
	}
}
