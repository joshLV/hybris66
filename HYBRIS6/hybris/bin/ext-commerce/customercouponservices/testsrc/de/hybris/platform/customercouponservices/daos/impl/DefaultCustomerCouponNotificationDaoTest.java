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
package de.hybris.platform.customercouponservices.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.customercouponservices.daos.CouponNotificationDao;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for {@link DefaultCustomerCouponNotificationDao}
 */
@IntegrationTest
public class DefaultCustomerCouponNotificationDaoTest extends ServicelayerTransactionalTest
{
	private static final String COUPON_ID = "customerCouponCode3";

	@Resource(name = "couponNotificationDao")
	private CouponNotificationDao couponNotificationDao;


	@Before
	public void prepare() throws ImpExException
	{
		importCsv("/customercouponservices/test/DefaultCustomerCouponServiceTest.impex", "UTF-8");
	}

	@Test
	public void testFindCouponNotificationByCouponCode()
	{
		final List<CouponNotificationModel> result = couponNotificationDao.findCouponNotificationByCouponCode(COUPON_ID);

		Assert.assertEquals(1, result.size());
	}

}
