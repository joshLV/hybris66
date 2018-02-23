package com.hybris.services.entitlements.client.response.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains fields from REST response for access token from platform.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Oauth2AccessTokenResponse
{
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("expires_in")
	private long expiresIn;

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public String getRefreshToken()
	{
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken)
	{
		this.refreshToken = refreshToken;
	}

	public long getExpiresIn()
	{
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn)
	{
		this.expiresIn = expiresIn;
	}
}
