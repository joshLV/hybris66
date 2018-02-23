/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.cissapdigitalpayment.strategies.impl;

import de.hybris.platform.cissapdigitalpayment.model.SAPDigitalPaymentConfigurationModel;
import de.hybris.platform.cissapdigitalpayment.strategies.SapDigitalPaymentConfigurationStrategy;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;



/**
 * Default implemenation statergy for getting the SAP Digital payment Configuration
 */
public class DefaultSapDigitalPaymentConfigurationStrategy implements SapDigitalPaymentConfigurationStrategy
{

	private GenericDao<SAPDigitalPaymentConfigurationModel> sapDigitalPaymentConfigurationDao;

	@Override
	public SAPDigitalPaymentConfigurationModel getSapDigitalPaymentConfiguration()
	{

		final List<SAPDigitalPaymentConfigurationModel> sapDpConfigList = getSapDigitalPaymentConfigurationDao().find();
		if (null != sapDpConfigList)
		{
			return sapDpConfigList.get(0);
		}
		return null;

	}

	/**
	 * @return the sapDigitalPaymentConfigurationDao
	 */
	public GenericDao<SAPDigitalPaymentConfigurationModel> getSapDigitalPaymentConfigurationDao()
	{
		return sapDigitalPaymentConfigurationDao;
	}

	/**
	 * @param sapDigitalPaymentConfigurationDao
	 *           the sapDigitalPaymentConfigurationDao to set
	 */
	public void setSapDigitalPaymentConfigurationDao(
			final GenericDao<SAPDigitalPaymentConfigurationModel> sapDigitalPaymentConfigurationDao)
	{
		this.sapDigitalPaymentConfigurationDao = sapDigitalPaymentConfigurationDao;
	}



}
