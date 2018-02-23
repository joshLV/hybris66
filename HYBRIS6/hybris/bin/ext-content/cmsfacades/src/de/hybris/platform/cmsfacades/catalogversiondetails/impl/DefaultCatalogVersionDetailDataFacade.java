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
package de.hybris.platform.cmsfacades.catalogversiondetails.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.catalogversiondetails.CatalogVersionDetailFacade;
import de.hybris.platform.cmsfacades.data.CatalogVersionData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CatalogVersionDetailFacade}.
 * 
 * @deprecated since version 6.4, please use the {@code CatalogFacade} in cmssmarteditwebservices extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public class DefaultCatalogVersionDetailDataFacade implements CatalogVersionDetailFacade
{

	private CMSAdminSiteService cmsAdminSiteService;

	private Converter<CMSSiteModel, CatalogVersionData> siteContentCatalogConverter;
	private Converter<CMSSiteModel, CatalogVersionData> siteProductCatalogConverter;

	@Override
	public CatalogVersionData getCatalogVersionDetailDataForSite(final String siteUid)
	{
		return this.getContentCatalogVersionDetailDataForSite(siteUid);
	}

	@Override
	public CatalogVersionData getContentCatalogVersionDetailDataForSite(final String siteUid)
	{
		return getSiteContentCatalogConverter().convert(getCmsAdminSiteService().getSiteForId(siteUid));

	}

	@Override
	public CatalogVersionData getProductCatalogVersionDetailDataForSite(final String siteUid)
	{
		return getSiteProductCatalogConverter().convert(getCmsAdminSiteService().getSiteForId(siteUid));
	}


	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	protected Converter<CMSSiteModel, CatalogVersionData> getSiteContentCatalogConverter()
	{
		return siteContentCatalogConverter;
	}

	@Required
	public void setSiteContentCatalogConverter(final Converter<CMSSiteModel, CatalogVersionData> siteContentCatalogConverter)
	{
		this.siteContentCatalogConverter = siteContentCatalogConverter;
	}

	protected Converter<CMSSiteModel, CatalogVersionData> getSiteProductCatalogConverter()
	{
		return siteProductCatalogConverter;
	}

	@Required
	public void setSiteProductCatalogConverter(final Converter<CMSSiteModel, CatalogVersionData> siteProductCatalogConverter)
	{
		this.siteProductCatalogConverter = siteProductCatalogConverter;
	}
}
