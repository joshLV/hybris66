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

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default pyhton provider.
 */
public class DefaultPythonProvider extends AbstractPythonAware
{
	Logger logger = LoggerFactory.getLogger(DefaultPythonProvider.class);

	/**
	 * Constructor.
	 * 
	 * @throws IOException
	 *            in case of i/o error
	 */
	public DefaultPythonProvider() throws IOException
	{
		super(new PythonInterpreter());
		loadRobotModules();
	}

	/**
	 * Constructor.
	 * 
	 * @param searchPath
	 *           directories that hold external keyword libraries
	 * @throws IOException
	 *            in case of i/o error
	 */
	public DefaultPythonProvider(final String... searchPath) throws IOException
	{
		this();
		locateKeywordLibraries(searchPath);
	}

	private void loadRobotModules()
	{
		getPythonInterpreter().exec("import robot");
		getPythonInterpreter().exec("from robot.conf import RobotSettings");
		getPythonInterpreter().exec("from robot.variables import init_global_variables");
		getPythonInterpreter().exec("from robot.running.runerrors import SuiteRunErrors");
		getPythonInterpreter().exec("from robot.running import TestSuite");
		getPythonInterpreter().exec("from robot.output import Output");
		getPythonInterpreter().exec("from robot.reporting import ResultWriter");
	}

	private void locateKeywordLibraries(final String[] searchPaths) throws IOException
	{
		getPythonInterpreter().exec("import sys");
		getPythonInterpreter().exec("from os.path import abspath");

		for (final String path : searchPaths)
		{
			if (!StringUtils.isBlank(path))
			{
				final File file = new File(path);
				if (file.isDirectory())
				{
					logger.info("Adding search path '{}'", path);
					getPythonInterpreter().exec("sys.path.append(abspath('" + path.replaceAll("\\\\", "/") + "'))");
				}
				else
				{
					logger.warn("Search path '{}' does not exist", path);
				}
			}
		}
	}
}
