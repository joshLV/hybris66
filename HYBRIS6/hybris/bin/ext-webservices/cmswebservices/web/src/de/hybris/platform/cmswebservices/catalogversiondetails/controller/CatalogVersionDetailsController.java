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
package de.hybris.platform.cmswebservices.catalogversiondetails.controller;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.catalogversiondetails.CatalogVersionDetailFacade;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.CatalogVersionData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * Controller to support the catalogversiondetails end point under sites.
 *
 * @pathparam siteId Identifier for a configured site
 * @deprecated since version 6.4, please use the {@code CatalogController} in cmssmarteditwebservices instead
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
@RestController
@IsAuthorizedCmsManager
@RequestMapping(CmswebservicesConstants.API_VERSION + "/sites/{siteId}")
public class CatalogVersionDetailsController
{
	@Resource
	private CatalogVersionDetailFacade catalogVersionDetailsFacade;

	@Resource
	private DataMapper dataMapper;

	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.3")
	@RequestMapping(value = "/catalogversiondetails", method = GET)
	@ApiOperation(
			value = "Get all catalog versions for site",
			notes = "Endpoint to retrieve all permitted Content Catalog Versions and additional details for a given site. "
					+ "Deprecated since 6.3. Please use <code>GET /v1/sites/{siteId}/contentcatalogversiondetails </code>")
	@ApiResponses({
			@ApiResponse(code = 200, message = "A list of all configured content catalogs and version details.", response = CatalogVersionData.class)
	})
	public CatalogVersionData getAllCatalogVersionsForSite(
			@ApiParam(value = "Site identifier", required = true) @PathVariable final String siteId)
	{
		return getDataMapper().map(getCatalogVersionDetailDataFacade().getCatalogVersionDetailDataForSite(siteId),
				CatalogVersionData.class);
	}

	@RequestMapping(value = "/contentcatalogversiondetails", method = GET)
	@ApiOperation(
			value = "Get content catalog versions for site",
			notes = "Endpoint to retrieve all permitted Content Catalog Versions and additional details for a given site.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "A list of all configured content catalogs and version details.", response = CatalogVersionData.class)
	})
	public CatalogVersionData getContentCatalogVersionsForSite(
			@ApiParam(value = "Site identifier", required = true) @PathVariable final String siteId)
	{
		return getDataMapper().map(getCatalogVersionDetailDataFacade().getContentCatalogVersionDetailDataForSite(siteId),
				CatalogVersionData.class);
	}

	@RequestMapping(value = "/productcatalogversiondetails", method = GET)
	@ApiOperation(
			value = "Get product catalog versions for site",
			notes = "Endpoint to retrieve all permitted Product Catalog Versions and additional details for a given site.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "A list of all configured content catalogs and version details.", response = CatalogVersionData.class)
	})
	public CatalogVersionData getProductCatalogVersionsForSite(
			@ApiParam(value = "Site identifier", required = true) @PathVariable final String siteId)
	{
		return getDataMapper().map(getCatalogVersionDetailDataFacade().getProductCatalogVersionDetailDataForSite(siteId),
				CatalogVersionData.class);
	}

	protected CatalogVersionDetailFacade getCatalogVersionDetailDataFacade()
	{
		return catalogVersionDetailsFacade;
	}

	public void setCatalogVersionDetailDataFacade(final CatalogVersionDetailFacade catalogVersionDetailsFacade)
	{
		this.catalogVersionDetailsFacade = catalogVersionDetailsFacade;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

}
