/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.cissapdigitalpayment.exceptions;

/**
 *
 */
public class SapDigitalPaymentCaptureException extends Exception
{

	private final String message;


	public SapDigitalPaymentCaptureException(final String message)
	{
		super();
		this.message = message;
	}


	/**
	 * @return the message
	 */
	@Override
	public String getMessage()
	{
		return message;
	}



}
