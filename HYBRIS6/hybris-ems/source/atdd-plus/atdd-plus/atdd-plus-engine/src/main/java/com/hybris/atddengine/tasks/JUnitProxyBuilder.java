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

import com.hybris.atddengine.framework.RobotTest;
import com.hybris.atddengine.framework.RobotTestSuite;
import com.hybris.atddengine.framework.RobotTestSuiteFactory;
import com.hybris.atddengine.templates.TemplateProcessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.python.core.PyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds and saves JUnitProxy files.
 */
public class JUnitProxyBuilder
{
    private static final Logger LOG = LoggerFactory.getLogger(JUnitProxyBuilder.class);

    private static final String SUIT_MARKER = "Test";
    private static final String UTF_8 = "UTF-8";

    private String rootPackagePath;
	private String includePath;
    private File reportsPath;
    private File packageDir;
    private File testSuiteFile;
    private RobotTestSuiteFactory robotTestSuiteFactory;
    private TemplateProcessor templateProcessor;
    private Map<String, Object> binding;


    /**
     * Set package name for generated sources.
     *
     * @param rootPackagePathArg rootPackagePath root package path.
     * @return JUnitProxyBuilder.
     */
    public JUnitProxyBuilder setRootPackagePath(final String rootPackagePathArg)
    {
        this.rootPackagePath = rootPackagePathArg;
        return this;
    }

    /**
     * Set path for reports.
     *
     * @param reportsPathArg report path.
     * @return JUnitProxyBuilder.
     */
    public JUnitProxyBuilder setReportsPath(final File reportsPathArg)
    {
        this.reportsPath = reportsPathArg;
        return this;
    }

	public JUnitProxyBuilder setIncludePath(final String includePath)
	{
		this.includePath = includePath;
		return this;
	}

    /**
     * Sets package directory.
     * @param packageDirArg package directory.
     * @return JUnitProxyBuilder.
     */
    public JUnitProxyBuilder setPackageDir(final File packageDirArg)
    {
        this.packageDir = packageDirArg;
        return this;
    }

    /**
     * Sets test-suite file.
     * @param testSuiteFileArg test suite file.
     * @return JUnitProxyBuilder.
     */
    public JUnitProxyBuilder setTestSuiteFile(final File testSuiteFileArg)
    {
        this.testSuiteFile = testSuiteFileArg;
        return this;
    }

    /**
     * Sets test-suite.
     * @param robotTestSuiteFactoryArg test-suite entity.
     * @return JUnitProxyBuilder.
     */
    public JUnitProxyBuilder setRobotTestSuiteFactory(final RobotTestSuiteFactory robotTestSuiteFactoryArg)
    {
        this.robotTestSuiteFactory = robotTestSuiteFactoryArg;
        return this;
    }

    /**
     * Sets template processor.
     * @param templateProcessorArg template processor.
     * @return JUnitProxyBuilder.
     */
    public JUnitProxyBuilder setTemplateProcessor(final TemplateProcessor templateProcessorArg)
    {
        this.templateProcessor = templateProcessorArg;
        return this;
    }

    /**
     * Sets the binding.
     * @param arg the extended properties
     * @return JUnitProxyBuilder.
     */
    public JUnitProxyBuilder setBinding(final HashMap<String, Object> arg)
	{
        binding = arg;
        return this;
    }

    /**
     * builds and save JUnitProxy file.
     * @throws Exception .
     * @return JUnitProxy file path.
     */
    public String build() throws Exception
    {
        checkEnvironment();

        String retProxyPath = StringUtils.EMPTY;
		Writer writer = null;
		try
        {
            final RobotTestSuite robotTestSuite = robotTestSuiteFactory.parseTestSuite(testSuiteFile);

            final String testSuiteName = formatTestSuitName(
                    robotTestSuite.getName().replaceAll(Pattern.quote(" "), "_")
            );

            final Map<String, Object> templateBinding = new HashMap<String, Object>();

            templateBinding.put("packageName", rootPackagePath);
            templateBinding.put("testSuiteName", testSuiteName);
            templateBinding.put("testSuitePath", testSuiteFile.getPath().replaceAll(Pattern.quote(File.separator), "/"));
            templateBinding.put("reportDirectory", reportsPath.getPath().replaceAll(Pattern.quote(File.separator), "/"));
            templateBinding.put(Constants.KEYWORD_INCLUDE_PATH_NAME, includePath.replaceAll(Pattern.quote(File.separator), "/"));

            templateBinding.putAll(this.binding);

            templateBinding.put("tests", robotTestSuite.getRobotTests());

            final File targetFile = new File(packageDir, testSuiteName + ".java");

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), UTF_8));
            templateProcessor.processTemplate(writer, getTemplateFile(), templateBinding);
            writer.close();

            retProxyPath = targetFile.getPath();
        }
        catch (final PyException e)
        {
            LOG.warn(String.format("Test suite file [%s] is malformed and will be ignored.", testSuiteFile.getName()));
        }
		finally
		{
			IOUtils.closeQuietly(writer);
		}

        return retProxyPath;
    }

    private String getTemplateFile()
	{
        final String templateFile;
        final Object templateObject = binding.get(Constants.TEMPLATE_FILE_PROP_NAME);
        if (templateObject == null)
		{
            templateFile = Constants.DEFAULT_TEMPLATE_FILE;
        }
        else
		{
            templateFile = templateObject.toString();
        }
        return templateFile;
    }

    private void checkEnvironment() throws IllegalArgumentException
    {
        if (rootPackagePath == null)
        {
            throw new IllegalArgumentException("Package path didn't initialized");
        }

        if (testSuiteFile == null)
        {
            throw new IllegalArgumentException("Test suite file didn't initialized");
        }

        if (reportsPath == null)
        {
            throw new IllegalArgumentException("Reports path didn't initialized");
        }

        if (robotTestSuiteFactory == null)
        {
            throw new IllegalArgumentException("Robot test suite factory didn't initialized");
        }

        if (packageDir == null)
        {
            throw new IllegalArgumentException("Package dir didn't initialized");
        }

        if (templateProcessor == null)
        {
            throw new IllegalArgumentException("Template processor didn't initialized");
        }
    }

    private String formatTestSuitName(final String name)
    {
        if (name.startsWith(SUIT_MARKER) || name.endsWith(SUIT_MARKER))
        {
            return name;
        }

        return SUIT_MARKER + name;
    }
}
