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
package com.hybris.services.entitlements.rest.filters;

import com.hybris.services.entitlements.rest.client.factory.CloseableHttpClientFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


/**
 * Filter that verifies access token from header to make sure that user is logged into the platform.
 */
public class PlatformOauth2TokenFilter implements Filter
{
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String ACCESS_TOKEN_HEADER = "access-token";
	public static final String BEARER_HEADER_VALUE_PART = "Bearer ";

	private String platformUrl;
	private boolean enabled;
	private CloseableHttpClientFactory httpClientFactory;

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException
	{
		// Nothing to init
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException
	{
		if (isEnabled())
		{
			handle((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
		}
		else
		{
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy()
	{
		// Nothing to destroy
	}

	protected void handle(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException
	{
		final String accessToken = servletRequest.getHeader(ACCESS_TOKEN_HEADER);
		final URI authUri = UriBuilder.fromPath(getPlatformUrl())
				.build();
		final HttpGet authRequest = new HttpGet(authUri);
		authRequest.addHeader(AUTHORIZATION_HEADER, BEARER_HEADER_VALUE_PART + accessToken);

		try (final CloseableHttpClient client = getHttpClientFactory().getClient())
		{
			final HttpResponse authResponse = client.execute(authRequest);
			final int responseStatusCode = authResponse.getStatusLine().getStatusCode();
			if (HttpStatus.Code.OK.equals(responseStatusCode))
			{
				filterChain.doFilter(servletRequest, servletResponse);
			}
			else
			{
				respondWithUnauthorized(authResponse, servletResponse);
			}
		} catch (final HttpHostConnectException e)
		{
			setUnauthorizedStatusForResponse(servletResponse);
		}
	}

	protected void respondWithUnauthorized(final HttpResponse authResponse, final HttpServletResponse servletResponse)
			throws IOException
	{
		try(final ServletOutputStream servletResponseOutputStream = servletResponse.getOutputStream();
				final InputStream authResponseInputStream = authResponse.getEntity().getContent())
		{
			IOUtils.copy(authResponseInputStream, servletResponseOutputStream);
		}
		setUnauthorizedStatusForResponse(servletResponse);
	}

	protected void setUnauthorizedStatusForResponse(final HttpServletResponse servletResponse)
	{
		servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

	protected String getPlatformUrl()
	{
		return platformUrl;
	}

	@Required
	public void setPlatformUrl(final String platformUrl)
	{
		this.platformUrl = platformUrl;
	}

	protected boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	protected CloseableHttpClientFactory getHttpClientFactory()
	{
		return httpClientFactory;
	}

	public void setHttpClientFactory(final CloseableHttpClientFactory httpClientFactory)
	{
		this.httpClientFactory = httpClientFactory;
	}
}
