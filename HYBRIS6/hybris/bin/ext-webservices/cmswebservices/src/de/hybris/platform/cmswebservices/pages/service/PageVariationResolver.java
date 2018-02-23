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
package de.hybris.platform.cmswebservices.pages.service;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;


/**
 * Provide methods for retrieving default and variation page information for a given page type and/or for a given CMS
 * page model.
 * 
 * @param <T> the type parameter which extends the {@link AbstractPageModel} type
 *
 * @deprecated since version 6.3. Please use {@link de.hybris.platform.cmsfacades.pages.service.PageVariationResolver}
 *             in the cmsfacades extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.3")
public interface PageVariationResolver<T extends AbstractPageModel>
		extends de.hybris.platform.cmsfacades.pages.service.PageVariationResolver<T>
{
	// Intentionally left empty.
}
