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
package de.hybris.platform.cmswebservices.media.facade;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;


/**
 * Facade for managing Media.
 *
 * @deprecated since version 6.3. Please use {@link de.hybris.platform.cmsfacades.media.MediaFacade} in the cmsfacades
 *             extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.3")
public interface MediaFacade extends de.hybris.platform.cmsfacades.media.MediaFacade
{
	// Intentionally left empty.
}
