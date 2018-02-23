/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.hybris.atddengine.framework.impl;


import com.hybris.atddengine.framework.RobotTestResult;

/**
 * Test result.
 */
public class PythonRobotTestResult implements RobotTestResult
{
	private final String message;

	private final boolean success;

	/**
	 * Constructor.
	 *
	 * @param success true if test passed successfully
	 * @param message description
	 */
	public PythonRobotTestResult(final boolean success, final String message)
	{
		this.success = success;
		this.message = message;
	}

	@Override
	public String getMessage()
	{
		return message;
	}

	@Override
	public boolean isSuccess()
	{
		return success;
	}
}
