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

import de.hybris.platform.acceleratorstorefrontcommons.consent.data.ConsentCookieData;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.site.BaseSiteService;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.Cookie;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hybris.ymkt.common.consent.YmktConsentService;
import com.hybris.ymkt.common.user.UserContextService;


/**
 * User consent management integration with {@link ConsentFacade} 
 * and {@link ConsentModel} of {@link YmktConsentService}.
 */
public class DefaultYmktConsentService implements YmktConsentService
{
	
	protected static final String ANONYMOUS_CONSENT_COOKIE = "anonymous-consents";
	protected static final String CONSENT_GIVEN = "GIVEN";	

	private static final Logger LOG = LoggerFactory.getLogger(DefaultYmktConsentService.class);

	protected BaseSiteService baseSiteService;
	protected ConfigurationService configurationService;
	protected ConsentFacade consentFacade;
	protected FlexibleSearchService flexibleSearchService;
	protected UserContextService userContextService;

	@Override
	public boolean getUserConsent(final String consentID)
	{
		return this.userContextService.isAnonymousUser() ? getAnonymousUserConsent(consentID) : getRegisteredUserConsent(consentID);
	}

	@Override
	public boolean getUserConsent(String customerId, String consentId)
	{
		final Map<String, String> queryParams = new HashMap<>();
		queryParams.put("consentId", consentId);
		queryParams.put("customerID", customerId);

		final SearchResult<ConsentModel> search = this.flexibleSearchService.search("SELECT {co.pk} FROM {Consent AS co} " + //
				"WHERE {co.consentWithdrawnDate} IS NULL " + //
				"AND {co.consentTemplate} IN ({{SELECT {ct.pk} FROM {ConsentTemplate AS ct} WHERE {ct.id} = ?consentId}}) " + //
				"AND {co.customer} IN ({{SELECT {cu.pk} FROM {Customer AS cu} WHERE {cu.customerID} = ?customerID}})", queryParams);
		return search.getCount() > 0;
	}
	
	/**
	 * Read and return consent value from cookie for the current 
	 * anonymous user's session given a consent template ID.
	 * 
	 * @param consentID
	 * 			String configuration property containing consent template ID
	 * @return true if current anonymous user has given consent, false otherwise
	 */
	protected boolean getAnonymousUserConsent(final String consentID)
	{
		final String consentTemplateId = getConsentTemplateID(consentID);

		if (consentTemplateId == null)
		{
			return false;
		}

		try
		{
			final ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			final Cookie[] cookies = attributes.getRequest().getCookies();

			if (cookies == null)
			{
				return false;
			}

			for (final Cookie cookie : cookies)
			{
				if (cookie.getName().startsWith(ANONYMOUS_CONSENT_COOKIE))
				{
					final String decodedValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
					return Arrays.stream(new ObjectMapper().readValue(decodedValue, ConsentCookieData[].class)) //
							.filter(cd -> consentTemplateId.equals(cd.getTemplateCode())) //
							.map(ConsentCookieData::getConsentState) //
							.filter(Objects::nonNull) //
							.anyMatch(c -> c.equals(CONSENT_GIVEN));
				}
			}
		}
		catch (final IllegalStateException e)
		{
			LOG.info("Not executing within a web request", e);
		}
		catch (final IOException e)
		{
			LOG.info("Exception when reading cookie value", e);
		}

		return false;
	}

	/**
	 * Read and return consent value for the current logged-in user
	 * given a consent template ID.
	 * 
	 * @param consentID
	 * 			String configuration property containing consent template ID
	 * @return true if current logged in user has given consent, false otherwise
	 */
	protected boolean getRegisteredUserConsent(final String consentID)
	{
		final String consentTemplateId = getConsentTemplateID(consentID);

		if (consentTemplateId == null)
		{
			return false;
		}
		
		final ConsentTemplateData consentTemplateData = this.consentFacade.getLatestConsentTemplate(consentTemplateId);
		return (consentTemplateData.getConsentData() != null && consentTemplateData.getConsentData().getConsentWithdrawnDate() == null);		
		
	}
	
	/**
	 * Look-up consent template ID from provided configuration property 
	 * @param consentID
	 * 			  String configuration property containing consent template ID
	 * @return String consent template ID
	 */
	private String getConsentTemplateID(final String consentID)
	{
		return this.configurationService.getConfiguration().getString(consentID);
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Required
	public void setConsentFacade(final ConsentFacade consentFacade)
	{
		this.consentFacade = consentFacade;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setUserContextService(final UserContextService userContextService)
	{
		this.userContextService = userContextService;
	}

}
