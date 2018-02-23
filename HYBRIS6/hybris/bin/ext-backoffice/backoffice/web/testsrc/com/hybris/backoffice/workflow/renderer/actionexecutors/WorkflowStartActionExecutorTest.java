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
package com.hybris.backoffice.workflow.renderer.actionexecutors;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.hybris.platform.workflow.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.workflow.WorkflowConstants;
import com.hybris.backoffice.workflow.WorkflowEventPublisher;
import com.hybris.backoffice.workflow.WorkflowFacade;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowStartActionExecutorTest
{
	@Mock
	private WorkflowEventPublisher workflowEventPublisher;
	@Mock
	private WorkflowFacade workflowFacade;
	@Mock
	private WorkflowModel data;

	@Spy
	private WorkflowStartActionExecutor workflowStartActionExecutor;

	@Before
	public void setUp()
	{
		workflowStartActionExecutor.setWorkflowEventPublisher(workflowEventPublisher);
		workflowStartActionExecutor.setWorkflowFacade(workflowFacade);
	}

	@Test
	public void shouldStartingWorkflowPublishGlobalEventAndNotifyUserAboutSuccess()
	{
		// given
		doReturn(true).when(workflowFacade).startWorkflow(data);
		// when
		final Boolean result = workflowStartActionExecutor.apply(data);
		// then
		verify(workflowEventPublisher).publishWorkflowUpdatedEvent(data);
		verify(workflowStartActionExecutor).notifyUser(data, WorkflowConstants.EVENT_TYPE_WORKFLOW_STARTED,
				NotificationEvent.Level.SUCCESS);
		assertThat(result).isTrue();
	}

	@Test
	public void shouldStartingWorkflowWithAProblemNotifyUserAboutFailure()
	{
		// given
		doReturn(false).when(workflowFacade).startWorkflow(data);
		// when
		final Boolean result = workflowStartActionExecutor.apply(data);
		// then
		verify(workflowStartActionExecutor).notifyUser(data, WorkflowConstants.EVENT_TYPE_WORKFLOW_STARTED,
				NotificationEvent.Level.FAILURE);
		assertThat(result).isFalse();
	}
}
