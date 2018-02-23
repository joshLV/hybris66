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
package de.hybris.platform.cmswebservices.enumdata.controller;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.enumdata.EnumDataFacade;
import de.hybris.platform.cmswebservices.data.EnumData;
import de.hybris.platform.cmswebservices.data.EnumListData;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import javax.annotation.Resource;
import java.util.List;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;


/**
 * EnumController exposes the values of enum types.
 *
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION +  "/enums")
public class EnumDataController
{

	@Resource(name = "cmsEnumDataFacade")
	private EnumDataFacade enumDataFacade;
	@Resource
	private DataMapper dataMapper;

	/**
	 * @deprecated since 6.2
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.2")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
			value = "Get enum values by class",
			notes = "This accepts a parameter of a classname and for this given classname, retrieves all of the possible enumeration values. "
					+ "Deprecated since version 6.2. Please use PUT /types/{typecode} instead.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "List of enum data", response = EnumListData.class)
	})
	public EnumListData getEnumValuesByClass(
			@ApiParam(value = "Class name of the enumeration which values should be retrieved", required = true) @RequestParam final String enumClass)
	{
		final List<EnumData> enums = getDataMapper().mapAsList(getEnumDataFacade().getEnumValues(enumClass), EnumData.class, null);

		final EnumListData enumListData = new EnumListData();
		enumListData.setEnums(enums);
		return enumListData;
	}

	protected EnumDataFacade getEnumDataFacade()
	{
		return enumDataFacade;
	}

	public void setEnumDataFacade(final EnumDataFacade enumDataFacade)
	{
		this.enumDataFacade = enumDataFacade;
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
