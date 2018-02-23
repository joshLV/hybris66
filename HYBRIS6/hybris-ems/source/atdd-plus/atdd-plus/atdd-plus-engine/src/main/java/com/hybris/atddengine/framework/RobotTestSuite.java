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

import java.util.List;


/**
 * Robot test suite.
 */
public interface RobotTestSuite
{
	/**
	 * Close the suite.
	 */
	void close();

	/**
	 * Name.
	 *
	 * @return name
	 */
	String getName();

	/**
	 * Get test by test name.
	 *
	 * @param testName the name of the test
	 * @return test
	 */
	RobotTest getRobotTest(final String testName);

	/**
	 * Get all robot tests.
	 *
	 * @return list of existing tests.
	 */
	List<RobotTest> getRobotTests();

	/**
	 * Suite status.
	 *
	 * @return true if closed
	 */
	boolean isClosed();

	/**
	 * Suite status.
	 *
	 * @return true if started
	 */
	boolean isStarted();

	/**
	 * Run single test.
	 *
	 * @param robotTest test to run
	 * @return execution result
	 */
	RobotTestResult run(final RobotTest robotTest);

	/**
	 * Start the suite.
	 */
	void start();
}
