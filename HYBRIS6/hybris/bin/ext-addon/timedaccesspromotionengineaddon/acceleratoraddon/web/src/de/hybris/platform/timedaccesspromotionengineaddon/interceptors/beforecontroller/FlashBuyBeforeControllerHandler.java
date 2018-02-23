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

package de.hybris.platform.timedaccesspromotionengineaddon.interceptors.beforecontroller;

import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeControllerHandler;
import de.hybris.platform.timedaccesspromotionenginefacades.FlashBuyFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;


/**
 * Set the cart flashBuyStatus for the flashbuy
 */
public class FlashBuyBeforeControllerHandler implements BeforeControllerHandler
{
	@Resource(name = "flashBuyFacade")
	private FlashBuyFacade flashBuyFacade;

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler) throws Exception
	{
		final String url = request.getRequestURI();
		if (url.contains("/checkout/multi/summary/placeOrder"))
		{
			flashBuyFacade.updateFlashBuyStatusForCart();
		}
		return true;
	}

}
