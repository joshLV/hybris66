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

package com.hybris.atddengine.keywords.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Aspect for handling exceptions in ATDD keyword method.
 */
@Aspect
public class ExceptionLoggingAspect
{
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

	/**
	 * Inner class holding the only {@link ExceptionLoggingAspect} instance.
	 */
	private static final class InstanceHolder
	{
		public static final ExceptionLoggingAspect INSTANCE = new ExceptionLoggingAspect();
	}

	/**
	 * Private constructor to avoid instantiation.
	 */
	private ExceptionLoggingAspect()
	{
	}

	/**
	 * Factory method necessary for spring configuration.
	 * 
	 * @return new {@link ExceptionLoggingAspect} instance
	 */
	public static ExceptionLoggingAspect aspectOf()
	{
		return InstanceHolder.INSTANCE;
	}

	/**
	 * {@link Pointcut} definition for all ATDD keyword methods.
	 */
	@Pointcut("execution(public * *..*KeywordLibrary.*(..))")
	public void keywordLibrary()
	{
	}

	/**
	 * Advice for logging exceptions thrown in keyword methods.
	 * 
	 * @param exception
	 * @throws Throwable
	 *            any unexpected exception that is thrown in the execution of the adviced join point
	 */
	@AfterThrowing(pointcut = "keywordLibrary()", throwing = "exception")
	public void exceptionLoggingAdvice(final Throwable exception) throws Throwable
	{
		LOG.error("Exception thrown during keyword execution", exception);
	}
}
