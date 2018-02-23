package com.hybris.services.entitlements.client;

import com.hybris.commons.client.RestResponseException;

/**
 * Provides methods for getting platform oauth2 access token.
 */
public interface Oauth2TokenManager
{
	/**
	 * Returns the string representation of user access token provided by platform.
	 *
	 * @return
	 * 	string representation of user access token provided by platform.
	 */
	String getToken();
}
