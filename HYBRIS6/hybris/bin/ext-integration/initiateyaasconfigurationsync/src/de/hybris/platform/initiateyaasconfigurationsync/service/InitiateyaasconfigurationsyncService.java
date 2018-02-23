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
package de.hybris.platform.initiateyaasconfigurationsync.service;

/**
 * The interface of InitiateyaasconfigurationsyncService.
 */
public interface InitiateyaasconfigurationsyncService
{
	/**
	 * Get the hybris logo url.
	 * @param logoCode	logo code
	 * @return	logo url
	 */
	String getHybrisLogoUrl(String logoCode);

	/**
	 * Create logo by given logo code.
	 * @param logoCode logo code
	 */
	void createLogo(String logoCode);
}