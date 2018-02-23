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
package com.hybris.ymkt.consent.service.impl;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.site.BaseSiteService;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;



/**
 *
 */
public class DefaultYmktConsentServiceTest
{
	BaseSiteService baseSiteService = Mockito.mock(BaseSiteService.class);
	BaseSiteModel baseSiteModel = Mockito.mock(BaseSiteModel.class);
	Configuration configuration = Mockito.mock(Configuration.class);
	ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);
	ConsentFacade consentFacade = Mockito.mock(ConsentFacade.class);

	DefaultYmktConsentService defaultYmktConsentService = new DefaultYmktConsentService();

	@Before
	public void setUp() throws Exception
	{
		defaultYmktConsentService.setBaseSiteService(baseSiteService);
		defaultYmktConsentService.setConfigurationService(configurationService);
		defaultYmktConsentService.setConsentFacade(consentFacade);

		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSiteModel);
		Mockito.when(baseSiteModel.getUid()).thenReturn("site");

		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString("ymktsite")).thenReturn("YMKT_CONSENT");
	}

	@Test
	public void testGetAnonymousUserConsent()
	{
		Assert.assertFalse(defaultYmktConsentService.getAnonymousUserConsent("ymkt"));
	}

	@Test
	public void testGetRegisteredUserConsent()
	{
		Assert.assertFalse(defaultYmktConsentService.getRegisteredUserConsent("ymkt"));
	}

}
