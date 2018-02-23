/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.yacceleratorordermanagement.actions.consignment;

import java.util.HashSet;
import java.util.Set;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;

import org.apache.log4j.Logger;


/**
 * Redirects which system will gain control of the fulfillment process for the given {@link ConsignmentModel}.
 * The options are either internal OMS fulfillment or external fulfillment process.
 */
public class RedirectConsignmentFulfillmentProcessAction extends AbstractAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(RedirectConsignmentFulfillmentProcessAction.class);

	protected enum Transition
	{
		INTERNALPROCESS, EXTERNALPROCESS, UNDETERMINISTIC;

		public static Set<String> getStringValues()
		{
			final Set<String> stringValues = new HashSet<>();

			for (final Transition transition : Transition.values())
			{
				stringValues.add(transition.toString());
			}
			return stringValues;
		}
	}

	@Override
	public String execute(final ConsignmentProcessModel consignmentProcess)
	{
		LOG.info("Process: " + consignmentProcess.getCode() + " in step " + getClass().getSimpleName());
		final ConsignmentModel consignment = consignmentProcess.getConsignment();

		String transition = Transition.INTERNALPROCESS.toString();

		if (ConsignmentStatus.CANCELLED.equals(consignment.getStatus()))
		{
			transition = Transition.UNDETERMINISTIC.toString();
		}
		else if (consignment.getFulfillmentSystemConfig() != null)
		{
			transition = Transition.EXTERNALPROCESS.toString();
		}

		LOG.debug("Process: " + consignmentProcess.getCode() + " transitions to " + transition);
		return transition;
	}

	@Override
	public Set<String> getTransitions()
	{
		return Transition.getStringValues();
	}
}
