/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cmsfacades.common.validator.impl;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrors;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.servicelayer.session.SessionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultValidationErrorsProviderTest
{
	@Mock
	private SessionService sessionService;

	@Mock
	private ObjectFactory<ValidationErrors> validationErrorsObjectFactory;

	@Spy
	@InjectMocks
	private DefaultValidationErrorsProvider validationErrorsProvider;
	
	@Before
	public void setup()
	{
		when(validationErrorsObjectFactory.getObject()).thenReturn(new DefaultValidationErrors());
	}

	@Test(expected=IllegalStateException.class)
	public void testWhenValidationErrorsAreNotInitialized_shouldThrowException()
	{
		validationErrorsProvider.getCurrentValidationErrors();
	}

	@Test
	public void testWhenValidationErrorsAreInitialized_andNoStackIsStored_shouldCreateValidationErrorsStackAndStoreInSession()
	{
		// Arrange
		ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);

		// Act
		final ValidationErrors validationErrors = validationErrorsProvider.initializeValidationErrors();

		// Assert
		verify(sessionService).setAttribute(eq(CmsfacadesConstants.SESSION_VALIDATION_ERRORS_OBJ), argumentCaptor.capture());
		Deque<ValidationErrors> stack = validationErrorsProvider.getWrappedStack(argumentCaptor.getValue());
		assertEquals(stack.peek(), validationErrors);
	}

	@Test
	public void testWhenValidationErrorsAreInitialized_andAStackIsStored_shouldPushToExistingStack()
	{
		// Arrange
		LinkedList<ValidationErrors> stack = new LinkedList<>();
		when(sessionService.getAttribute(CmsfacadesConstants.SESSION_VALIDATION_ERRORS_OBJ)).thenReturn(new AtomicReference<>(stack));

		// Act
		final ValidationErrors validationErrors = validationErrorsProvider.initializeValidationErrors();

		// Assert
		assertEquals(stack.get(0), validationErrors);
	}

	@Test
	public void WhenGetCurrentValidationErrorsIsCalled_thenValidationErrorsAreRetrieved()
	{
		// Arrange
		ValidationErrors expectedValidationErrors = validationErrorsObjectFactory.getObject();
		LinkedList<ValidationErrors> stack = new LinkedList<>();
		stack.push(expectedValidationErrors);
		when(sessionService.getAttribute(CmsfacadesConstants.SESSION_VALIDATION_ERRORS_OBJ)).thenReturn(new AtomicReference<>(stack));

		// Act
		ValidationErrors actualValidationErrors = validationErrorsProvider.getCurrentValidationErrors();

		// Assert
		verify(sessionService, never()).setAttribute(eq(CmsfacadesConstants.SESSION_VALIDATION_ERRORS_OBJ), Matchers.any());
		assertEquals(expectedValidationErrors, actualValidationErrors);
		assertEquals(stack.size(), 1);
	}


	@Test
	public void WhenFinalizeValidationErrorsIsCalled_thenValueIsRemovedFromStack()
	{
		// Arrange
		ValidationErrors expectedValidationErrors = validationErrorsObjectFactory.getObject();
		LinkedList<ValidationErrors> stack = new LinkedList<>();
		stack.push(expectedValidationErrors);
		when(sessionService.getAttribute(CmsfacadesConstants.SESSION_VALIDATION_ERRORS_OBJ)).thenReturn(new AtomicReference<>(stack));

		// Act
		validationErrorsProvider.finalizeValidationErrors();

		// Assert
		verify(sessionService, never()).setAttribute(eq(CmsfacadesConstants.SESSION_VALIDATION_ERRORS_OBJ), Matchers.any());
		assertEquals(stack.size(), 0);
	}
	
}
