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
package de.hybris.platform.timedaccesspromotionenginefacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.timedaccesspromotionenginefacades.constants.TimedaccesspromotionenginefacadesConstants;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class TimedaccesspromotionenginefacadesManager extends GeneratedTimedaccesspromotionenginefacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TimedaccesspromotionenginefacadesManager.class.getName() );
	
	public static final TimedaccesspromotionenginefacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TimedaccesspromotionenginefacadesManager) em.getExtension(TimedaccesspromotionenginefacadesConstants.EXTENSIONNAME);
	}
	
}
