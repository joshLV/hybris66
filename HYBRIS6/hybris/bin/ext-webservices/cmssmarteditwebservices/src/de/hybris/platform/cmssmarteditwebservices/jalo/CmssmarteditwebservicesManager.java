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
package de.hybris.platform.cmssmarteditwebservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;

@SuppressWarnings("PMD")
public class CmssmarteditwebservicesManager extends GeneratedCmssmarteditwebservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( CmssmarteditwebservicesManager.class.getName() );
	
	public static final CmssmarteditwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CmssmarteditwebservicesManager) em.getExtension(CmssmarteditwebservicesConstants.EXTENSIONNAME);
	}
	
}
