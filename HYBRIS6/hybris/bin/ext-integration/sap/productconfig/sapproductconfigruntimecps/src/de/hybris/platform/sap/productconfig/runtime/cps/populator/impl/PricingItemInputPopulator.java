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
package de.hybris.platform.sap.productconfig.runtime.cps.populator.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.sap.productconfig.runtime.cps.model.pricing.PricingItemInput;
import de.hybris.platform.sap.productconfig.runtime.cps.model.runtime.CPSItem;
import de.hybris.platform.sap.productconfig.runtime.cps.model.runtime.CPSVariantCondition;

import java.util.ArrayList;


/**
 * Popuplates the pricing item input data for querrying dynamic pricing information, such as total values, based on the
 * configuration runtime data.
 */
public class PricingItemInputPopulator extends AbstractPricingItemInputPopulator implements Populator<CPSItem, PricingItemInput>
{
	@Override
	public void populate(final CPSItem source, final PricingItemInput target)
	{
		fillCoreAttributes(source.getId(), source.getQuantity(), target);
		fillPricingAttributes(source.getKey(), target);
		fillAccessDates(target);
		fillVariantConditions(source, target);
	}

	protected void fillVariantConditions(final CPSItem source, final PricingItemInput target)
	{
		target.setVariantConditions(new ArrayList<>());
		for (final CPSVariantCondition condition : source.getVariantConditions())
		{
			if (!" ".equals(condition.getKey()))
			{
				target.getVariantConditions().add(condition);
			}
			else
			{
				throw new IllegalStateException("Variant condition does not carry a key");
			}
		}

	}


}
