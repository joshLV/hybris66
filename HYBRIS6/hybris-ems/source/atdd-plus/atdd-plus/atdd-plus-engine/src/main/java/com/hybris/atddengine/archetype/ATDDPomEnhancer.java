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

package com.hybris.atddengine.archetype;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *This class adds ATDD support to existing POM file.
 */
public class ATDDPomEnhancer
{
    private static final Logger LOG = LoggerFactory.getLogger(ATDDPomEnhancer.class);

    private static final String POM = "pom.xml";
    private static final String OLD_POM = "pom-old.xml";
    private static final String UTF_8 = "UTF-8";

    private static final String ATDD_MVN_ARTIFACT_ID = "atdd";
    private static final String ATDD_MVN_GROUP_ID = "com.hybris.atdd";
    private static final String ATDD_MVN_VERSION = "0.2.2-SNAPSHOT";
    private static final String ATDD_MVN_TYPE = "jar";
    private static final String ATDD_MVN_SCOPE = "test";

    private static final String ATDD_MVN_EXEC_PLUGIN_ID = "build-dump";
    private static final String ATDD_MVN_EXEC_PLUGIN_PHASE = "generate-sources";
    private static final String ATDD_MVN_EXEC_PLUGIN_GOAL = "java";

    private static final String ATDD_MVN_PLUGIN_GROUP_ID = "org.codehaus.mojo";
    private static final String ATDD_MVN_PLUGIN_ARTIFACT_ID = "exec-maven-plugin";
    private static final String ATDD_MVN_PLUGIN_VERSION = "1.2";

    private static final String ATDD_MVN_PLUGIN_CONFIGURATION = ( //There isn't another way to add configuration
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<configuration>\n" +
            "  <mainClass>com.hybris.atddengine.Processor</mainClass>\n" +
            "  <arguments>\n" +
            "    <argument>${basedir}</argument>\n" +
            "  </arguments>\n" +
            "  <classpathScope>test</classpathScope>\n" +
            "</configuration>"
    );

    /**
     * Adds ATDD support to existing POM file.
     * @param rootPath path to pom file.
     */
    public void addConfigurationToPom(final String rootPath)
    {
        Reader targetReader = null;
        try
        {
            final File targetPomFile = new File(rootPath + File.separator + POM);
            final File copyPomFile = new File(rootPath + File.separator + OLD_POM);
            FileUtils.copyFile(targetPomFile, copyPomFile);

            targetReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(targetPomFile), UTF_8)
            );

            final Model targetModel = new MavenXpp3Reader().read(targetReader);
            final MavenProject targetProject = new MavenProject(targetModel);

            targetProject.getDependencies().add(
                    createATDDEngineDependency()
            );

            targetProject.getBuildPlugins().add(
                    createBuildPluginFeature()
            );

            final ModelWriter writer = new DefaultModelWriter();
            writer.write(targetPomFile, Collections.<String, Object>emptyMap(), targetModel);
        }
        catch(final IOException e)
        {
            LOG.error("Can't access file", e);
        }
        catch(final XmlPullParserException e)
        {
            LOG.error("Error invalid POM file format", e);
        }
        finally
        {
            IOUtils.closeQuietly(targetReader);
        }
    }

    private Dependency createATDDEngineDependency()
    {
        final Dependency dependency = new Dependency();
        dependency.setArtifactId(ATDD_MVN_ARTIFACT_ID);
        dependency.setGroupId(ATDD_MVN_GROUP_ID);
        dependency.setScope(ATDD_MVN_SCOPE);
        dependency.setVersion(ATDD_MVN_VERSION);
        dependency.setType(ATDD_MVN_TYPE);

        return dependency;
    }

    private Plugin createBuildPluginFeature() throws IOException, XmlPullParserException
    {
        final PluginExecution execution = new PluginExecution();
        execution.setId(ATDD_MVN_EXEC_PLUGIN_ID);
        execution.setPhase(ATDD_MVN_EXEC_PLUGIN_PHASE);
        execution.addGoal(ATDD_MVN_EXEC_PLUGIN_GOAL);

        final Plugin plugin = new Plugin();
        plugin.setGroupId(ATDD_MVN_PLUGIN_GROUP_ID);
        plugin.setArtifactId(ATDD_MVN_PLUGIN_ARTIFACT_ID);
        plugin.setVersion(ATDD_MVN_PLUGIN_VERSION);

        plugin.addExecution(execution);
        plugin.setConfiguration(
                Xpp3DomBuilder.build(new StringReader(ATDD_MVN_PLUGIN_CONFIGURATION))
        );

        return plugin;
    }
}
