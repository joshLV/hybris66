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
package de.hybris.platform.sap.c4c.quote.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.sap.c4c.quote.constants.C4cquoteConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class C4cquoteManager extends GeneratedC4cquoteManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( C4cquoteManager.class.getName() );
	
	public static final C4cquoteManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (C4cquoteManager) em.getExtension(C4cquoteConstants.EXTENSIONNAME);
	}
	
}
