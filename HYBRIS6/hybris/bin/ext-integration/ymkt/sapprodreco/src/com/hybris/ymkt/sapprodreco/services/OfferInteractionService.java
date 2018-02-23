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

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.ymkt.common.http.HttpURLConnectionRequest;
import com.hybris.ymkt.common.http.HttpURLConnectionResponse;
import com.hybris.ymkt.common.odata.ODataService;
import com.hybris.ymkt.common.user.UserContextService;
import com.hybris.ymkt.sapprodreco.dao.OfferInteractionContext;
import com.hybris.ymkt.sapprodreco.dao.OfferInteractionContext.Interaction;
import com.hybris.ymkt.sapprodreco.dao.OfferInteractionContext.Offer;
import com.hybris.ymkt.sapprodrecobuffer.model.SAPOfferInteractionModel;
import com.hybris.ymkt.sapprodrecobuffer.service.RecommendationBufferService;


/**
 * This services is used to send offer interactions to SAP Hybris Marketing
 */
public class OfferInteractionService
{
	protected static final String APPLICATION_JSON = "application/json";
	protected static final String COMMUNICATION_MEDIUM = "ONLINE_SHOP";
	protected static final String IMPORTHEADERS = "ImportHeaders";

	private static final Logger LOG = LoggerFactory.getLogger(OfferInteractionService.class);

	protected static final int MAX_FAILURE = 3;
	protected static final EntityProviderReadProperties NO_READ_PROPERTIES = EntityProviderReadProperties.init().build();
	protected static final int READTIMEOUT = 300000;

	protected ModelService modelService;
	protected ODataService oDataService;
	protected int readBatchSize;
	protected RecommendationBufferService recommendationBufferService;
	protected UserContextService userContextService;

	/**
	 * Helper method that will bundle all the interactions for the property "Interactions" in the payload we send to ymkt
	 *
	 * @param model
	 * @return Interaction Map
	 */
	protected Map<String, Object> buildInteractionMap(SAPOfferInteractionModel model)
	{
		final Map<String, Object> interactionMap = new HashMap<>();
		final Map<String, String> offerMap = new HashMap<>();

		interactionMap.put("Key", ""); // mandatory field
		interactionMap.put("CommunicationMedium", COMMUNICATION_MEDIUM);
		interactionMap.put("Timestamp", model.getTimeStamp());
		interactionMap.put("InteractionType", model.getInteractionType());
		interactionMap.put("IsAnonymous", Boolean.FALSE);
		interactionMap.put("ContactId", model.getContactId());
		interactionMap.put("ContactIdOrigin", model.getContactIdOrigin());

		offerMap.put("Id", model.getOfferId());
		offerMap.put("ContentItemId", model.getOfferContentItemId());
		offerMap.put("RecommendationScenarioId", model.getOfferRecommendationScenarioId());

		interactionMap.put("Offers", offerMap);

		return interactionMap;
	}

	/**
	 * This method fills the existing offerInteractionContext with new values in preparation for the payload creation
	 *
	 * @param offerInteractionContext
	 */
	private void fillInteractionInInteractionContext(OfferInteractionContext offerInteractionContext)
	{
		Interaction interaction = offerInteractionContext.getInteractions().get(0);

		interaction.setKey("");
		interaction.setCommunicationMedium(COMMUNICATION_MEDIUM);
		interaction.setContactId(userContextService.getUserId());
		interaction.setContactIdOrigin(userContextService.getUserOrigin());
		interaction.setAnonymous(false);
		interaction.setTimeStamp(offerInteractionContext.getTimestamp());
	}

