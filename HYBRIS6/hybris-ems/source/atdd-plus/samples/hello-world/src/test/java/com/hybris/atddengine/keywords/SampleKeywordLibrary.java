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

package com.hybris.atddengine.keywords;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Sample key words library.
 */
public class SampleKeywordLibrary
{
    private static final Logger LOG = LoggerFactory.getLogger( SampleKeywordLibrary.class);

    /**
     * Hello world with arguments.
     * @param arg argument.
     */
    public void helloWorld(final String arg)
    {
    	if(StringUtils.isBlank(arg)) {
    		throw new IllegalArgumentException("String argument may not be blank");
    	}
        LOG.info("KeyWordLibrarySample - Hello world: " + arg);
    }

    /**
     * Hello world with no arguments.
     */
    public void helloWorldNoParameters()
    {
        LOG.info("KeyWordLibrarySample - Hello world: No Parameters");
    }
}
