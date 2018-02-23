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

import javax.net.ssl.HostnameVerifier;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.FactoryBean;

/**
 * Constructs hostname verifier that trusts every host.
 */
public class InsecureHostnameVerifierFactory implements FactoryBean<HostnameVerifier>
{
	@Override
	public HostnameVerifier getObject() throws Exception
	{
		return NoopHostnameVerifier.INSTANCE;
	}

	@Override
	public Class<?> getObjectType()
	{
		return HostnameVerifier.class;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}
}
