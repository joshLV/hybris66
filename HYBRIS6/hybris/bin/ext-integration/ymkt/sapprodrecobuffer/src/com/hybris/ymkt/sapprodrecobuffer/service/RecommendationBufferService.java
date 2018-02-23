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
package com.hybris.ymkt.sapprodrecobuffer.service;

import java.util.Date;
import java.util.List;

import com.hybris.ymkt.sapprodrecobuffer.model.SAPOfferInteractionModel;
import com.hybris.ymkt.sapprodrecobuffer.model.SAPRecoClickthroughModel;
import com.hybris.ymkt.sapprodrecobuffer.model.SAPRecoImpressionAggrModel;
import com.hybris.ymkt.sapprodrecobuffer.model.SAPRecoImpressionModel;
import com.hybris.ymkt.sapprodrecobuffer.model.SAPRecommendationBufferModel;


/**
 * Collection of database usage for recommendation.
 */
public interface RecommendationBufferService
{
	/**
	 * Read a {@link List} of {@link SAPRecoImpressionAggrModel}.
	 * 
	 * @param batchSize
	 *           Batch size.
	 * @return {@link List} of {@link SAPRecoImpressionAggrModel}.
	 */
	List<SAPRecoImpressionAggrModel> getAggregatedImpressions(int batchSize);

	/**
	 * Read {@link List} of {@link SAPRecoClickthroughModel}.
	 * 
	 * @param batchSize
	 *           Batch size.
	 * @return {@link List} of {@link SAPRecoClickthroughModel}
	 */
	List<SAPRecoClickthroughModel> getClickthroughs(int batchSize);

	/**
	 * Get recommendations with scope R. If none are found, get recommendations with scope G
	 *
	 * @param scenarioId
	 *           Scenario ID.
	 * @param leadingItems
	 *           Leading items.
	 * @return {@link SAPRecommendationBufferModel}.
	 */
	SAPRecommendationBufferModel getGenericRecommendation(String scenarioId, String leadingItems);

	/**
	 * Read {@link List} of {@link SAPRecoImpressionModel}.
	 * 
	 * @param batchSize
	 *           Batch size.
	 * @return {@link List} of {@link SAPRecoImpressionModel}
	 */
	List<SAPRecoImpressionModel> getImpressions(int batchSize);

	/**
	 * Read {@link List} of {@link SAPOfferInteractionModel}.
	 * 
	 * @param batchSize
	 *           Batch size.
	 * @return {@link List} of {@link SAPOfferInteractionModel}.
	 */
	List<SAPOfferInteractionModel> getOfferInteractions(int batchSize);

	/**
	 * Get a recommendation.
	 *
	 * @param userId
	 *           User ID.
	 * @param scenarioId
	 *           Scenario ID.
	 * @param leadingItems
	 *           Leading items.
	 * @return {@link SAPRecommendationBufferModel}.
	 */
	SAPRecommendationBufferModel getRecommendation(String userId, String scenarioId, String leadingItems);

	/**
	 * Check if a recommendation is expired.
	 *
	 * @param recommendation
	 *           {@link SAPRecommendationBufferModel}.
	 *
	 * @return true if expired, false otherwise.
	 */
	boolean isRecommendationExpired(SAPRecommendationBufferModel recommendation);

	/**
	 * Remove expired mappings based on the expiry offset.
	 */
	void removeExpiredMappings();

	/**
	 * Remove expired recommendations based on the expiry offset.
	 */
	void removeExpiredRecommendations();

	/**
	 * Remove expired mappings based on the expiry offset.
	 */
	void removeExpiredTypeMappings();

	/**
	 * Add a new recommendation entry.
	 *
	 * @param userId
	 *           User ID.
	 * @param scenarioId
	 *           Scenario ID.
	 * @param hashId
	 *           Hash ID.
	 * @param leadingItems
	 *           Leading items.
	 * @param recoList
	 *           Recommendation List.
	 * @param recoType
	 *           Recommendation Type.
	 * @param expiresOn
	 *           Expire date.
	 */
	void saveRecommendation(String userId, String scenarioId, String hashId, String leadingItems, String recoList, String recoType,
			Date expiresOn);
}
