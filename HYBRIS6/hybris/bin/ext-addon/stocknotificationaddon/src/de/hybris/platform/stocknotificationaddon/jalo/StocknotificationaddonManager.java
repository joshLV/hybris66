/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.stocknotificationaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.stocknotificationaddon.constants.StocknotificationaddonConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class StocknotificationaddonManager extends GeneratedStocknotificationaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( StocknotificationaddonManager.class.getName() );
	
	public static final StocknotificationaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (StocknotificationaddonManager) em.getExtension(StocknotificationaddonConstants.EXTENSIONNAME);
	}
	
}
