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

package de.hybris.platform.timedaccesspromotionenginefacades;

import de.hybris.platform.commercefacades.product.data.ProductData;


/**
 * interface of the Flash Buy facade
 */
public interface FlashBuyFacade
{
	/**
	 * Prepare flash buy information, set product's max order quantity for product and return flashbuy coupon code.
	 *
	 * @param product
	 *           ProductData of the product
	 * @return String FlashBuyCoupon code
	 */
	String prepareFlashBuyInfo(ProductData product);

	/**
	 * update cart model flashbuystatus
	 */
	void updateFlashBuyStatusForCart();
}
