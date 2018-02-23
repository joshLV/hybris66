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
import de.hybris.platform.hmc.HMCHelper;
import de.hybris.platform.hmc.util.action.ActionEvent;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;

import java.text.MessageFormat;



/**
 * Calculates the complete order.
 */
public class DisableB2BCustomer extends B2BItemAction
{
	@Override
	public ActionResult perform(final ActionEvent actionEvent) throws JaloBusinessException
	{
		final Item item = getItem(actionEvent);

		if (item == null)
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bemployeedisable.notcreated"), false);
		}

		if (!canChange(item))
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bemployeedisable.cannotchange"),
					false);
		}

		final B2BCustomer customer = (B2BCustomer) item;
		customer.setActive(false);
		customer.setLoginDisabled(true);


		return new ActionResult(ActionResult.OK, MessageFormat.format(
				HMCHelper.getLocalizedString("action.b2bemployeedisable.success"), new Object[]
				{ "OK" }), true, false);
	}

	@Override
	public boolean needConfirmation()
	{
		return true;
	}

	@Override
	public String getConfirmationMessage()
	{
		return HMCHelper.getLocalizedString("action.b2bemployeedisable.confirm");
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
			return isVisible(actionEvent) && ((B2BCustomer) item).isActive().booleanValue();
		}
	}

	@Override
	public boolean isVisible(final ActionEvent actionEvent)
	{
		// the disable button should only be visible for the members of 'admingoup'
		final User user = getModelService().getSource(getUserService().getCurrentUser());
		return user.isMemberOf(UserManager.getInstance().getAdminUserGroup(), true)
				|| user.isMemberOf(UserManager.getInstance().getUserGroupByGroupID(B2BConstants.B2BADMINGROUP), true);
	}
}
