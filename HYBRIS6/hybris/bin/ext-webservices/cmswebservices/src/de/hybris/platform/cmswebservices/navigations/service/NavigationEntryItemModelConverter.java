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
package de.hybris.platform.cmswebservices.navigations.service;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmswebservices.data.NavigationEntryData;
import de.hybris.platform.core.model.ItemModel;

/**
 * Interface that defines the converter functions for a given item type. It defines two functions, one to convert from
 * {@link NavigationEntryData} to {@link ItemModel} and another to convert from {@link ItemModel} to its Unique
 * Identifier value.
 *
 * @deprecated since version 6.3. Please use
 *             {@link de.hybris.platform.cmsfacades.navigations.service.NavigationEntryItemModelConverter} in the
 *             cmsfacades extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.3")
public interface NavigationEntryItemModelConverter
		extends de.hybris.platform.cmsfacades.navigations.service.NavigationEntryItemModelConverter
{
	// Intentionally left empty.
}
