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
package com.hybris.services.entitlements.client;

import com.hybris.commons.client.RestCallBuilder;
import org.springframework.beans.factory.annotation.Required;

/**
 * Client implementation for entitlement management.
 */
public class Oauth2AwareEntitlementRestClient extends DefaultEntitlementRestClient
{
	private static final String ACCESS_TOKEN_HEADER = "access-token";

	private Oauth2TokenManager oauth2TokenManager;

	private boolean oauthEnabled = true;

	@Override
	protected RestCallBuilder buildCall(final String path, final Object... params)
	{
		if(!isOauthEnabled())
		{
			return super.buildCall(path, params);
		}
		final String accessToken = getOauth2TokenManager().getToken();

		return super.buildCall(path, params)
				.header(ACCESS_TOKEN_HEADER, accessToken);
	}

	protected Oauth2TokenManager getOauth2TokenManager()
	{
		return oauth2TokenManager;
	}

	@Required
	public void setOauth2TokenManager(Oauth2TokenManager oauth2TokenManager)
	{
		this.oauth2TokenManager = oauth2TokenManager;
	}

	protected boolean isOauthEnabled()
	{
		return oauthEnabled;
	}
	
	public void setOauthEnabled(final boolean oAuthEnabled)
	{
		this.oauthEnabled = oAuthEnabled;
	}
}
