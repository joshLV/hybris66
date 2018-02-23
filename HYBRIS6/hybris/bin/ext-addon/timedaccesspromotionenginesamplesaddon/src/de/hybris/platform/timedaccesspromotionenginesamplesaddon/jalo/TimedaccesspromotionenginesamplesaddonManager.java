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
package de.hybris.platform.timedaccesspromotionenginesamplesaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.timedaccesspromotionenginesamplesaddon.constants.TimedaccesspromotionenginesamplesaddonConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class TimedaccesspromotionenginesamplesaddonManager extends GeneratedTimedaccesspromotionenginesamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TimedaccesspromotionenginesamplesaddonManager.class.getName() );
	
	public static final TimedaccesspromotionenginesamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TimedaccesspromotionenginesamplesaddonManager) em.getExtension(TimedaccesspromotionenginesamplesaddonConstants.EXTENSIONNAME);
	}
	
}
