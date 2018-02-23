/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.eventtracking.ws.services;

import com.bazaarvoice.jolt.exception.JsonUnmarshalException;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.apache.log4j.Logger;

import static java.util.Optional.ofNullable;


/**
 * @author stevo.slavic
 *
 */
public class DefaultRawEventEnricher implements RawEventEnricher
{
	private static final Logger LOG = Logger.getLogger(DefaultRawEventEnricher.class);
	private static final String ENRICHMENT_SPEC_TEMPLATE = "[{\"operation\":\"default\", " +
															 "\"spec\": { \"session_id\": \"%s\", " +
																		  "\"timestamp\": \"%s\", " +
																		  "\"user_id\": \"%s\", " +
																		  "\"user_email\": \"%s\", " +
																		  "\"base_site_id\": \"%s\" " +
																		  "} " +
														      "}]";

	private final UserService userService;
	private final BaseSiteService baseSiteService;

	public DefaultRawEventEnricher(final UserService userService, final BaseSiteService baseSiteService)
	{
		this.userService = userService;
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @see de.hybris.eventtracking.ws.services.RawEventEnricher#enrich(java.lang.String,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String enrich(final String json, final HttpServletRequest req)
	{
		final HttpSession session = req.getSession();
		final String sessionId = session.getId();
		final String timestamp = getCurrentTimestamp();
		final UserModel user = userService.getCurrentUser();
		String userId = null;
		String userEmail = null;
		if (user != null && CustomerModel.class.isAssignableFrom(user.getClass()))
		{
			userId = ((CustomerModel) user).getCustomerID();
			userEmail = ((CustomerModel) user).getContactEmail();
		}
		userId = StringUtils.trimToEmpty(userId);
		userEmail = StringUtils.trimToEmpty(userEmail);
		final String baseSiteId = getSiteId();

		final Chainr chainr = Chainr.fromSpec(JsonUtils.jsonToList(String.format(ENRICHMENT_SPEC_TEMPLATE, sessionId, timestamp,
				userId, userEmail, baseSiteId)));
		try {
			Map<String, Object> jsonObjectMap;
			jsonObjectMap = JsonUtils.javason(json);
			return JsonUtils.toJsonString(chainr.transform(jsonObjectMap));
		} catch (JsonUnmarshalException e){
			LOG.warn("Unexpected error occurred parsing json. " + e.getMessage());
			return json;
		}
	}

	protected String getCurrentTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000); // seconds since Unix epoch
	}

	protected String getSiteId(){
		return getCurrentBaseSiteModel().isPresent() ? getCurrentBaseSiteModel().get().getUid() : StringUtils.EMPTY;
	}

	protected Optional<BaseSiteModel> getCurrentBaseSiteModel() {
		return ofNullable(baseSiteService.getCurrentBaseSite());
	}

}
