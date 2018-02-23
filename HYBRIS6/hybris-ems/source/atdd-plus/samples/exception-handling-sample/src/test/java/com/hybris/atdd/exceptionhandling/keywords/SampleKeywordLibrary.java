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

package com.hybris.atdd.exceptionhandling.keywords;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;


/**
 * Sample keyword library for exception handling.
 */
@Configurable
public class SampleKeywordLibrary
{
	private static final Logger LOG = LoggerFactory.getLogger(SampleKeywordLibrary.class);

	/**
	 * Sample keyword which always throws a {@link NotImplementedException}
	 */
	public void notImplementedKeyword()
	{
		throw new NotImplementedException("This keyword has not been implemented yet.");
	}

	public void sayHelloTo(final String name)
	{
		if (StringUtils.isBlank(name))
		{
			throw new IllegalArgumentException("Parameter name may not be null");
		}
		LOG.info("Hello, {}!", name);
	}
}
