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

import com.hybris.atddengine.archetype.ATDDPomEnhancer;
import com.hybris.atddengine.archetype.ResourcesAppender;
import com.hybris.atddengine.tasks.Constants;
import com.hybris.atddengine.tasks.GenerateProxies;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ATDD tests processor.
 */
public final class Processor
{
    private static final Logger LOG = LoggerFactory.getLogger(Processor.class);

    private Processor()
    {

    }

    /**
     * Entry point.
     * @param args input arguments.
     * @throws Exception .
     */
    public static void main(final String[] args) throws Exception
    {
        if (args.length >= 1 && args[0].compareToIgnoreCase("generate") == 0)
        {
            final String path;
            if (args.length == 2)
            {
                path = args[1];
            }
            else
            {
                path = System.getProperty("user.dir");
            }

            generateProjectStructure(path);
        }
        else if (args.length >= 2) {
            LOG.info("ATDD Engine Processor started");

            final String projectRootPath = args[0];
            LOG.info("Project root: " + projectRootPath);

            final String buildPath = args[1];
            LOG.info("Project build path: " + buildPath);

            generateProxies(projectRootPath, buildPath);
        }
        else {
            LOG.info("Wrong input parameters");
        }

    }

    private static void generateProxies(String projectRootPath, String buildPath) throws Exception {
        final GenerateProxies proxyGenerator = new GenerateProxies();

        final String targetDir = projectRootPath + File.separator + Constants.GENERATED_SOURCES_PATH;

        FileUtils.deleteDirectory(new File(targetDir));

        proxyGenerator.setTargetDir(targetDir);
        proxyGenerator.setTestPath(projectRootPath + File.separator + Constants.TEST_SUITES_PATH);
        proxyGenerator.setReportsPath(buildPath + File.separator + Constants.REPORTS_PATH);
        proxyGenerator.setRootPackagePath(Constants.GENERATED_SOURCES_PACKAGE);
        proxyGenerator.setBindingFile(projectRootPath + File.separator + Constants.BINDING_PATH);
		proxyGenerator.setIncludePath(projectRootPath + File.separator + Constants.RESOURCE_PATH);
		proxyGenerator.setRootProjectPath(projectRootPath);

        proxyGenerator.execute();
    }

    private static void generateProjectStructure(final String path) {
        LOG.info("Start generating atdd structure");

        new ResourcesAppender().createStructure(path);
        LOG.info("Project structure generated successfully");

        new ATDDPomEnhancer().addConfigurationToPom(path);
        LOG.info("Pom file changed successfully");
    }


}
