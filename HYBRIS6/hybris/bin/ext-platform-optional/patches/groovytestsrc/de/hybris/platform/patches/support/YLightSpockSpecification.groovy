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

import de.hybris.platform.core.Registry
import de.hybris.platform.patches.utils.AppendSpringConfiguration
import de.hybris.platform.testframework.RunListeners
import de.hybris.platform.testframework.runlistener.LightPlatformRunListener
import org.apache.log4j.Logger
import org.junit.runner.RunWith
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.util.ReflectionUtils
import spock.lang.Specification

import javax.annotation.Resource
import java.lang.reflect.Field

/**
 * Activates the {@link YSpockHybrisRunner} for test processing and
 * registers the {@link LightPlatformRunListener} for setting correct tenant and cluster of test system.
 */
@RunWith(YSpockHybrisRunner.class)
@RunListeners([ LightPlatformRunListener.class ])
class YLightSpockSpecification extends Specification {
    private static final Logger LOG = Logger.getLogger(YLightSpockSpecification.class)

    private ApplicationContext applicationContext

    def setup() {
        final ApplicationContext parentContext = Registry.getApplicationContext()
        if (getClass().isAnnotationPresent(AppendSpringConfiguration.class)) {
            String[] springConfiguration = getClass().getAnnotation(AppendSpringConfiguration.class).value()
            applicationContext = new ClassPathXmlApplicationContext(springConfiguration, parentContext)
        } else {
            applicationContext = parentContext
        }
        autowireProperties(applicationContext)
    }

    protected void autowireProperties(final ApplicationContext applicationContext) {
        final Object test = this
        final AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory()
        final Set<String> missing = new LinkedHashSet<String>()

        ReflectionUtils.doWithFields(test.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
                final Resource resource = field.getAnnotation(Resource.class)
                if (resource != null) {
                    field.setAccessible(true)
                    Object bean = ReflectionUtils.getField(field, test)
                    if (bean == null) {
                        final String beanName = getBeanName(resource, field)
                        try {
                            bean = beanFactory.getBean(beanName, field.getType())
                            if (bean != null) {
                                ReflectionUtils.setField(field, test, bean)
                            }
                        } catch (final BeansException e) {
                            LOG.error("error fetching bean " + beanName + " : " + e.getMessage(), e)
                        }
                        if (bean == null) {
                            missing.add(field.getName())
                        }
                    }
                }
            }
        })
        if (!missing.isEmpty()) {
            throw new IllegalStateException("test " + getClass().getSimpleName()
                    + " is not properly initialized - missing bean references " + missing)
        }
    }

    protected String getBeanName(final Resource resource, final Field field) {
        if (resource.mappedName() != null && resource.mappedName().length() > 0) {
            return resource.mappedName()
        } else if (resource.name() != null && resource.name().length() > 0) {
            return resource.name()
        } else {
            return field.getName()
        }
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext
    }
}
