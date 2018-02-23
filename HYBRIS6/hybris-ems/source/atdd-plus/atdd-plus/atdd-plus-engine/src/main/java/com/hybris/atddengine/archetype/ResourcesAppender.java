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

import com.hybris.atddengine.tasks.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates ATDD project structure.
 */
public class ResourcesAppender
{

    private static final Logger LOG = LoggerFactory.getLogger(ResourcesAppender.class);

    private static final String KEY_WORDS_FILE = "Keywords.txt";
    private static final String TEST_SUITES_FILE = "Hello_World.txt";
    private static final String KEY_WORDS_LIB_FILE = "KeyWordLibrary.java";

    private static final String RESOURCE_KEY_WORDS_PATH = Paths.get("atdd", "keywords", KEY_WORDS_FILE).toString();
    private static final String RESOURCE_TEST_SUITES_PATH = Paths.get("atdd", "suites", TEST_SUITES_FILE).toString();
    private static final String RESOURCE_KEY_WORDS_LIBRARY_PATH = Paths.get(
            "java","com","hybris","services","atddengine", KEY_WORDS_LIB_FILE
    ).toString();

    private static final String TEST_SOURCES = Paths.get("src","test").toString();

    /**
     * Creates ATDD project structure.
     * @param rootPath path to root project folder.
     */
    public void createStructure(final String rootPath)
    {
        try
        {
            FileUtils.writeStringToFile(
                    new File(rootPath + File.separator + Constants.TEST_KEYWORDS_PATH, KEY_WORDS_FILE),
                    readFromResources(RESOURCE_KEY_WORDS_PATH)
            );

            FileUtils.writeStringToFile(
                    new File(rootPath + File.separator + Constants.TEST_SUITES_PATH, TEST_SUITES_FILE),
                    readFromResources(RESOURCE_TEST_SUITES_PATH)
            );

            FileUtils.writeStringToFile(
                    new File(rootPath + File.separator + TEST_SOURCES, RESOURCE_KEY_WORDS_LIBRARY_PATH),
                    readFromResources(RESOURCE_KEY_WORDS_LIBRARY_PATH)
            );
        }
        catch(final IOException e)
        {
            LOG.error("Can't access file", e);
        }
    }

    private String readFromResources(final String path) throws IOException
    {
        final InputStream stream = ClassLoader.getSystemResourceAsStream(path);

        final StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer);
        final String ret = writer.toString();

        IOUtils.closeQuietly(writer);
        IOUtils.closeQuietly(stream);

        return ret;
    }
}
