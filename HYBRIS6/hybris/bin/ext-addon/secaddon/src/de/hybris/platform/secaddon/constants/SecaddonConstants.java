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
package de.hybris.platform.secaddon.constants;

/**
 * Global class for all Secaddon constants. You can add global constants for your extension into this class.
 */
public final class SecaddonConstants extends GeneratedSecaddonConstants
{
	public static final String EXTENSIONNAME = "secaddon";

	public static final String CURRENT_LANGUAGE = "CurrentLanguage";
	public static final String HYBRIS_COUNT = "Hybris-Count";
	public static final String VISIBILITY_PUBLIC = "PUBLIC";
	public static final String MAPPED_CUSTOMER_ID = "mappedCustomerId";
	public static final String MIXIN_QUERY_BACKOFFICE_PROPERTY_KEY = "mixin.query";
	public static final String MIXIN_QUERY_KEY = "secaddon.mixin.query";
	public static final String MIXIN_QUERY_DEFAULT_VALUE = "mixins.hybrisCommerce.hybrisCustomerId";

	private SecaddonConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
