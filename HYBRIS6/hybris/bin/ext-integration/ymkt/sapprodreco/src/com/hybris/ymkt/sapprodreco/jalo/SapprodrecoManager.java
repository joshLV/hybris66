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
package com.hybris.ymkt.sapprodreco.jalo;

import de.hybris.platform.jalo.JaloSession;

import com.hybris.ymkt.sapprodreco.constants.SapprodrecoConstants;


public class SapprodrecoManager extends GeneratedSapprodrecoManager
{

	public static final SapprodrecoManager getInstance()
	{
		return (SapprodrecoManager) JaloSession.getCurrentSession().getExtensionManager()
				.getExtension(SapprodrecoConstants.EXTENSIONNAME);
	}

}
