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
package de.hybris.platform.cmswebservices.common.facade.populator;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;


/**
 * Interface {@link LocalizedPopulator} is used to retrieve the languages supported by the current base store and to
 * populate localized attributes.
 *
 * @deprecated since version 6.3. Please use {@link de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator}
 *             in the cmsfacades extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.3")
public interface LocalizedPopulator extends de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator
{
	// Intentionally left empty.
}
