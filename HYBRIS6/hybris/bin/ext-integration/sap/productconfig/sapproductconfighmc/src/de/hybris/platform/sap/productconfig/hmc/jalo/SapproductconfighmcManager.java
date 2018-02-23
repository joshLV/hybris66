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
package de.hybris.platform.sap.productconfig.hmc.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.sap.productconfig.hmc.constants.SapproductconfighmcConstants;

import org.apache.log4j.Logger;



/**
 * This is the extension manager of the Sapproductconfighmc extension.
 */
public class SapproductconfighmcManager extends GeneratedSapproductconfighmcManager
{
	/** Edit the local|project.properties to change logging behavior (properties 'log4j.*'). */
	private static final Logger LOG = Logger.getLogger(SapproductconfighmcManager.class.getName());

	/*
	 * Some important tips for development:
	 *
	 * Do NEVER use the default constructor of manager's or items. => If you want to do something whenever the manger is
	 * created use the init() or destroy() methods described below
	 *
	 * Do NEVER use STATIC fields in your manager or items! => If you want to cache anything in a "static" way, use an
	 * instance variable in your manager, the manager is created only once in the lifetime of a "deployment" or tenant.
	 */


	/**
	 * Never call the constructor of any manager directly, call getInstance() You can place your business logic here -
	 * like registering a jalo session listener. Each manager is created once for each tenant.
	 */
	public SapproductconfighmcManager() // NOPMD
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("constructor of SapproductconfighmcManager called.");
		}
	}


	/**
	 * Get the valid instance of this manager.
	 *
	 * @return the current instance of this manager
	 */
	public static SapproductconfighmcManager getInstance()
	{
		return (SapproductconfighmcManager) Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
				.getExtension(SapproductconfighmcConstants.EXTENSIONNAME);
	}
}
