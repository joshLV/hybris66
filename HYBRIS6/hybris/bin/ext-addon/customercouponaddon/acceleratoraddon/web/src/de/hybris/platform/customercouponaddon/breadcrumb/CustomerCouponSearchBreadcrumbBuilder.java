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
package de.hybris.platform.customercouponaddon.breadcrumb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.customercouponfacades.CustomerCouponFacade;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.provider.FacetDisplayNameProvider;

public class CustomerCouponSearchBreadcrumbBuilder extends SearchBreadcrumbBuilder
{	
	private CustomerCouponFacade customerCouponFacade;
	private I18NService i18nService;
	private String parentBreadcrumbResourceKey;
	private String parentBreadcrumbLinkPath;
	private MessageSource messageSource;

	
	@Override
	public List<Breadcrumb> getBreadcrumbs(final String categoryCode, final ProductSearchPageData<SearchStateData, ProductData> searchPageData)
	{
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();
		
		final List<BreadcrumbData<SearchStateData>> breadcrumbDatas = searchPageData.getBreadcrumbs().stream()
				.filter(bc -> bc.getFacetValueName().equalsIgnoreCase(getNameForCouponInBreadcrumbs(bc))).collect(Collectors.toList());
		
		if (CollectionUtils.isNotEmpty(breadcrumbDatas) && breadcrumbDatas.size() == 1 && categoryCode != null)
		{
			final String name = getMessageSource()
					.getMessage(getParentBreadcrumbResourceKey(), null, getI18nService().getCurrentLocale());
			final String breadcrumbLinkPath = getParentBreadcrumbLinkPath();
			final String link = breadcrumbLinkPath != null && !breadcrumbLinkPath.isEmpty() ? breadcrumbLinkPath : "#";
			breadcrumbs.add(new Breadcrumb(link, name, null));
			breadcrumbs.add(new Breadcrumb("#", breadcrumbDatas.get(0).getFacetValueName(), null));
			return breadcrumbs;
		}
		
		return super.getBreadcrumbs(categoryCode, searchPageData);
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	protected MessageSource getMessageSource() {
		return messageSource;
	}
	
	@Required
	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	protected CustomerCouponFacade getCustomerCouponFacade() {
		return customerCouponFacade;
	}
	
	@Required
	public void setCustomerCouponFacade(final CustomerCouponFacade customerCouponFacade) {
		this.customerCouponFacade = customerCouponFacade;
	}

	protected String getParentBreadcrumbResourceKey()
	{
		return parentBreadcrumbResourceKey;
	}

	@Required
	public void setParentBreadcrumbResourceKey(final String parentBreadcrumbResourceKey)
	{
		this.parentBreadcrumbResourceKey = parentBreadcrumbResourceKey;
	}

	protected String getParentBreadcrumbLinkPath()
	{
		return parentBreadcrumbLinkPath;
	}

	@Required
	public void setParentBreadcrumbLinkPath(final String parentBreadcrumbLinkPath)
	{
		this.parentBreadcrumbLinkPath = parentBreadcrumbLinkPath;
	}
	
	public String getNameForCouponInBreadcrumbs(final BreadcrumbData<SearchStateData>  bc)
	{
		final CustomerCouponData customerCouponData = getCustomerCouponFacade().getCustomerCouponForCode(bc.getFacetValueCode());
		if (customerCouponData != null)
		{
			return customerCouponData.getName();
		}
		else
		{
			return null;
		}
	}
}
