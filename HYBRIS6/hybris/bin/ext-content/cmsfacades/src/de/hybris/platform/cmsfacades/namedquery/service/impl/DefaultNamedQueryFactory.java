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
package de.hybris.platform.cmsfacades.namedquery.service.impl;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.namedquery.service.NamedQueryFactory;


/**
 * Default implementation NamedQueryFactory interface, which holds a named query definition map. For each NamedQuery
 * search domain (e.g. Media), there should be a separate Spring bean with the named queries definitions.
 *
 * @deprecated since 6.4, please use {@link de.hybris.platform.cms2.namedquery.service.impl.DefaultNamedQueryFactory}
 *             instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public class DefaultNamedQueryFactory extends de.hybris.platform.cms2.namedquery.service.impl.DefaultNamedQueryFactory
		implements NamedQueryFactory
{
	// Intentionally left empty.
}
