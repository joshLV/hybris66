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

package com.hybris.atddengine;

import com.hybris.atddengine.tasks.Constants;

import java.io.File;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for Processor.
 */
@Ignore("Bamboo fails when build from ZIP")
public class ProcessorTest
{
    private static final String GENERATED_PROXY = "TestHello_World.java";
    private static final String ROOT_PROJECT_DIR = "user.dir";

	@Ignore("Bamboo fails when build from ZIP")
    @Test
    public void mainTest() throws Exception
    {
        final String rootDir = System.getProperty(ROOT_PROJECT_DIR);

        Processor.main(new String[]{rootDir});

        final File generatedFile = new File(
                rootDir + File.separatorChar + Constants.GENERATED_SOURCES_PATH + File.separator + GENERATED_PROXY
        );

        Assert.assertTrue(generatedFile.exists());
    }
}
