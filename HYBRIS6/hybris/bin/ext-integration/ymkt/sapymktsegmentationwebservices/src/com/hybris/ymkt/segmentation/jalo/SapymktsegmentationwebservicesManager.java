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

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import com.hybris.ymkt.segmentation.constants.SapymktsegmentationwebservicesConstants;

@SuppressWarnings("PMD")
public class SapymktsegmentationwebservicesManager extends GeneratedSapymktsegmentationwebservicesManager
{
	
	public static final SapymktsegmentationwebservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SapymktsegmentationwebservicesManager) em.getExtension(SapymktsegmentationwebservicesConstants.EXTENSIONNAME);
	}
	
}
