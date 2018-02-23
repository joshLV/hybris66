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
 * Robot test interface.
 */
public interface RobotTest
{
	/**
	 * Name.
	 *
	 * @return name
	 */
	String getName();

	/**
	 * Test suite.
	 *
	 * @return suite
	 */
	RobotTestSuite getTestSuite();

	/**
	 * Test result.
	 *
	 * @return test result
	 */
	RobotTestResult run();

    /**
     * If the test should be ignored
     * @return <code>true</code> if the test should be ignored
     */
    boolean isIgnored();
}
