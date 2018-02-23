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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.atddengine.framework.RobotTestSuiteFactory;
import com.hybris.atddengine.framework.impl.DefaultPythonProvider;
import com.hybris.atddengine.framework.impl.PythonRobotTestSuiteFactory;
import com.hybris.atddengine.templates.TemplateProcessorFactory;
import com.hybris.atddengine.templates.impl.VelocityTemplateProcessorFactory;


/**
 * This class generates JUnitProxy files.
 */
public class GenerateProxies
{
	private static final Logger LOG = LoggerFactory.getLogger(GenerateProxies.class);

	private RobotTestSuiteFactory testSuiteFactory;
	private File targetDir;
	private File testPath;
	private String includePath;
	private TemplateProcessorFactory templateProcessorFactory;
	private String rootPackagePath;
	private String rootProjectPath;
	private File reportsPath;
	private HashMap<String, Object> binding;

	/**
	 * Starts generating process.
	 * 
	 * @throws Exception .
	 */
	public void execute() throws Exception
	{
		final String searchPath = getFullSearchPath();
		final DefaultPythonProvider pythonAware = new DefaultPythonProvider(searchPath, includePath);
		setRobotTestSuiteFactory(new PythonRobotTestSuiteFactory(pythonAware));
		setTemplateProcessorFactory(new VelocityTemplateProcessorFactory());
		processRobotDir(testPath);
	}

	private String getFullSearchPath()
	{
		final Object searchPathObject = binding.get(Constants.KEYWORD_SEARCH_PATH_NAME);
		if (searchPathObject == null)
		{
			return null;
		}
		final String searchPath = searchPathObject.toString();
		if (Paths.get(searchPath).isAbsolute())
		{
			return searchPath;
		}
		else
		{
			return Paths.get(rootProjectPath, searchPath).normalize().toString().replaceAll(Pattern.quote(File.separator), "/");
		}
	}

	private List<File> collectTestSuiteFiles(final File projectDir)
	{
		final List<File> testSuiteFiles = new ArrayList<File>();

		for (final File testSuiteFile : projectDir.listFiles())
		{
			if (testSuiteFile.isFile())
			{
				testSuiteFiles.add(testSuiteFile);
			}
			else
			{
				if (!".svn".equalsIgnoreCase(testSuiteFile.getName()))
				{
					final String message = String.format("Subdirectories are currently not supported. Skipping %s", testSuiteFile);
					LOG.warn(message);
				}
			}
		}

		return testSuiteFiles;
	}

	private void generateJUnitProxy(final File packageDir, final File testSuiteFile) throws Exception
	{
		applyDefaultBindings();
		new JUnitProxyBuilder().setPackageDir(packageDir).setTestSuiteFile(testSuiteFile).setReportsPath(reportsPath)
				.setRobotTestSuiteFactory(testSuiteFactory).setRootPackagePath(rootPackagePath).setIncludePath(includePath)
				.setTemplateProcessor(templateProcessorFactory.createTemplateProcessor()).setBinding(binding).build();
	}

	private HashMap<String, Object> createBinding(final File bindingFile)
	{
		final HashMap<String, Object> result = new HashMap<>();

		if (bindingFile.exists() && bindingFile.isFile())
		{
			FileInputStream inputStream = null;

			try
			{
				inputStream = new FileInputStream(bindingFile);
				final Properties props = new Properties();
				props.load(inputStream);

				for (final String key : props.stringPropertyNames())
				{
					if (key.startsWith(Constants.BINDING_PROPS_PREFIX))
					{
						result.put(key.substring(Constants.BINDING_PROPS_PREFIX.length()), props.get(key));
					}
				}
			}
			catch (final IOException e)
			{
				LOG.error("Failed to load binding from {}!", bindingFile);
			}
			finally
			{
				IOUtils.closeQuietly(inputStream);
			}
		}

		return result;
	}

	private void generateJUnitTests(final List<File> testSuiteFiles) throws Exception
	{
		if (!targetDir.exists() && !targetDir.mkdirs())
		{
			final String message = String.format("Failed to create packageDirectory %s!", targetDir);
			throw new IOException(message);
		}

		cleanDirectory(targetDir);

		for (final File testSuiteFile : testSuiteFiles)
		{
			generateJUnitProxy(targetDir, testSuiteFile);
		}
	}

	private void processProjectDir(final File projectDir) throws Exception
	{
		if (projectDir.isDirectory())
		{
			final List<File> testSuiteFiles = collectTestSuiteFiles(projectDir);

			if (!testSuiteFiles.isEmpty())
			{
				generateJUnitTests(testSuiteFiles);
			}
		}
	}

	private void processRobotDir(final File robotDir) throws Exception
	{
		if (robotDir.isDirectory())
		{
			processProjectDir(robotDir);
		}
		else
		{
			final String message = String.format("%s is not a directory. Skipping!", robotDir);
			LOG.error(message);
		}
	}

	private void cleanDirectory(final File dir)
	{
		if (dir.exists())
		{
			for (final File file : dir.listFiles())
			{
				if (!file.delete())
				{
					LOG.warn("Can't remove file: {}", file.getName());
				}
			}
		}
	}

	private void setRobotTestSuiteFactory(final RobotTestSuiteFactory robotTestSuiteFactory)
	{
		this.testSuiteFactory = robotTestSuiteFactory;
	}


	/**
	 * Set path for test suites files.
	 * 
	 * @param testPath
	 *           test suite path
	 */
	public void setTestPath(final String testPath)
	{
		this.testPath = new File(testPath);
	}

	public void setIncludePath(final String includePath)
	{
		this.includePath = includePath;
	}

	/**
	 * Set path for generated sources.
	 * 
	 * @param targetDir
	 *           target directory
	 */
	public void setTargetDir(final String targetDir)
	{
		this.targetDir = new File(targetDir);
	}

	/**
	 * Set package name for generated sources.
	 * 
	 * @param rootPackagePath
	 *           rootPackagePath root package path
	 */
	public void setRootPackagePath(final String rootPackagePath)
	{
		this.rootPackagePath = rootPackagePath;
	}

	/**
	 * Set path for reports.
	 * 
	 * @param reportsPath
	 *           report path.
	 */
	public void setReportsPath(final String reportsPath)
	{
		this.reportsPath = new File(reportsPath);
	}

	/**
	 * Set the binding file
	 * 
	 * @param bindingFilePath
	 *           the path to the binding file
	 */
	public void setBindingFile(final String bindingFilePath)
	{
		binding = createBinding(new File(bindingFilePath));
	}

	private void applyDefaultBindings()
	{
		binding.put(Constants.KEYWORD_SEARCH_PATH_NAME, getFullSearchPath());
		if (!binding.containsKey(Constants.TEST_CASE_PREFIX))
		{
			binding.put(Constants.TEST_CASE_PREFIX, "");
		}
	}

	private void setTemplateProcessorFactory(final TemplateProcessorFactory templateProcessorFactory)
	{
		this.templateProcessorFactory = templateProcessorFactory;
	}

	public void setRootProjectPath(final String path)
	{
		rootProjectPath = path.replaceAll("\\\\", "/");
	}
}
