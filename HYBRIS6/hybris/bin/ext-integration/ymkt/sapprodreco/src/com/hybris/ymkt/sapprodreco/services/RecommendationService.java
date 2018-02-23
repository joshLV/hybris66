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

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.ymkt.common.http.HttpURLConnectionRequest;
import com.hybris.ymkt.common.http.HttpURLConnectionResponse;
import com.hybris.ymkt.common.odata.ODataService;
import com.hybris.ymkt.common.user.UserContextService;
import com.hybris.ymkt.recentvieweditemsservices.RecentViewedItemsService;
import com.hybris.ymkt.sapprodreco.constants.SapprodrecoConstants;
import com.hybris.ymkt.sapprodreco.dao.ProductRecommendationData;
import com.hybris.ymkt.sapprodreco.dao.RecommendationContext;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario.BasketObject;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario.LeadingObject;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario.ResultObject;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario.Scenario;
import com.hybris.ymkt.sapprodreco.dao.RecommendationScenario.ScenarioHash;
import com.hybris.ymkt.sapprodrecobuffer.model.SAPRecommendationBufferModel;
import com.hybris.ymkt.sapprodrecobuffer.service.RecommendationBufferService;


/**
 * This service perform the 'get recommendation' actions, such as reading all scenario hashes of a user or read the
 * recommendation for a given scenario and context.
 */
public class RecommendationService
{

	protected static final String APPLICATION_JSON = "application/json";

	private static final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

	protected static final EntityProviderReadProperties NO_READ_PROPERTIES = EntityProviderReadProperties.init().build();
	protected static final String RECOMMENDATION_SCENARIOS = "RecommendationScenarios";

	protected CartService cartService;
	protected ODataService oDataService;
	protected RecentViewedItemsService recentViewedItemsService;
	protected RecommendationBufferService recommendationBufferService;
	protected UserContextService userContextService;

	protected List<ProductRecommendationData> convertBuffer(final SAPRecommendationBufferModel buffer)
	{
		return Arrays.stream(buffer.getRecoList().split("\\s*,\\s*")) //
				.map(ProductRecommendationData::new) //
				.collect(Collectors.toList());
	}

	protected Map<String, Object> convertMapBasketObject(final BasketObject bo)
	{
		final Map<String, Object> leadingObject = new HashMap<>();
		leadingObject.put("BasketObjectType", bo.getBasketObjectType());
		leadingObject.put("BasketObjectId", bo.getBasketObjectId());
		return leadingObject;
	}

	protected Map<String, Object> convertMapLeadingObject(final LeadingObject lo)
	{
		final Map<String, Object> leadingObject = new HashMap<>();
		leadingObject.put("LeadingObjectType", lo.getLeadingObjectType());
		leadingObject.put("LeadingObjectId", lo.getLeadingObjectId());
		return leadingObject;
	}

	protected Map<String, Object> convertMapScenario(final Scenario s)
	{
		final Map<String, Object> scenario = new HashMap<>();
		scenario.put("ScenarioId", s.getScenarioId());
		scenario.put("LeadingObjects",
				s.getLeadingObjects().stream() //
						.map(this::convertMapLeadingObject) //
						.collect(Collectors.toList()));
		scenario.put("BasketObjects",
				s.getBasketObjects().stream() //
						.map(this::convertMapBasketObject) //
						.collect(Collectors.toList()));
		return scenario;
	}

	protected Map<String, Object> convertMapScenarioHash(final ScenarioHash sh)
	{
		final Map<String, Object> scenarioHash = new HashMap<>();
		scenarioHash.put("ScenarioId", sh.getScenarioId());
		scenarioHash.put("HashId", sh.getHashId());
		return scenarioHash;
	}

	protected RecommendationScenario createRecommendationScenario(final RecommendationContext context)
	{
		final RecommendationScenario recoScenario = new RecommendationScenario(this.userContextService.getUserId(),
				this.userContextService.getUserOrigin());


		// Add User's cookie scenario hash matching the scenario Id


		final Scenario scenario = new Scenario(context.getScenarioId());

		// Last seen product or categories
		for (final String leadingObjectId : context.getLeadingItemId())
		{
			scenario.getLeadingObjects().add(new LeadingObject(context.getLeadingItemDSType(), leadingObjectId));
		}

		// Basket items
		if (StringUtils.isNotBlank(context.getCartItemDSType()))
		{
			for (final String basketObjectId : this.getCartItemsFromSession())
			{
				scenario.getBasketObjects().add(new BasketObject(context.getCartItemDSType(), basketObjectId));
			}
		}

		// Basket items placed in the leading items
		if (context.isIncludeCart())
		{
			for (final String leadingObjectId : this.getCartItemsFromSession())
			{
				scenario.getLeadingObjects().add(new LeadingObject(context.getLeadingItemDSType(), leadingObjectId));
			}
		}

		// Last x viewed products or categories
		if (context.isIncludeRecent())
		{
			List<String> itemIds = Collections.emptyList();
			if (SapprodrecoConstants.PRODUCT.equals(context.getLeadingItemType()))
			{
				itemIds = this.recentViewedItemsService.getRecentViewedProducts();
			}
			else if (SapprodrecoConstants.CATEGORY.equals(context.getLeadingItemType()))
			{
				itemIds = this.recentViewedItemsService.getRecentViewedCategories();
			}
			for (final String leadingObjectId : itemIds)
			{
				scenario.getLeadingObjects().add(new LeadingObject(context.getLeadingItemDSType(), leadingObjectId));
			}
		}

		recoScenario.getScenarios().add(scenario);

		return recoScenario;
	}

