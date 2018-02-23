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

import org.python.util.PythonInterpreter;


/**
 * Python interface.
 */
public abstract class AbstractPythonAware implements PythonAware
{
	private final PythonInterpreter pythonInterpreter;

	/**
	 * Copy constructor.
	 *
	 * @param pythonAware source
	 */
	public AbstractPythonAware(final PythonAware pythonAware)
	{
		this(pythonAware.getPythonInterpreter());
	}

	/**
	 * Constructor.
	 *
	 * @param pythonInterpreter python interpreter
	 */
	public AbstractPythonAware(final PythonInterpreter pythonInterpreter)
	{
		this.pythonInterpreter = pythonInterpreter;
	}

	@Override
	public PythonInterpreter getPythonInterpreter()
	{
		return pythonInterpreter;
	}
}
