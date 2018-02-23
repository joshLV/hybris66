/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */

package com.hybris.services.entitlements.rest.resources;

import com.hybris.services.entitlements.api.EntitlementFacade;
import com.hybris.services.entitlements.api.codes.ResourceCode;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


/**
 * User account controller.
 */
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@StatusCodes({
		@ResponseCode(code = ResourceCode.CODE_404, condition = "ERROR - Resource not found")
})
@Path("/users")
public class UserResource
{
	@Autowired
	private EntitlementFacade entitlementFacade;

	/**
	 * Remove all data of given user.
	 *
	 * @param userId user to remove
	 */
	@DELETE
	@Path("/{userId}")
	public void delete(@PathParam("userId") final String userId)
	{
		entitlementFacade.deleteUser(userId);
	}

	@Required
	public void setEntitlementFacade(final EntitlementFacade entitlementFacade)
	{
		this.entitlementFacade = entitlementFacade;
	}
}
