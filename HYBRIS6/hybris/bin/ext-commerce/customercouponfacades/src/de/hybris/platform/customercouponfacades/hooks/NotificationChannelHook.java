/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.customercouponfacades.hooks;

import de.hybris.platform.core.model.user.CustomerModel;


/**
 * Hook for checking customer notification preference
 */
public interface NotificationChannelHook
{
	/**
	 * @param current
	 *           customer
	 * @return true if the specific channel is on
	 */
	public Boolean isChannelOn(final CustomerModel customer);
}
