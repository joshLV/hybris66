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

import de.hybris.platform.b2b.process.approval.actions.AbstractSimpleB2BApproveOrderDecisionAction;
import de.hybris.platform.b2b.process.approval.model.B2BApprovalProcessModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.task.RetryLaterException;
import org.apache.log4j.Logger;


/**
 * @deprecated Since 6.3. Please see quote functionality from commerce.
 */
@Deprecated
public class CheckSalesQuoteResults extends AbstractSimpleB2BApproveOrderDecisionAction
{
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(CheckSalesQuoteResults.class);

	@Override
	public Transition executeAction(final B2BApprovalProcessModel process) throws RetryLaterException
	{
		OrderModel order = null;
		Transition transition = Transition.NOK;
		try
		{
			order = process.getOrder();
			if (OrderStatus.REJECTED.equals(order.getStatus()))
			{
				order.setStatus(OrderStatus.REJECTED_QUOTE);
				transition = Transition.NOK;
			}

			if (OrderStatus.APPROVED.equals(order.getStatus()))
			{
				order.setStatus(OrderStatus.APPROVED_QUOTE);
				transition = Transition.OK;
			}
			this.modelService.save(order);
			return transition;
		}
		catch (final Exception e)
		{
			this.handleError(order, e);
			return transition;
		}
	}

	protected void handleError(final OrderModel order, final Exception e)
	{
		if (order != null)
		{
			this.setOrderStatus(order, OrderStatus.B2B_PROCESSING_ERROR);
		}
		LOG.error(e.getMessage(), e);
	}
}
