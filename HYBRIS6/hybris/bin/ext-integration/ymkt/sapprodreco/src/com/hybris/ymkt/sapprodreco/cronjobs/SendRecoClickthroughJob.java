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
package com.hybris.ymkt.sapprodreco.cronjobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.ymkt.sapprodreco.services.InteractionService;

public class SendRecoClickthroughJob extends AbstractJobPerformable<CronJobModel> {

	private static final Logger LOG = LoggerFactory.getLogger(SendRecoClickthroughJob.class);

	protected InteractionService interactionService;

	@Override
	public PerformResult perform(final CronJobModel job) {

		LOG.info("{} started at {}", job.getCode(),
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(job.getStartTime()));

		try {

			interactionService.sendInteractions();

			LOG.info("{} completed in {}ms", job.getCode(), System.currentTimeMillis() - job.getStartTime().getTime());

			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		} catch (final Exception e) {

			LOG.error("{} aborted: error occurred during sending clickthroughs.", job.getCode(), e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}

	/**
	 * @param interactionService
	 *            the interactionService to set
	 */
	@Required
	public void setInteractionService(final InteractionService interactionService) {
		this.interactionService = interactionService;
	}

}