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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.FactoryBean;


/**
 * Constructs {@link SSLContext} that trusts every certificate. For debug purposes only.
 */
public class InsecureSslContextFactory implements FactoryBean<SSLContext>
{
	@Override
	public SSLContext getObject() throws Exception
	{
		final SSLContext sc = SSLContext.getInstance("TLSv1.2");
		System.setProperty("https.protocols", "TLSv1.2");
		sc.init(null, new TrustManager[]{new X509TrustManager()
		{
			@Override
			public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException
			{
				// Trust to all
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException
			{
				// Trust to all
			}

			@Override
			public X509Certificate[] getAcceptedIssuers()
			{
				return new X509Certificate[0];
			}
		}}, new java.security.SecureRandom());
		return sc;
	}

	@Override
	public Class<?> getObjectType()
	{
		return SSLContext.class;
	}

	@Override
	public boolean isSingleton()
	{
		return false;
	}
}
