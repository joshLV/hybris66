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

package com.hybris.atdd.resourcepathsample.keywords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class TestKeywordLibrary
{
	private static final Logger LOG = LoggerFactory.getLogger(TestKeywordLibrary.class);

	public void writeText(final String text)
	{
		LOG.info(text);
	}
}
