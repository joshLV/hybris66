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
package com.hybris.ymkt.recentvieweditemsaddon.interceptors.beforeview;

import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorservices.util.SpringHelper;
import de.hybris.platform.addonsupport.controllers.page.AbstractAddOnPageController;
import de.hybris.platform.addonsupport.interceptors.BeforeViewHandlerAdaptee;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.ModelMap;

import com.hybris.ymkt.recentvieweditemsservices.RecentViewedItemsService;


/**
 * Capture the page (category page, product page) beforeView event, and check if there is product ID or category ID that
 * can be added to the recently viewed items queues
 */
public class RecentViewedItemsBeforeViewHandlerAdaptee implements BeforeViewHandlerAdaptee
{

	private RecentViewedItemsService recentViewedItemsService;

	@Override
	public String beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName)
	{
		final AbstractPageModel pageModel = (AbstractPageModel) model.get(AbstractAddOnPageController.CMS_PAGE_MODEL);

		if (pageModel instanceof ProductPageModel)
		{
			final RequestContextData requestContextData = SpringHelper.getSpringBean(request, "requestContextData",
					RequestContextData.class, true);
			final ProductModel product = requestContextData.getProduct();
			final String productCode = product.getCode();
			String categoryCode = null;
			final Collection<CategoryModel> categories = product.getSupercategories();
			if (categories != null)
			{
				categoryCode = categories.iterator().next().getCode();
			}
			recentViewedItemsService.productVisited(productCode, categoryCode);
		}
		else if (pageModel instanceof CategoryPageModel)
		{
			final RequestContextData requestContextData = SpringHelper.getSpringBean(request, "requestContextData",
					RequestContextData.class, true);
			final CategoryModel category = requestContextData.getCategory();
			recentViewedItemsService.productVisited(null, category.getCode());
		}

		return viewName;
	}

	@Required
	public void setRecentViewedItemsService(final RecentViewedItemsService recentViewedItemsService)
	{
		this.recentViewedItemsService = recentViewedItemsService;
	}
}
