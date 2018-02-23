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
package de.hybris.platform.cmswebservices.items.facade;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;


/**
 * Component facade interface which deals with methods related to component operations.
 *
 * @deprecated since version 6.3. Please use {@link de.hybris.platform.cmsfacades.items.ComponentItemFacade} in the
 *             cmsfacades extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.3")
public interface ComponentItemFacade extends de.hybris.platform.cmsfacades.items.ComponentItemFacade
{
	// Intentionally left empty.
}
