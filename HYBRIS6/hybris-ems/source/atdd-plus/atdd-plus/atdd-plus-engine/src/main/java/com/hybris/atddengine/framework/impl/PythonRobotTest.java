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


import com.hybris.atddengine.framework.PythonWrapper;
import com.hybris.atddengine.framework.RobotTest;
import com.hybris.atddengine.framework.RobotTestResult;
import com.hybris.atddengine.framework.RobotTestSuite;

import com.hybris.atddengine.tasks.Constants;
import org.python.core.PyList;
import org.python.core.PyObject;


/**
 * Python robot tests.
 */
public class PythonRobotTest extends AbstractPythonAware implements RobotTest, PythonWrapper
{
	private final RobotTestSuite robotTestSuite;

	private final PyObject test;

	/**
	 * Constructor.
	 *
	 * @param robotTestSuite robot test suite
	 * @param test test
	 */
	public PythonRobotTest(final PythonRobotTestSuite robotTestSuite, final PyObject test)
	{
		super(robotTestSuite);

		this.robotTestSuite = robotTestSuite;
		this.test = test;
	}

    public boolean isIgnored()
	{
		final PyObject tags1 = test.__getattr__("tags");
		final PyList tags = (PyList) tags1;
		for (final Object tag : tags)
		{
			if (Constants.IGNORE_TAG.equals(tag))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName()
	{
		return test.__getattr__("name").asString();
	}

	@Override
	public PyObject getPyObject()
	{
		return test;
	}

	@Override
	public RobotTestSuite getTestSuite()
	{
		return robotTestSuite;
	}

	@Override
	public RobotTestResult run()
	{
		if (getTestSuite().isStarted())
		{
			if (getTestSuite().isClosed())
			{
				throw new IllegalStateException("Parent robotTestSuite has already been closed.");
			}
			else
			{
				return getTestSuite().run(this);
			}
		}
		else
		{
			throw new IllegalStateException("Parent robotTestSuite has not been started yet.");
		}
	}
}