	protected void executeRecommendationScenario(final RecommendationScenario objRecoScenario, final boolean scenarioHashesOnly)
			throws IOException
	{
		try
		{
			final HttpURLConnectionRequest request = new HttpURLConnectionRequest("POST",
					this.oDataService.createURL(RECOMMENDATION_SCENARIOS));
			request.getRequestProperties().put("Accept", APPLICATION_JSON);
			request.getRequestProperties().put("Content-Type", APPLICATION_JSON);

			final Map<String, Object> recommendationScenario = new LinkedHashMap<>();
			recommendationScenario.put("UserId", objRecoScenario.getUserId());
			recommendationScenario.put("UserType", objRecoScenario.getUserType());
			recommendationScenario.put("ExternalTracking", Boolean.TRUE);

			// User's precomputed scenario's target group.
			recommendationScenario.put("ScenarioHashes",
					objRecoScenario.getScenarioHashes().stream() //
							.map(this::convertMapScenarioHash) //
							.collect(Collectors.toList()));

			recommendationScenario.put("Scenarios",
					objRecoScenario.getScenarios().stream() //
							.map(this::convertMapScenario) //
							.collect(Collectors.toList()));

			if (!scenarioHashesOnly)
			{
				// Adding a empty array of ResultObjects will request the ResultObjects
				recommendationScenario.put("ResultObjects", Collections.emptyList());
			}

			final byte[] payload = this.oDataService.convertMapToJSONPayload(RECOMMENDATION_SCENARIOS, recommendationScenario);
			request.setPayload(payload);

			final HttpURLConnectionResponse response = this.oDataService.executeWithRetry(request);
			LOG.debug("Response payload : {}", new String(response.getPayload(), StandardCharsets.UTF_8));

			final EdmEntitySet entitySet = this.oDataService.getEdm().getDefaultEntityContainer()
					.getEntitySet(RECOMMENDATION_SCENARIOS);
			try (InputStream inputStream = new ByteArrayInputStream(response.getPayload()))
			{
				final ODataEntry oData = EntityProvider.readEntry(APPLICATION_JSON, entitySet, inputStream, NO_READ_PROPERTIES);
				objRecoScenario.update(oData.getProperties());
			}
		}
		catch (final IOException | EntityProviderException | EdmException e)
		{
			throw new IOException("Error reading recommendation scenario " + objRecoScenario, e);
		}
	}

	protected List<String> getCartItemsFromSession()
	{
		return this.cartService.getSessionCart().getEntries().stream() //
				.map(AbstractOrderEntryModel::getProduct) //
				.map(ProductModel::getCode) //
				.collect(Collectors.toList());
	}

	/**
	 * Read {@link ProductRecommendationData}s according to {@link RecommendationContext}.
	 *
	 * @param context
	 *           Parameters of the recommendations to read.
	 * @return {@link List} of {@link ProductRecommendationData}
	 */
	public List<ProductRecommendationData> getProductRecommendation(final RecommendationContext context)
	{
		final String scenarioId = context.getScenarioId();

		final RecommendationScenario recoScenario = this.createRecommendationScenario(context);

		final Scenario scenario = recoScenario.getScenarios().get(0);

		final String leadingItems = scenario.getLeadingObjects().stream() //
				.map(LeadingObject::getLeadingObjectId) //
				.sorted() //
				.collect(Collectors.joining(","));

		SAPRecommendationBufferModel buffer;

		// Read buffered results

		final boolean anonymousUser = this.userContextService.isAnonymousUser();
		if (anonymousUser)
		{
			buffer = recommendationBufferService.getGenericRecommendation(scenarioId, leadingItems);
		}
		else
		{
			buffer = recommendationBufferService.getRecommendation(recoScenario.getUserId(), scenarioId, leadingItems);
		}

		if (buffer != null && !recommendationBufferService.isRecommendationExpired(buffer))
		{
			// Buffer has good results
			return this.convertBuffer(buffer);
		}

		// Buffer is not good, request yMKT.
		try
		{
			final List<ScenarioHash> oldScenarioHashes = new ArrayList<>(recoScenario.getScenarioHashes());
			this.executeRecommendationScenario(recoScenario, false);
			this.updateUserScenarioHashes(oldScenarioHashes, recoScenario.getScenarioHashes());
			this.updateRecommendationBuffer(leadingItems, recoScenario);

			// Backend's result is good
			return recoScenario.getResultObjects().stream() //
					.map(ResultObject::getResultObjectId) //
					.map(ProductRecommendationData::new) //
					.collect(Collectors.toList());
		}
		catch (final IOException e)
		{
			LOG.error("Error reading recommendation from backend using= {} and RecommendationContext = {}", recoScenario, context,
					e);
		}

		// yMKT failed, try to get something because its better than nothing.

		if (buffer != null)
		{
			// Expired results.
			return this.convertBuffer(buffer);
		}

		// Logged in but no results, yMKT failed, therefore try anonymous buffer
		if (!anonymousUser)
		{
			buffer = recommendationBufferService.getGenericRecommendation(context.getScenarioId(), leadingItems);
			if (buffer != null)
			{
				return this.convertBuffer(buffer);
			}
		}

		// Could continue trying to read buffer with less leading products ?

		return Collections.emptyList();
	}

