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
package de.hybris.platform.b2b.strategies;

import java.util.List;


/**
 * A strategy for getting available user group codes that a B2BCustomer can be assigned to.
 */
public interface B2BUserGroupsLookUpStrategy
{

	/**
	 * Gets the user groups.
	 *
	 * @return the user groups
	 */
	List<String> getUserGroups();
}