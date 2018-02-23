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
package de.hybris.platform.chinesepspwechatpaysamplesaddon.jalo;

import de.hybris.platform.chinesepspwechatpaysamplesaddon.constants.ChinesepspwechatpaysamplesaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class ChinesepspwechatpaysamplesaddonManager extends GeneratedChinesepspwechatpaysamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepspwechatpaysamplesaddonManager.class.getName() );
	
	public static final ChinesepspwechatpaysamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepspwechatpaysamplesaddonManager) em.getExtension(ChinesepspwechatpaysamplesaddonConstants.EXTENSIONNAME);
	}
	
}
