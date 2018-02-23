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
package com.hybris.ymkt.segmentation.jalo;

import de.hybris.platform.core.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.ymkt.segmentation.constants.SapymktsegmentationConstants;



/**
 * This is the extension manager of the Sapymktsegmentation extension.
 */
public class SapymktsegmentationManager extends GeneratedSapymktsegmentationManager
{
	/** Edit the local|project.properties to change logging behavior (properties 'log4j.*'). */
	private static final Logger LOG = LoggerFactory.getLogger(SapymktsegmentationManager.class);

	/**
	 * Never call the constructor of any manager directly, call getInstance() You can place your business logic here -
	 * like registering a jalo session listener. Each manager is created once for each tenant.
	 */
	public SapymktsegmentationManager() // NOPMD 
	{
		LOG.debug("constructor of SapymktsegmentationManager called.");
	}

	/**
	 * Get the valid instance of this manager.
	 * 
	 * @return the current instance of this manager
	 */
	public static SapymktsegmentationManager getInstance()
	{
		return (SapymktsegmentationManager) Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
				.getExtension(SapymktsegmentationConstants.EXTENSIONNAME);
	}
}