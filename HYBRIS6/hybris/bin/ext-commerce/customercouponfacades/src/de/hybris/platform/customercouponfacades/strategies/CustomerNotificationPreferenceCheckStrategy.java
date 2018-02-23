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

package de.hybris.platform.customercouponfacades.strategies;

/**
 * Strategy for checking customer notification preference
 */
public interface CustomerNotificationPreferenceCheckStrategy
{
	/**
	 * 
	 * check if the current customer subscrib any type of notification
	 * 
	 * @return true if customer subscrib at least one type of notification false otherwise
	 */
	Boolean checkCustomerNotificationPreference();
}
