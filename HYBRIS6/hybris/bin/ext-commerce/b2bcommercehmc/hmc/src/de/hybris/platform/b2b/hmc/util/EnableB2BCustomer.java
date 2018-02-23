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
package de.hybris.platform.b2b.hmc.util;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.hmc.HMCHelper;
import de.hybris.platform.hmc.util.action.ActionEvent;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;

import java.text.MessageFormat;

import org.apache.log4j.Logger;



/**
 * Calculates the complete order.
 */
public class EnableB2BCustomer extends B2BItemAction
{
	private static final Logger LOG = Logger.getLogger(EnableB2BCustomer.class);

	@Override
	public ActionResult perform(final ActionEvent actionEvent) throws JaloBusinessException
	{
		final Item item = getItem(actionEvent);

		if (item == null)
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bemployeeenable.notcreated"), false);
		}

		if (!canChange(item))
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bemployeeenable.cannotchange"),
					false);
		}

		final B2BCustomer customer = (B2BCustomer) item;
		if (!canActivate(customer))
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bemployeeenable.cannotactivate"),
					false);
		}

		customer.setActive(true);
		customer.setLoginDisabled(false);

		return new ActionResult(ActionResult.OK, MessageFormat.format(
				HMCHelper.getLocalizedString("action.b2bemployeeenable.success"), new Object[]
				{ "OK" }), true, false);
	}


	/**
	 * Checks if a parent b2bunit is active
	 * 
	 * @param customer
	 * @return true if parent unit is active.
	 */
	private boolean canActivate(final B2BCustomer customer)
	{
		boolean canActivate = false;
		try
		{
			final B2BCustomerModel model = getModelService().get(customer);
			final B2BUnitModel parent = getB2bUnitService().getParent(model);
			if (parent != null)
			{
				return parent.getActive().booleanValue();
			}
			// root unit you can activate no problem.
			canActivate = true;
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return canActivate;
	}


	@Override
	public boolean needConfirmation()
	{
		return false;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.hmc.util.action.ItemAction#isActive(de.hybris.platform.hmc.util.action.ActionEvent)
	 */
	public boolean isActive(final ActionEvent actionEvent)
	{
		final Item item = getItem(actionEvent);
		if (item == null)
		{
			return false;
		}
		else
		{
			return isVisible(actionEvent) && !((B2BCustomer) item).isActive().booleanValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.hmc.util.action.ItemAction#isVisible(de.hybris.platform.hmc.util.action.ActionEvent)
	 */
	@Override
	public boolean isVisible(final ActionEvent actionEvent)
	{
		final User user = getModelService().getSource(getUserService().getCurrentUser());
		return user.isMemberOf(UserManager.getInstance().getAdminUserGroup(), true)
				|| user.isMemberOf(UserManager.getInstance().getUserGroupByGroupID(B2BConstants.B2BADMINGROUP), true);
	}


}
