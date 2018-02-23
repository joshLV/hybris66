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

package com.hybris.atddengine.tasks;

import java.nio.file.Paths;

/**
 * Global constants.
 */
public final class Constants
{
	/**
	 * JUnit template file name.
	 */
    public static final String DEFAULT_TEMPLATE_FILE = "templates/JUnitProxy.java.vm";

	/**
	 * Root ATDD path.
	 */
    public static final String ROOT_ATDD_PATH = Paths.get("src","test","resources","atdd").toString();

	/**
	 * Path for tests suites.
	 */
    public static final String TEST_SUITES_PATH = Paths.get(ROOT_ATDD_PATH,"suites").toString();

	/**
	 * Path for keyword definitions.
	 */
    public static final String TEST_KEYWORDS_PATH = Paths.get(ROOT_ATDD_PATH,"keywords").toString();

	/**
	 * Name for reports dir.
	 */
    public static final String REPORTS_PATH = Paths.get("atdd", "reports").toString();

	/**
	 * Package for generated sources.
	 */
    public static final String GENERATED_SOURCES_PACKAGE = "generated";

	/**
	 * Path for generated sources.
	 */
    public static final String GENERATED_SOURCES_PATH = Paths.get("src","test","java", GENERATED_SOURCES_PACKAGE).toString();

	/**
	 * Path for external keyword definitions
	 */
	public static final String RESOURCE_PATH = Paths.get("target", "dependencies", "atdd", "keywords").toString();

    /**
     * Name of the test properties file used to create the binding.
     */
    public static final String BINDING_FILE = "local-test.properties";

    /**
     * Path to the test properties file used to create the binding.
     */
    public static final String BINDING_PATH = Paths.get("src","test", "resources", BINDING_FILE).toString();

    /**
     * Prefix for binding properties.
     */
    public static final String BINDING_PROPS_PREFIX = "atdd.";

    /**
     * Property that holds the default template file.
     */
    public static final String TEMPLATE_FILE_PROP_NAME = "templateFile";

	/**
	 * Property than holds path to keywords imported from dependencies
	 */
	public static final String KEYWORD_INCLUDE_PATH_NAME = "keywordIncludePath";

	/**
	 * Property that holds full search path for extra keywords
	 */
	public static final String KEYWORD_SEARCH_PATH_NAME = "keywordSearchPath";

	/**
	 * Tag test case with this value to skip the test.
	 */
	public static final String IGNORE_TAG = "Ignore";

	/**
	 * Prefix to add to all test case function names.
	 * Allows to distinguish same test cases ran from within different modules.
	 */
	public static final String TEST_CASE_PREFIX = "testPrefix";

	private Constants()
	{
	}
}
