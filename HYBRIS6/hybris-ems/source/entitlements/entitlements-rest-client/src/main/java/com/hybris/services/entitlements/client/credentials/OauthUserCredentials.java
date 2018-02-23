package com.hybris.services.entitlements.client.credentials;

import org.springframework.beans.factory.annotation.Required;

/**
 * Class-container for user credentials necessary for oAuth2 authentication.
 */
public class OauthUserCredentials
{
	private String clientId;
	private String clientSecret;
	private String username;
	private String password;

	public String getClientId()
	{
		return clientId;
	}

	@Required
	public void setClientId(final String clientId)
	{
		this.clientId = clientId;
	}

	public String getClientSecret()
	{
		return clientSecret;
	}

	@Required
	public void setClientSecret(final String clientSecret)
	{
		this.clientSecret = clientSecret;
	}

	public String getUsername()
	{
		return username;
	}

	@Required
	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	@Required
	public void setPassword(final String password)
	{
		this.password = password;
	}
}
