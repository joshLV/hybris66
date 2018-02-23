/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package de.hybris.platform.configurablebundleatddtests.keywords;


import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * ATDD keywords for bundle facades
 */
public class ConfigurableBundleKeywords extends AbstractKeywordLibrary
{
	@Autowired
	private BundleCartFacade bundleCartFacade;

	/**
	 * Start new bundle.
	 *
	 * @param componentId bundle component
	 * @param productCode product (should be one of the component's products)
	 * @param quantity number of products to add
	 * @return cart modification
	 * @throws CommerceCartModificationException if the bundle can not be created
	 */
	public OrderEntryData startNewBundle(final String componentId, final String productCode, final int quantity)
			throws CommerceCartModificationException
	{
		return getBundleCartFacade().startBundle(componentId, productCode, quantity).getEntry();
	}

	protected BundleCartFacade getBundleCartFacade()
	{
		return bundleCartFacade;
	}
}