	/**
	 * Saves the interaction in the database table with itemType SAPOfferInteraction
	 *
	 * @param offerInteractionContext
	 *           {@link OfferInteractionContext}
	 */
	public void saveOfferInteraction(final OfferInteractionContext offerInteractionContext)
	{
		try
		{
			this.fillInteractionInInteractionContext(offerInteractionContext);

			SAPOfferInteractionModel offerInteractionModel = modelService.create(SAPOfferInteractionModel.class);
			Interaction interaction = offerInteractionContext.getInteractions().get(0); // we limit to 1 interaction
			Offer offer = interaction.getOffers().get(0); // we limit to 1 offer

			offerInteractionModel.setTimeStamp(offerInteractionContext.getTimestamp());
			offerInteractionModel.setContactId(interaction.getContactId());
			offerInteractionModel.setContactIdOrigin(interaction.getContactIdOrigin());			
			offerInteractionModel.setInteractionType(interaction.getInteractionType());
			offerInteractionModel.setOfferId(offer.getId());
			offerInteractionModel.setOfferContentItemId(offer.getContentItemId());
			offerInteractionModel.setOfferRecommendationScenarioId(offer.getRecommendationScenarioId());

			modelService.save(offerInteractionModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("An error occurred while saving Offer Interaction with {}", offerInteractionContext, e);
		}
	}

	private boolean sendOfferInteraction(List<Map<String, Object>> interactionsArray)
	{

		//Build object for JSON payload
		final Map<String, Object> offerInteractionPayloadMap = new HashMap<>();

		offerInteractionPayloadMap.put("Id", "");
		offerInteractionPayloadMap.put("Timestamp", System.currentTimeMillis());
		offerInteractionPayloadMap.put("Interactions", interactionsArray);

		try
		{
			final HttpURLConnectionRequest request = new HttpURLConnectionRequest("POST", this.oDataService.createURL(IMPORTHEADERS));
			request.getRequestProperties().put("Accept", APPLICATION_JSON);
			request.getRequestProperties().put("Content-Type", APPLICATION_JSON);
			request.setReadTimeout(READTIMEOUT);

			final byte[] payload = this.oDataService.convertMapToJSONPayload(IMPORTHEADERS, offerInteractionPayloadMap);
			request.setPayload(payload);
			LOG.debug("Request payload : {}", new String(request.getPayload(), StandardCharsets.UTF_8));

			final HttpURLConnectionResponse response = this.oDataService.executeWithRetry(request);
			LOG.debug("Response payload : {}", new String(response.getPayload(), StandardCharsets.UTF_8));

		}
		catch (final IOException e)
		{
			LOG.error("Error posting offer interaction: payload {}", offerInteractionPayloadMap, e);
			return false;
		}
		return true;
	}

	/**
	 * Send all offer interaction records via OData service.
	 */
	public void sendOfferInteractions()
	{
		int offerInteractionListSize = readBatchSize;
		int successCounter = 0;
		int failureCounter = 0;
		long readTotalTime = 0;
		long sendTotalTime = 0;
		long deleteTotalTime = 0;

		List<SAPOfferInteractionModel> interactionsModelsArray = new ArrayList<>();

		while (offerInteractionListSize == readBatchSize && failureCounter <= MAX_FAILURE)
		{

			long readStartTime = System.currentTimeMillis();
			final List<SAPOfferInteractionModel> offerInteractions = recommendationBufferService.getOfferInteractions(readBatchSize);
			readTotalTime += System.currentTimeMillis() - readStartTime;

			offerInteractionListSize = offerInteractions.size();
			for (int i = 0; i < offerInteractionListSize && failureCounter <= MAX_FAILURE; i++)
			{
				SAPOfferInteractionModel offerInteraction = offerInteractions.get(i);
				interactionsModelsArray.add(offerInteraction);
			}

			List<Map<String, Object>> interactionsArray = interactionsModelsArray.stream() //
					.map(this::buildInteractionMap) //
					.collect(Collectors.toList());

			final boolean iterationSuccess = sendOfferInteraction(interactionsArray);

			if (iterationSuccess)
			{
				successCounter++;
				long deleteStartTime = System.currentTimeMillis();
				for (SAPOfferInteractionModel interactionModel : interactionsModelsArray)
				{
					modelService.remove(interactionModel);
				}

				deleteTotalTime += System.currentTimeMillis() - deleteStartTime;
			}
			else
			{
				failureCounter++;
			}

		}

		LOG.info("Send Offer Interactions: Successful={}, Failed={}, RetrieveTime={}ms, SendTime={}ms, DeleteTime={}ms",
				successCounter, failureCounter, readTotalTime, sendTotalTime, deleteTotalTime);
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setODataService(ODataService oDataService)
	{
		this.oDataService = oDataService;
	}

	@Required
	public void setReadBatchSize(int readBatchSize)
	{
		LOG.debug("readBatchSize={}", readBatchSize);
		this.readBatchSize = readBatchSize;
	}

	@Required
	public void setRecommendationBufferService(RecommendationBufferService recommendationBufferService)
	{
		this.recommendationBufferService = recommendationBufferService;
	}

	@Required
	public void setUserContextService(UserContextService userContextService)
	{
		this.userContextService = userContextService;
	}
}
