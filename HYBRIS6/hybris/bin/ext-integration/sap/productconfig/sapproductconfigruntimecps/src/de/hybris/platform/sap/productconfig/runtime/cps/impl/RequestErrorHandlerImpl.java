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
package de.hybris.platform.sap.productconfig.runtime.cps.impl;

import de.hybris.platform.sap.productconfig.runtime.cps.RequestErrorHandler;
import de.hybris.platform.sap.productconfig.runtime.cps.model.external.CPSExternalConfiguration;
import de.hybris.platform.sap.productconfig.runtime.cps.model.pricing.PricingDocumentResult;
import de.hybris.platform.sap.productconfig.runtime.cps.model.runtime.CPSConfiguration;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationEngineException;
import de.hybris.platform.sap.productconfig.runtime.interf.PricingEngineException;

import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.hybris.charon.exp.HttpException;
import com.hybris.charon.exp.NotFoundException;

import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * Default implementation of {@link RequestErrorHandler}. Just forwards the exceptions into runtime exceptions, a
 * specific exception text is raised for HTTP NOT FOUND
 */
public class RequestErrorHandlerImpl implements RequestErrorHandler
{

	private static final String PRICING_TIMEOUT_ERROR_MESSAGE = "Timeout exception occured during pricing call";
	private static final String CONFIGURATION_TIMEOUT_ERROR_MESSAGE = "Timeout exception occured during configuration call";
	private static final String NOT_FOUND_MESSAGE = "Entity could not be found. Please review the session timeout settings in hybris ECP and in CPS";
	private static final Logger LOG = Logger.getLogger(RequestErrorHandlerImpl.class);
	protected static final String NO_SERVER_MESSAGE = "No message available";

	@Override
	public void processUpdateConfigurationError(final HttpException ex) throws ConfigurationEngineException
	{
		logRequestError("cps", ex);
		checkNotFound(ex);
		throw new ConfigurationEngineException("Update configuration failed, please see server log for more details", ex);
	}

	@Override
	public CPSConfiguration processCreateDefaultConfigurationError(final HttpException ex)
	{
		logRequestError("cps", ex);
		throw new IllegalStateException("Create default configuration failed, please see server log for more details", ex);
	}

	@Override
	public CPSConfiguration processGetConfigurationError(final HttpException ex) throws ConfigurationEngineException
	{
		logRequestError("cps", ex);
		checkNotFound(ex);
		throw new ConfigurationEngineException("Get configuration failed, please see server log for more details", ex);
	}

	@Override
	public void processDeleteConfigurationError(final HttpException ex)
	{
		logRequestError("cps", ex);
		ifNotFoundThrowIllegalState(ex);
		throw new IllegalStateException("Delete configuration failed, please see server log for more details", ex);
	}

	@Override
	public CPSExternalConfiguration processGetExternalConfigurationError(final HttpException ex)
			throws ConfigurationEngineException
	{
		logRequestError("cps", ex);
		checkNotFound(ex);
		throw new ConfigurationEngineException("Get external configuration failed, please see server log for more details", ex);
	}

	@Override
	public CPSConfiguration processCreateRuntimeConfigurationFromExternalError(final HttpException ex)
	{
		logRequestError("cps", ex);
		throw new IllegalStateException(
				"Create runtime configuration from external configuration failed, please see server log for more details", ex);
	}

	@Override
	public PricingDocumentResult processCreatePricingDocumentError(final HttpException ex) throws PricingEngineException
	{
		logRequestError("pricing", ex);
		throw new PricingEngineException("Create pricing document failed, please see server log for more details", ex);
	}

	@Override
	public boolean processHasKbError(final HttpException ex)
	{
		logRequestError("cps", ex);
		throw new IllegalStateException("Finding kb request failed, please see server log for more details", ex);
	}

	protected void checkNotFound(final HttpException ex) throws ConfigurationEngineException
	{
		if (ex instanceof NotFoundException)
		{
			throw new ConfigurationEngineException(NOT_FOUND_MESSAGE, ex);
		}
	}

	protected void ifNotFoundThrowIllegalState(final HttpException ex)
	{
		if (ex instanceof NotFoundException)
		{
			throw new IllegalStateException(NOT_FOUND_MESSAGE, ex);
		}
	}

	protected void logRequestError(final String serviceName, final HttpException ex)
	{
		LOG.error("While calling the " + serviceName + " service the following server error occured:" + getServerMessage(ex));
	}

	protected String getServerMessage(final HttpException ex)
	{
		final Observable<String> serverMessage = ex.getServerMessage();
		if (serverMessage == null)
		{
			return NO_SERVER_MESSAGE;
		}
		return serverMessage.subscribeOn(Schedulers.io()).toBlocking().first();
	}

	@Override
	public PricingDocumentResult processCreatePricingDocumentRuntimeException(final RuntimeException ex)
			throws PricingEngineException
	{
		final Throwable cause = ex.getCause();
		if (cause instanceof TimeoutException)
		{
			LOG.error(PRICING_TIMEOUT_ERROR_MESSAGE, cause);
			throw new PricingEngineException(PRICING_TIMEOUT_ERROR_MESSAGE, cause);
		}
		throw ex;
	}

	@Override
	public void processConfigurationRuntimeException(final RuntimeException e) throws ConfigurationEngineException
	{
		final Throwable cause = e.getCause();
		if (cause instanceof TimeoutException)
		{
			LOG.error(CONFIGURATION_TIMEOUT_ERROR_MESSAGE, cause);
			throw new ConfigurationEngineException(CONFIGURATION_TIMEOUT_ERROR_MESSAGE, cause);
		}
		throw e;
	}
}