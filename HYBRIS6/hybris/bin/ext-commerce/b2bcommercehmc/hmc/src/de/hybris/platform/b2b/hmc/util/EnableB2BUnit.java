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
import de.hybris.platform.b2b.jalo.B2BUnit;
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
 * Set the active flag on a {@link B2BUnit} and the branch it belongs to.
 * 
 */
public class EnableB2BUnit extends B2BItemAction
{

	private static final Logger LOG = Logger.getLogger(EnableB2BUnit.class);


	@Override
	public ActionResult perform(final ActionEvent actionEvent) throws JaloBusinessException
	{
		final Item item = getItem(actionEvent);

		if (item == null)
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bunitenable.notcreated"), false);
		}

		if (!canChange(item))
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bunitenable.cannotchange"), false);
		}

		final B2BUnit b2bunit = (B2BUnit) item;

		if (!canActivate(b2bunit))
		{
			return new ActionResult(ActionResult.FAILED, HMCHelper.getLocalizedString("action.b2bunitenable.cannotactivate"), false);
		}

		b2bunit.setActive(true);

		return new ActionResult(ActionResult.OK, MessageFormat.format(HMCHelper.getLocalizedString("action.b2bunitenable.success"),
				new Object[]
				{ "OK" }), true, false);
	}

	/**
	 * Checks if a parent b2bunit is active
	 * 
	 * @param unit
	 * @return true if parent unit is active.
	 */
	private boolean canActivate(final B2BUnit unit)
	{
		boolean canActivate = false;
		try
		{
			final B2BUnitModel model = getModelService().get(unit);
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
		return true;
	}

	@Override
	public String getConfirmationMessage()
	{
		return HMCHelper.getLocalizedString("action.b2bunitenable.confirm");
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
			return isVisible(actionEvent) && !((B2BUnit) item).isActive().booleanValue();
		}
	}


	@Override
	public boolean isVisible(final ActionEvent actionEvent)
	{
		// the disable button should only be visible for the members of 'admingoup' and 'b2badmingroup'
		final User user = getModelService().getSource(getUserService().getCurrentUser());
		return user.isMemberOf(UserManager.getInstance().getAdminUserGroup(), true)
				|| user.isMemberOf(UserManager.getInstance().getUserGroupByGroupID(B2BConstants.B2BADMINGROUP), true);
	}


}
