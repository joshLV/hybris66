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
package de.hybris.platform.b2b.process.salesquote.actions;

import de.hybris.platform.b2b.enums.WorkflowTemplateType;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.process.approval.actions.AbstractProceduralB2BOrderAproveAction;
import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.b2b.services.B2BApproverService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2b.services.B2BWorkflowIntegrationService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @deprecated Since 6.3. Please see quote functionality from commerce.
 */
@Deprecated
public class StartSalesQuoteWorkflow extends AbstractProceduralB2BOrderAproveAction
{
	private static final Logger LOG = Logger.getLogger(StartSalesQuoteWorkflow.class);
	private B2BWorkflowIntegrationService b2bWorkflowIntegrationService;
	private UserService userService;
	public B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	public B2BApproverService<B2BCustomerModel> b2bApproverService;
	private WorkflowProcessingService workflowProcessingService;
	private WorkflowService workflowService;

	@Override
	public void executeAction(final B2BApprovalProcessModel process) throws RetryLaterException
	{
		final OrderModel order = process.getOrder();

		final Collection<UserModel> accountManagerApprovers = getB2bApproverService().getAccountManagerApprovers(order.getUnit());
		//Since the process can be triggered without sales rep assigned to the unit default to an administrator
		// user if no sales rep
		final UserModel accountManager = (UserModel) ObjectUtils.defaultIfNull(
				getB2bUnitService().getAccountManagerForUnit(order.getUnit()), getUserService().getAdminUser());


		final List<UserModel> userModelList = (List<UserModel>) CollectionUtils.union(accountManagerApprovers,
				Collections.singletonList(accountManager));

		if (LOG.isDebugEnabled())
		{
			final List<String> approverUids = new ArrayList<String>();
			for (final UserModel userModel : userModelList)
			{
				approverUids.add(userModel.getUid());
			}
			LOG.debug(String.format("Creating a sales quote worflow for order %s and approvers %s", order.getCode(), approverUids));
		}

		final String workflowTemplateCode = getB2bWorkflowIntegrationService().generateWorkflowTemplateCode("B2B_Quote_",
				userModelList);
		final WorkflowTemplateModel workflowTemplate = getB2bWorkflowIntegrationService().createWorkflowTemplate(userModelList,
				workflowTemplateCode,
				String.format("Customer %s is about to reach credit limit for order %s ", accountManager.getUid(), order.getCode()),
				WorkflowTemplateType.SALES_QUOTES);

		final WorkflowModel workflow = getWorkflowService().createWorkflow(workflowTemplate.getName(), workflowTemplate,
				Arrays.asList(new ItemModel[]
		{ process, order }), workflowTemplate.getOwner());
		getWorkflowProcessingService().startWorkflow(workflow);
		getModelService().saveAll(); // workaround for PLA-10938
		order.setWorkflow(workflow);
		order.setStatus(OrderStatus.PENDING_QUOTE);
		getModelService().save(order);

	}

	@Required
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	@Required
	public void setB2bWorkflowIntegrationService(final B2BWorkflowIntegrationService b2bWorkflowIntegrationService)
	{
		this.b2bWorkflowIntegrationService = b2bWorkflowIntegrationService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Required
	public void setB2bApproverService(final B2BApproverService<B2BCustomerModel> b2bApproverService)
	{
		this.b2bApproverService = b2bApproverService;
	}

	@Required
	public void setWorkflowProcessingService(final WorkflowProcessingService workflowProcessingService)
	{
		this.workflowProcessingService = workflowProcessingService;
	}

	@Required
	public void setWorkflowService(final WorkflowService workflowService)
	{
		this.workflowService = workflowService;
	}

	public WorkflowService getWorkflowService()
	{
		return workflowService;
	}

	public B2BApproverService<B2BCustomerModel> getB2bApproverService()
	{
		return b2bApproverService;
	}

	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	public B2BWorkflowIntegrationService getB2bWorkflowIntegrationService()
	{
		return b2bWorkflowIntegrationService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public WorkflowProcessingService getWorkflowProcessingService()
	{
		return workflowProcessingService;
	}
}
