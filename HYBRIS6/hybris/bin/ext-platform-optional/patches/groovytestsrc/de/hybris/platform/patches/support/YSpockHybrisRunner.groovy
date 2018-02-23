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
package de.hybris.platform.patches.support

import de.hybris.platform.core.Log4JUtils
import de.hybris.platform.testframework.*
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger
import org.junit.runner.Result
import org.junit.runner.manipulation.Filter
import org.junit.runner.manipulation.NoTestsRemainException
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.RunNotifier
import org.junit.runners.model.InitializationError
import org.spockframework.runtime.Sputnik
import org.spockframework.runtime.model.SpecInfo
import org.springframework.util.ReflectionUtils

import java.lang.reflect.Method

/**
 * Hybris runner for Spock tests
 */
//CHECKSTYLE:OFF
@SuppressWarnings("PMD")
public class YSpockHybrisRunner extends Sputnik {
    static
    {
        if (StringUtils.isBlank(System.getProperty("HYBRIS_BIN_DIR"))) {
            String platformHome = System.getProperty("platformhome")
            if (StringUtils.isBlank(platformHome)) {
                throw new IllegalStateException("platformhome must be set")
            }

            String classPathFilePath = UnitTestClassLoaderUtils.createDefaultClassPathFilePath(platformHome)
            ClasspathGenerator.generate(platformHome, platformHome.replace(UnitTestClassLoaderUtils.BIN_PLATFORM_PATH, UnitTestClassLoaderUtils.TEMP_PLATFORM_PATH))
            UnitTestClassLoaderUtils.addJunitClasspathUrls(Thread.currentThread().getContextClassLoader(), classPathFilePath)
        }

        Log4JUtils.startup()
    }
    /** Used logger instance. */
    private static final Logger LOG = Logger.getLogger(HybrisJUnit4ClassRunner.class)
    /** List of registered listeners determined from annotations of test class. */
    private final Map<Class<? extends RunListener>, ? extends RunListener> listeners
    /** Object holding tests results */
    private final Result result = new Result()

    private Filter nonAnnotationFilter = null

    /**
     * @param clazz
     * @throws InitializationError
     */
    public YSpockHybrisRunner(Class<?> clazz) throws InitializationError {
        super(clazz)
        this.listeners = determineListeners()
    }

    @Override
    public void filter(final Filter filter) throws NoTestsRemainException {
        // need to keep a reference to the standard filter since ...
        if (!(filter instanceof DefaultAnnotationFilter)) {
            this.nonAnnotationFilter = filter
        }
        // ... super.filter internally replaces the filter by DefaultAnnotationFilter
        // but we still depend on the information in the default one!
        super.filter(filter)
    }

    /* (non-Javadoc)
     * @see org.spockframework.runtime.Sputnik#run(org.junit.runner.notification.RunNotifier)
     */

    @Override
    public void run(RunNotifier notifier) {
        // Filtering tests -> must pass current filter to preserve method level filtering
        final Filter filter = new DefaultAnnotationFilter(this.nonAnnotationFilter)
        try {
            this.filter(filter)
        }
        catch (final NoTestsRemainException e) {
            //nothing to test - all tests are skipped because of DefaultAnnotationFilter
            return

        }

        // wrap the notifier to call all listeners afters in chain mode
        final ChainingRunNotifierWrapper wrappedNotifier = new ChainingRunNotifierWrapper(notifier)

        // register listeners
        for (final RunListener listener : listeners.values()) {
            wrappedNotifier.addListener(listener)
            if (LOG.isDebugEnabled()) {
                LOG.debug("Registered " + listener.getClass().getName() + " as run listener")
            }
        }

        try {
            // as testRunStarted was already called on delegates we must call it for wrapped ones
            wrappedNotifier.fireTestRunStarted(getDescription())

            super.run(wrappedNotifier)
        }
        finally {
            // as testRunFinished will be called on delegates we must call it for wrapped ones only
            wrappedNotifier.fireTestRunFinished(result)

            // remove listeners to not get called for finished test run (in wrong order)
            wrappedNotifier.removeAllListeners()
        }
    }

    /**
     * Determines all listeners configured for test method.
     *
     * @throws InitializationError
     *            error while instantiation of the listeners
     */
    private Map<Class<? extends RunListener>, RunListener> determineListeners() throws InitializationError {
        final Map<Class<? extends RunListener>, RunListener> resultingListeners = new LinkedHashMap<Class<? extends RunListener>, RunListener>()

        final RunListener resultListener = this.result.createListener()
        resultingListeners.put(RunListener.class, resultListener)

        Method method = ReflectionUtils.findMethod(getClass().getSuperclass(), "getSpec")
        ReflectionUtils.makeAccessible(method)
        SpecInfo specInfo = (SpecInfo) ReflectionUtils.invokeMethod(method, this)

        Class curClass = specInfo.getReflection()
        while (curClass != null) {
            final RunListeners listenerAnno = (RunListeners) curClass.getAnnotation(RunListeners.class)
            if (listenerAnno != null) {
                for (final Class<? extends RunListener> listener : listenerAnno.value()) {
                    try {
                        resultingListeners.put(listener, listener.newInstance())
                    }
                    catch (final InstantiationException e) {
                        throw new InitializationError(Collections.singletonList((Throwable) e))
                    }
                    catch (final IllegalAccessException e) {
                        throw new InitializationError(Collections.singletonList((Throwable) e))
                    }
                }
            }
            curClass = curClass.getSuperclass()
        }
        return resultingListeners
    }
}
//CHECKSTYLE:ON