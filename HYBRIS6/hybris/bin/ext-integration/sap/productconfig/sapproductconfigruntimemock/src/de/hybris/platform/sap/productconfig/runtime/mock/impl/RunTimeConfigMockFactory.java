/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import de.hybris.platform.sap.productconfig.runtime.mock.ConfigMock;
import de.hybris.platform.sap.productconfig.runtime.mock.ConfigMockFactory;


public class RunTimeConfigMockFactory implements ConfigMockFactory
{

	@Override
	public ConfigMock createConfigMockForProductCode(final String productCode)
	{
		ConfigMock mock = null;

		switch (productCode)
		{
			case "CPQ_HOME_THEATER":
				mock = new CPQHomeTheaterPocConfigMockImpl();
				break;

			case "CPQ_LAPTOP":
				mock = new CPQLaptopPocConfigMockImpl();
				break;

			default:
				mock = new YSapSimplePocConfigMockImpl();
				break;
		}

		return mock;
	}
}
