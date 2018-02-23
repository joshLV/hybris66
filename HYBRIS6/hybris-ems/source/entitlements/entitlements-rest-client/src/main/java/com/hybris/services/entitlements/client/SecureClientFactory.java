/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package com.hybris.services.entitlements.client;

import com.hybris.commons.security.ApiKeyProvider;
import com.hybris.commons.security.impl.SecurityAwareClientRequestFilter;
import com.hybris.commons.security.impl.SecurityAwareClientResponseFilter;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SecureClientFactory implements FactoryBean<Client>, InitializingBean
{
	private final static Logger LOG = LoggerFactory.getLogger(SecureClientFactory.class);
	private ClientConfig clientConfig;
	private LoggingFilter loggingFilter;
	private boolean loggingFilterEnabled;
	private HttpAuthenticationFeature httpAuthenticationFeature;
	private boolean apiAuthenticationEnabled;
	private boolean basicAuthEnabled;
	private ApiKeyProvider apiKeyProvider;
	private SSLContext sslContext;
	private HostnameVerifier hostnameVerifier;

	@Override
	public Client getObject()
	{
		LOG.debug("Getting insecure context");
		final Client client = ClientBuilder.newBuilder()
				.sslContext(getSslContext())
				.hostnameVerifier(getHostnameVerifier())
				.build();
		if (isLoggingFilterEnabled())
		{
			client.register(getLoggingFilter());
		}

		if (isBasicAuthEnabled() && getHttpAuthenticationFeature() != null) {
			client.register(getHttpAuthenticationFeature());
		}

		return client;
	}

	@Override
	public Class<?> getObjectType()
	{
		return Client.class;
	}

	@Override
	public boolean isSingleton()
	{
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (getClientConfig() == null) {
			setClientConfig(new ClientConfig());
		}

		ApacheConnectorProvider connectorProvider = new ApacheConnectorProvider();
		getClientConfig().connectorProvider(connectorProvider);
		if (isApiAuthenticationEnabled()) {
			getClientConfig().register(new SecurityAwareClientRequestFilter(getApiKeyProvider()));
			getClientConfig().register(new SecurityAwareClientResponseFilter(getApiKeyProvider()));
		}
	}

	protected ClientConfig getClientConfig()
	{
		return clientConfig;
	}

	public void setClientConfig(final ClientConfig clientConfig)
	{
		this.clientConfig = clientConfig;
	}

	protected LoggingFilter getLoggingFilter()
	{
		return loggingFilter;
	}

	public void setLoggingFilter(final LoggingFilter loggingFilter)
	{
		this.loggingFilter = loggingFilter;
	}

	protected boolean isLoggingFilterEnabled()
	{
		return loggingFilterEnabled;
	}

	public void setLoggingFilterEnabled(final boolean loggingFilterEnabled)
	{
		this.loggingFilterEnabled = loggingFilterEnabled;
	}

	protected HttpAuthenticationFeature getHttpAuthenticationFeature()
	{
		return httpAuthenticationFeature;
	}

	public void setHttpAuthenticationFeature(final HttpAuthenticationFeature httpAuthenticationFeature)
	{
		this.httpAuthenticationFeature = httpAuthenticationFeature;
	}

	protected boolean isApiAuthenticationEnabled()
	{
		return apiAuthenticationEnabled;
	}

	public void setApiAuthenticationEnabled(final boolean apiAuthenticationEnabled)
	{
		this.apiAuthenticationEnabled = apiAuthenticationEnabled;
	}

	protected boolean isBasicAuthEnabled()
	{
		return basicAuthEnabled;
	}

	public void setBasicAuthEnabled(final boolean basicAuthEnabled)
	{
		this.basicAuthEnabled = basicAuthEnabled;
	}

	protected ApiKeyProvider getApiKeyProvider()
	{
		return apiKeyProvider;
	}

	public void setApiKeyProvider(final ApiKeyProvider apiKeyProvider)
	{
		this.apiKeyProvider = apiKeyProvider;
	}

	protected SSLContext getSslContext()
	{
		return sslContext;
	}

	public void setSslContext(final SSLContext sslContext)
	{
		this.sslContext = sslContext;
	}

	protected HostnameVerifier getHostnameVerifier()
	{
		return hostnameVerifier;
	}

	public void setHostnameVerifier(final HostnameVerifier hostnameVerifier)
	{
		this.hostnameVerifier = hostnameVerifier;
	}
}
