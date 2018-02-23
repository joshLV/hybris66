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
package de.hybris.platform.cmswebservices.media.service;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;


/**
 * Provide methods for retrieving media format information for a given CMS component model.
 *
 * @deprecated since version 6.3. Please use {@link de.hybris.platform.cmsfacades.media.service.CMSMediaFormatService}
 *             in the cmsfacades extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.3")
public interface CMSMediaFormatService extends de.hybris.platform.cmsfacades.media.service.CMSMediaFormatService
{
	// Intentionally left empty.
}
