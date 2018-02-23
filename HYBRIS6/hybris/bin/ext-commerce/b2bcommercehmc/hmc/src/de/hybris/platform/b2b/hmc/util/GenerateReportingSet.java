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

import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.hmc.HMCHelper;
import de.hybris.platform.hmc.util.action.ActionEvent;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;

import java.text.MessageFormat;



/**
 * Calculates the complete order.
 */
public class GenerateReportingSet extends B2BItemAction
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

		final B2BUnit unit = (B2BUnit) item;

		getB2bReportingService().setReportSetForUnit((B2BUnitModel) getModelService().get(unit));




		return new ActionResult(ActionResult.OK, MessageFormat.format(HMCHelper.getLocalizedString("action.reportset.success")
				+ " " + unit.getUID(), new Object[]
		{ "OK" }), true, true);
	}

}
