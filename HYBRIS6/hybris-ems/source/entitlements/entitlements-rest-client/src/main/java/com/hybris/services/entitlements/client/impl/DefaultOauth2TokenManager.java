package com.hybris.services.entitlements.client.impl;

import com.hybris.commons.client.GenericRestClient;
import com.hybris.commons.client.RestResponseException;
import com.hybris.services.entitlements.client.response.entity.Oauth2AccessTokenResponse;
import com.hybris.services.entitlements.client.Oauth2TokenManager;
import com.hybris.services.entitlements.client.credentials.OauthUserCredentials;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.core.MediaType;
import java.util.Objects;

/**
 * Default implementation of {@link Oauth2TokenManager} which performs REST call to
 * platform oAuth2 provider to get access token. Can only use single set of user
 * credentials at the same time.
 */
public class DefaultOauth2TokenManager extends GenericRestClient implements Oauth2TokenManager
{
	private static final long MILLISECONDS_TO_SECONDS_RATIO = 1000;
	private static final long MINIMAL_REFRESH_THRESHOLD = 10;
	private static final String GRANT_TYPE_PASSWORD = "password";
	private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	private static final String CLIENT_ID_HEADER = "client_id";
	private static final String CLIENT_SECRET_HEADER = "client_secret";
	private static final String USERNAME_HEADER = "username";
	private static final String PASSWORD_HEADER = "password";
	private static final String GRANT_TYPE_HEADER = "grant_type";
	private static final String REFRESH_TOKEN_HEADER = "refresh_token";

	private OauthUserCredentials userCredentials;
	private Oauth2AccessTokenResponse lastAccessTokenResponse;
	private long lastAccessTokenTimestampSeconds;

	@Override
	public String getToken()
	{
		try
		{
			if (getLastAccessTokenResponse() == null)
			{
				getNewToken();
			}
			else if (toSeconds(System.currentTimeMillis()) - getLastAccessTokenTimestampSeconds() >
					getLastAccessTokenResponse().getExpiresIn() - MINIMAL_REFRESH_THRESHOLD)
			{
				refreshToken();
			}
		}
		catch (final RestResponseException e)
		{
			throw new RuntimeException("Error when refreshing user oAuth2 access token", e);
		}

		return getLastAccessTokenResponse().getAccessToken();
	}

	protected void getNewToken() throws RestResponseException
	{
		setLastAccessTokenResponse(this.call("")
				.queryParam(CLIENT_ID_HEADER, userCredentials.getClientId())
				.queryParam(CLIENT_SECRET_HEADER, userCredentials.getClientSecret())
				.queryParam(USERNAME_HEADER, userCredentials.getUsername())
				.queryParam(PASSWORD_HEADER, userCredentials.getPassword())
				.queryParam(GRANT_TYPE_HEADER, GRANT_TYPE_PASSWORD)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.post(Oauth2AccessTokenResponse.class)
				.result());
		setLastAccessTokenTimestampSeconds(toSeconds(System.currentTimeMillis()));
	}

	protected void refreshToken() throws RestResponseException
	{
		setLastAccessTokenResponse(this.call("")
				.queryParam(CLIENT_ID_HEADER, userCredentials.getClientId())
				.queryParam(CLIENT_SECRET_HEADER, userCredentials.getClientSecret())
				.queryParam(GRANT_TYPE_HEADER, GRANT_TYPE_REFRESH_TOKEN)
				.queryParam(REFRESH_TOKEN_HEADER, lastAccessTokenResponse.getRefreshToken())
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)
				.post(Oauth2AccessTokenResponse.class)
				.result());
		setLastAccessTokenTimestampSeconds(toSeconds(System.currentTimeMillis()));
	}

	protected long toSeconds(final long timeInMilliseconds)
	{
		return timeInMilliseconds / MILLISECONDS_TO_SECONDS_RATIO;
	}

	protected OauthUserCredentials getUserCredentials()
	{
		return userCredentials;
	}

	@Required
	public void setUserCredentials(final OauthUserCredentials userCredentials)
	{
		if (!Objects.equals(userCredentials, this.userCredentials))
		{
			lastAccessTokenResponse = null;
			lastAccessTokenTimestampSeconds = 0;
		}
		this.userCredentials = userCredentials;
	}

	protected Oauth2AccessTokenResponse getLastAccessTokenResponse()
	{
		return lastAccessTokenResponse;
	}

	protected void setLastAccessTokenResponse(Oauth2AccessTokenResponse lastAccessTokenResponse)
	{
		this.lastAccessTokenResponse = lastAccessTokenResponse;
	}

	protected long getLastAccessTokenTimestampSeconds()
	{
		return lastAccessTokenTimestampSeconds;
	}

	protected void setLastAccessTokenTimestampSeconds(long lastAccessTokenTimestampSeconds)
	{
		this.lastAccessTokenTimestampSeconds = lastAccessTokenTimestampSeconds;
	}
}
