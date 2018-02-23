package com.hybris.services.entitlements.rest.client.factory;

import org.apache.http.impl.client.CloseableHttpClient;

import javax.servlet.ServletException;

/**
 * Factory method interface for creating {@link CloseableHttpClient}
 */
public interface CloseableHttpClientFactory
{
	/**
	 * Returns an instance of configured CloseableHttpClient.
	 *
	 * @return
	 * 		an instance of configured CloseableHttpClient.
	 * @throws ServletException if an exception occurs during client creation.
	 */
	CloseableHttpClient getClient() throws ServletException;
}
