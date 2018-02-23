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

package com.hybris.atddengine.framework;


/**
 * Test result.
 */
public interface RobotTestResult
{
	/**
	 * Result message.
	 *
	 * @return message text
	 */
	String getMessage();

	/**
	 * Test result status.
	 *
	 * @return true if succeeded
	 */
	boolean isSuccess();
}