	protected List<String> getRecentItemsFromSession(final String leadingItemType)
	{
		switch (leadingItemType)
		{
			case SapprodrecoConstants.PRODUCT:
				return this.recentViewedItemsService.getRecentViewedProducts();
			case SapprodrecoConstants.CATEGORY:
				return this.recentViewedItemsService.getRecentViewedCategories();
			default:
				LOG.error("Invalid leadingItemType='{}' supplied.", leadingItemType);
				return Collections.emptyList();
		}
	}

	/**
	 * This method read all ScenarioHash for the provided scenarioId of the current user.<br>
	 *
	 *
	 * @param scenarioIds
	 *           {@link List} of scenario ids.
	 * @return {@link List} of {@link ScenarioHash}. The resulting list may not contain all requested scenario ids.
	 *
	 */
	public List<ScenarioHash> getScenarioHashes(final List<String> scenarioIds)
	{
		final RecommendationScenario recoScenario = new RecommendationScenario(this.userContextService.getUserId(),
				this.userContextService.getUserOrigin());

		// Add all scenarioId to recoScenario
		scenarioIds.stream() //
				.map(Scenario::new) //
				.collect(Collectors.toCollection(() -> recoScenario.getScenarios()));

		try
		{
			this.executeRecommendationScenario(recoScenario, true);
			return recoScenario.getScenarioHashes();
		}
		catch (final IOException e)
		{
			LOG.error("Error reading scenario hashes for = {}", scenarioIds, e);
			return Collections.emptyList();
		}
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Required
	public void setODataService(final ODataService oDataService)
	{
		this.oDataService = oDataService;
	}

	@Required
	public void setRecentViewedItemsService(final RecentViewedItemsService recentViewedItemsService)
	{
		this.recentViewedItemsService = recentViewedItemsService;
	}

	@Required
	public void setRecommendationBufferService(final RecommendationBufferService recommendationBufferService)
	{
		this.recommendationBufferService = recommendationBufferService;
	}

	@Required
	public void setUserContextService(final UserContextService userContextService)
	{
		this.userContextService = userContextService;
	}

	protected void updateRecommendationBuffer(final String leadingItems, final RecommendationScenario reco)
	{
		final List<ScenarioHash> scenarioHashes = reco.getScenarioHashes();
		if (scenarioHashes.isEmpty())
		{
			return;
		}
		assert scenarioHashes.size() == 1 : scenarioHashes.size();
		final ScenarioHash scenarioHash = scenarioHashes.get(0);

		final List<ResultObject> resultObjects = reco.getResultObjects();
		if (resultObjects.isEmpty())
		{
			return;
		}

		final String userId = reco.getUserId();
		final String scenarioId = scenarioHash.getScenarioId();
		final String hashId = scenarioHash.getHashId();

		final String recoList = resultObjects.stream().map(ResultObject::getResultObjectId).collect(Collectors.joining(","));
		final String recoType = scenarioHash.getResultScope();
		final Date expiresOn = scenarioHash.getExpiresOn();

		recommendationBufferService.saveRecommendation(userId, scenarioId, hashId, leadingItems, recoList, recoType, expiresOn);
	}

	protected void updateUserScenarioHashes(final List<ScenarioHash> oldHashes, final List<ScenarioHash> newHashes)
	{
		Map<String, ScenarioHash> newHashesMap = newHashes.stream().collect(Collectors.toMap(ScenarioHash::getScenarioId, z -> z));

		for (ScenarioHash oldHash : oldHashes)
		{
			ScenarioHash newHash = newHashesMap.get(oldHash.getScenarioId());
			if (oldHash.getHashId().equals(newHash.getHashId()))
			{
				// Replace user cookie with the new hash
			}
		}

		// Optional scope.
		// Detect if the expired on of the same scenario ID has changed which would invalidate all scenario hashes for a given scenario ID using old expire on.

	}

	/**
	 * Simple validation of recommendation hash ID. 32 characters hexadecimal uppercase.
	 *
	 * @param hashId
	 *           Hash ID to validate
	 * @return true if valid, false otherwise.
	 */
	public boolean validateScenarioHash(final String hashId)
	{
		if (hashId == null || hashId.length() != 32)
		{
			return false;
		}
		for (int i = 0; i < 32; i++)
		{
			final char c = hashId.charAt(i);
			if ((c < '0' || '9' < c) && (c < 'A' || 'F' < c))
			{
				return false;
			}
		}
		return true;
	}
}
