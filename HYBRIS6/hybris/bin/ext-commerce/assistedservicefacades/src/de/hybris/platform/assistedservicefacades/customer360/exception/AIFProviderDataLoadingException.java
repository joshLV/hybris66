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
package de.hybris.platform.assistedservicefacades.customer360.exception;

/**
 * AIF Exception Class thrown in case of problem loading provider's data. This will be thrown in case the provider is
 * not able to retrieve data or data processing has a problem.
 */
public class AIFProviderDataLoadingException extends RuntimeException
{

	public AIFProviderDataLoadingException(final String message)
	{
		super(message);
	}

	public AIFProviderDataLoadingException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public AIFProviderDataLoadingException(final Throwable cause)
	{
		super(cause);
	}
}