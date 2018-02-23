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
package de.hybris.platform.sap.sapordermgmtcfgfacades.impl;

import de.hybris.platform.sap.productconfig.facades.ConfigurationOrderIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.sapordermgmtservices.order.OrderService;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;


/**
 * Default implementation of {@link ConfigurationOrderIntegrationFacade} for synchronous order management
 */
public class DefaultConfigurationOrderIntegrationFacade implements ConfigurationOrderIntegrationFacade
{


	private OrderService orderService;
	private BaseStoreService baseStoreService;

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Resource(name = "sapProductConfigDefaultOrderIntegrationFacade")
	private ConfigurationOrderIntegrationFacade sapProductConfigCartIntegrationFacade;

	/**
	 * @return the sapProductConfigCartIntegrationFacade
	 */
	public ConfigurationOrderIntegrationFacade getSapProductConfigCartIntegrationFacade()
	{
		return sapProductConfigCartIntegrationFacade;
	}

	/**
	 * @param sapProductConfigCartIntegrationFacade
	 *           the sapProductConfigCartIntegrationFacade to set
	 */
	public void setSapProductConfigCartIntegrationFacade(
			final ConfigurationOrderIntegrationFacade sapProductConfigCartIntegrationFacade)
	{
		this.sapProductConfigCartIntegrationFacade = sapProductConfigCartIntegrationFacade;
	}

	/**
	 * @return the orderService
	 */
	public OrderService getOrderService()
	{
		return orderService;
	}

	@Override
	public ConfigurationOverviewData getConfiguration(final String code, final int entryNumber)
	{
		if (isSapOrderMgmtEnabled())
		{
			final ConfigModel configModel = orderService.getConfiguration(code, String.valueOf(entryNumber));
			final ConfigurationOverviewData result = new ConfigurationOverviewData();
			result.setId(configModel.getId());
			result.setProductCode(configModel.getRootInstance().getName());
			return result;
		}
		else
		{
			return sapProductConfigCartIntegrationFacade.getConfiguration(code, entryNumber);
		}
	}


	/**
	 * @param orderService
	 */
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;

	}

	/**
	 * Check if synchronous order management SOM is active
	 *
	 * @return true is SOM is active
	 */
	protected boolean isSapOrderMgmtEnabled()
	{
		return getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null
				&& getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().isSapordermgmt_enabled();

	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * Method to check if order with code is re-orderable
	 *
	 * @param orderCode order code
	 * @return boolean is reorderable
	 */
	public boolean isReorderable(final String orderCode)
	{
		if (isSapOrderMgmtEnabled())
		{
			return false;
		}
		else
		{
			return sapProductConfigCartIntegrationFacade.isReorderable(orderCode);
		}
	}

}
