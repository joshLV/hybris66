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
package de.hybris.platform.customercouponfacades.jalo;

import de.hybris.platform.customercouponfacades.constants.CustomercouponfacadesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class CustomercouponfacadesManager extends GeneratedCustomercouponfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( CustomercouponfacadesManager.class.getName() );
	
	public static final CustomercouponfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CustomercouponfacadesManager) em.getExtension(CustomercouponfacadesConstants.EXTENSIONNAME);
	}
	
}
