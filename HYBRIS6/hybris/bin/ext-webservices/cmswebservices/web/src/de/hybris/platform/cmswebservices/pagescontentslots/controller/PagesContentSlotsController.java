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
package de.hybris.platform.cmswebservices.pagescontentslots.controller;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.pagescontentslots.PageContentSlotFacade;
import de.hybris.platform.cmswebservices.data.PageContentSlotData;
import de.hybris.platform.cmswebservices.data.PageContentSlotListData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;


/**
 * Controller that provides an API to retrieve all pages where a given content slot is present.
 *
 * @pathparam siteId Site identifier
 * @pathparam catalogId Catalog name
 * @pathparam versionId Catalog version identifier
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pagescontentslots")
public class PagesContentSlotsController
{
	@Resource
	private PageContentSlotFacade pageContentSlotFacade;
	@Resource
	private DataMapper dataMapper;

	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.3")
	@RequestMapping(method = RequestMethod.GET, params =
{ "slotId" })
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(
			value = "Get pages by content slot",
			notes = "Retrieve all pages containing the content slot specified by the slot id. This resource is deprecated since version 6.3, please use GET /pagescontentslots?pageId={pageId} to retrieve shared content slot information.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "DTO which serves as a wrapper object that contains a list of PageContentSlotListData, never null.", response = PageContentSlotListData.class)
	})
	public @ResponseBody PageContentSlotListData getPagesByContentSlot(
			@ApiParam(value = "Identifier of the slot which the page contains", required = true) @RequestParam("slotId") final String slotId)
	{
		final PageContentSlotListData pageContentSlotList = new PageContentSlotListData();

		try
		{
			final List<PageContentSlotData> convertedPages = getDataMapper()
					.mapAsList(getPageContentSlotFacade().getPagesBySlotId(slotId), PageContentSlotData.class, null);
			pageContentSlotList.setPageContentSlotList(convertedPages);
		}
		catch (final CMSItemNotFoundException e)
		{
			pageContentSlotList.setPageContentSlotList(Collections.emptyList());
		}
		return pageContentSlotList;
	}

	@RequestMapping(method = RequestMethod.GET, params = { "pageId" })
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Get content slots by page", notes = "Retrieve all content slots defined on the page specified by the page id.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "DTO which serves as a wrapper object that contains a list of PageContentSlotListData, never null.", response = PageContentSlotListData.class)
	})
	public @ResponseBody PageContentSlotListData getContentSlotsByPage(
			@ApiParam(value = "Identifier of the page", required = true) @RequestParam("pageId") final String pageId)
	{
		final PageContentSlotListData pageContentSlotList = new PageContentSlotListData();

		try
		{
			final List<PageContentSlotData> convertedSlots = getDataMapper()
					.mapAsList(getPageContentSlotFacade().getContentSlotsByPage(pageId), PageContentSlotData.class, null);
			pageContentSlotList.setPageContentSlotList(convertedSlots);
		}
		catch (final CMSItemNotFoundException e)
		{
			pageContentSlotList.setPageContentSlotList(Collections.emptyList());
		}
		return pageContentSlotList;
	}

	protected PageContentSlotFacade getPageContentSlotFacade()
	{
		return pageContentSlotFacade;
	}

	public void setPageContentSlotFacade(final PageContentSlotFacade pageContentSlotFacade)
	{
		this.pageContentSlotFacade = pageContentSlotFacade;
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
