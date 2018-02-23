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

import de.hybris.platform.jalo.SessionContext;


public class CMSYmktCampaignRestriction extends GeneratedCMSYmktCampaignRestriction
{
	/**
	 * @deprecated since the very beginning. It has never been supported. Just here as the inheritance requires it.
	 *             Please, use
	 *             {@link com.hybris.ymkt.segmentation.model.CMSYmktCampaignRestrictionModel#getDescription()}.
	 */
	@Deprecated
	@Override
	@SuppressWarnings("squid:S1133")
	public String getDescription(final SessionContext ctx)
	{
		throw new UnsupportedOperationException();
	}
}
