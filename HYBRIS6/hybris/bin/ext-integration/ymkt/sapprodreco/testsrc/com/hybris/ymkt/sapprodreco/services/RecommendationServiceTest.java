/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.ymkt.sapprodreco.services;

import de.hybris.bootstrap.annotations.ManualTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hybris.ymkt.common.http.HttpURLConnectionService;
import com.hybris.ymkt.common.odata.ODataConvertEdmService;
import com.hybris.ymkt.common.odata.ODataService;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario.Scenario;


@ManualTest
public class RecommendationServiceTest
{
	static RecommendationService recoService = new RecommendationService();
	static ODataService oDataService = new ODataService();
	static HttpURLConnectionService httpURLConnectionService = new HttpURLConnectionService();
	static ODataConvertEdmService oDataConvertEdmService = new ODataConvertEdmService();

	public static void disableCertificates() throws Exception
	{
		final TrustManager[] trustAllCerts =
		{ (TrustManager) Proxy.getProxyClass(X509TrustManager.class.getClassLoader(), X509TrustManager.class)
				.getConstructor(InvocationHandler.class).newInstance((InvocationHandler) (o, m, args) -> null) };

		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		disableCertificates();
		oDataService.setHttpURLConnectionService(httpURLConnectionService);
		oDataService.setODataConvertEdmService(oDataConvertEdmService);

		oDataService.setRootUrl("https://localhost:11100/sap/opu/odata/sap/PROD_RECO_RUNTIME_SRV/");
		oDataService.setUser("");
		oDataService.setPassword("");

		recoService.setODataService(oDataService);
	}

	@Test
	public void testWithResult() throws Exception
	{
		final RecommendationScenario recoScenario = new RecommendationScenario("6de4ae57e795a737", "COOKIE_ID");
		recoScenario.getScenarios().add(new Scenario("TEST_GRIFFIN"));
		recoService.executeRecommendationScenario(recoScenario, false);
		Assert.assertNotNull(recoScenario);
		Assert.assertFalse(recoScenario.getScenarioHashes().isEmpty());
		Assert.assertFalse(recoScenario.getResultObjects().isEmpty());
	}

	@Test
	public void testIsValidScenarioHash() throws Exception
	{
		Assert.assertTrue(recoService.validateScenarioHash("3FB9A008CF3A86022A88A581846E070F"));

		Assert.assertFalse(recoService.validateScenarioHash(null));
		Assert.assertFalse(recoService.validateScenarioHash(""));
		Assert.assertFalse(recoService.validateScenarioHash("3FB9A008CF3A86022A88A581846E070"));
		Assert.assertFalse(recoService.validateScenarioHash("3FB9A008CF3A86022A88A581846E070F1"));
		Assert.assertFalse(recoService.validateScenarioHash("3fB9A008CF3A86022A88A581846E070F"));
		Assert.assertFalse(recoService.validateScenarioHash("3=B9A008CF3A86022A88A581846E070F"));
	}



	@Test
	public void testHashOnly() throws Exception
	{
		final RecommendationScenario recoScenario = new RecommendationScenario("6de4ae57e795a737", "COOKIE_ID");
		recoScenario.getScenarios().add(new Scenario("TEST_GRIFFIN"));
		recoService.executeRecommendationScenario(recoScenario, true);
		Assert.assertNotNull(recoScenario);
		Assert.assertFalse(recoScenario.getScenarioHashes().isEmpty());
		Assert.assertTrue(recoScenario.getResultObjects().isEmpty());

		recoService.executeRecommendationScenario(recoScenario, false);
		Assert.assertNotNull(recoScenario);
		Assert.assertFalse(recoScenario.getScenarioHashes().isEmpty());
		Assert.assertFalse(recoScenario.getResultObjects().isEmpty());
	}

}
