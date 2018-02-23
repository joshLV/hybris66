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


import java.io.File;

/**
 * Interface for supporting...
 *
 */
public interface RobotTestSuiteFactory
{
	/**
	 * Parse single suite.
	 *
	 * @param testSuiteFile suite file
	 * @return constructed suite
	 */
    RobotTestSuite parseTestSuite(final File testSuiteFile);

	/**
	 * Parse multiple suites.
	 *
	 * @param testSuiteFiles array of suite files
	 * @return constructed suite
	 */
    RobotTestSuite parseTestSuite(final File[] testSuiteFiles);

	/**
	 * Parse single suite with custom settings.
	 *
	 * @param robotSettings settings
	 * @param testSuiteFile suite file
	 * @return constructed suite
	 */
    RobotTestSuite parseTestSuite(final RobotSettings robotSettings, final File testSuiteFile);

	/**
	 * Parse multiple suites with custom settings.
	 *
	 * @param robotSettings settings
	 * @param testSuiteFiles array of suite files
	 * @return constructed suite
	 */
    RobotTestSuite parseTestSuite(final RobotSettings robotSettings, final File[] testSuiteFiles);
}

