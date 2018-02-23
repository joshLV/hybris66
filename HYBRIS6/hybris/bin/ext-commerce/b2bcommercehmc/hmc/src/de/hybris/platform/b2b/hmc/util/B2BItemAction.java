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
package de.hybris.platform.b2b.hmc.util;

import de.hybris.platform.b2b.dao.B2BOrderDao;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BReportingService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.hmc.util.action.ActionEvent;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.hmc.util.action.ItemAction;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;


public class B2BItemAction extends ItemAction
{

	private final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService = (B2BUnitService<B2BUnitModel, B2BCustomerModel>) Registry
			.getApplicationContext().getBean("b2bUnitService");
	private final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
	private final UserService userService = (UserService) Registry.getApplicationContext().getBean("userService");
	private final B2BOrderDao defaultB2BOrderDao = (B2BOrderDao) Registry.getApplicationContext().getBean("defaultB2BOrderDao");
	private final CommonI18NService commonI18NService = (CommonI18NService) Registry.getApplicationContext().getBean(
			"commonI18NService");
	private final FormatFactory formatFactory = (FormatFactory) Registry.getApplicationContext().getBean("formatFactory");
	private final B2BReportingService b2bReportingService = (B2BReportingService) Registry.getApplicationContext().getBean(
			"b2bReportingService");

	/**
	 * @return the defaultB2BOrderDao
	 */
	protected B2BOrderDao getDefaultB2BOrderDao()
	{
		return defaultB2BOrderDao;
	}


	/**
	 * @return the commonI18NService
	 */
	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}


	/**
	 * @return the formatFactory
	 */
	protected FormatFactory getFormatFactory()
	{
		return formatFactory;
	}


	/**
	 * @return the b2bUnitService
	 */
	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}


	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @return the b2bReportingService
	 */
	protected B2BReportingService getB2bReportingService()
	{
		return b2bReportingService;
	}


	@Override
	public ActionResult perform(final ActionEvent arg0) throws JaloBusinessException
	{
		return null;
	}





}
