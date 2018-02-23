package com.hybris.services.entitlements.rest.client.factory.impl;

import com.hybris.services.entitlements.rest.client.factory.CloseableHttpClientFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.servlet.ServletException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of {@link CloseableHttpClientFactory} with disabled certificate authority
 * and hostname verification.
 */
public class InsecureHttpClientFactory implements CloseableHttpClientFactory
{

	@Override
	public CloseableHttpClient getClient() throws ServletException
	{
		final HostnameVerifier allowAllHostNameVerifier = (hostName, session) -> true;
		final SSLContextBuilder trustSelfSignedContextBuilder = new SSLContextBuilder();

		try
		{
			trustSelfSignedContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
					trustSelfSignedContextBuilder.build(), allowAllHostNameVerifier);

			return HttpClients.custom()
					.setSSLSocketFactory(sslSocketFactory)
					.build();
		}
		catch (KeyStoreException |NoSuchAlgorithmException |KeyManagementException e)
		{
			throw new ServletException(e);
		}
	}
}
