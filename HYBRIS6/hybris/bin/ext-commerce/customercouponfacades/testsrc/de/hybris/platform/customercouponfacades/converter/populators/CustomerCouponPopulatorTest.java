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
package de.hybris.platform.customercouponfacades.converter.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * Unit test for {@link CustomerCouponPopulator}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ Config.class, Converters.class })
@UnitTest
public class CustomerCouponPopulatorTest
{

	private static final String COUPON_EXPIRE_NOTIFICATIONS_DAYS = "coupon.expire.notification.days";
	private static final String COUPON_NAME = "testname";
	private static final long THRESHOLD_DAYS = 1l;
	private static final String COUPON_ID = "TEST";
	private static final String DESCRIPTION = "description";
	private static final String STATUS = "Effective";
	private static final String ROOT_CATEGORY = "coupon.rootcategory";
	private static final String ROOT_CATEGORY_CONFIG_DATA = "1";

	private CustomerCouponPopulator populator;

	@Mock
	private CustomerCouponModel source;

	@Mock
	private CustomerCouponService customerCouponService;

	private Locale locale;
	private CustomerCouponData target;
	private Date startDate;
	private Date endDate;


	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(Config.class);
		PowerMockito.mockStatic(Converters.class);

		populator = new CustomerCouponPopulator();

		target = new CustomerCouponData();
		locale = new Locale("en");
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 2);
		startDate = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 10);
		endDate = calendar.getTime();

		PowerMockito.when(Config.getLong(COUPON_EXPIRE_NOTIFICATIONS_DAYS, THRESHOLD_DAYS)).thenReturn(THRESHOLD_DAYS);
		PowerMockito.when(Config.getString(ROOT_CATEGORY, StringUtils.EMPTY)).thenReturn(ROOT_CATEGORY_CONFIG_DATA);
		Mockito.when(customerCouponService.getCouponNotificationStatus(Mockito.anyString())).thenReturn(true);
		final List<PromotionSourceRuleModel> promotionSourceRule = new ArrayList<>();

		Mockito.when(customerCouponService.getPromotionSourceRuleForCouponCode(Mockito.anyString()))
				.thenReturn(promotionSourceRule);

		populator.setCustomerCouponService(customerCouponService);
		Mockito.when(source.getCouponId()).thenReturn(COUPON_ID);
		Mockito.when(source.getStartDate()).thenReturn(startDate);
		Mockito.when(source.getEndDate()).thenReturn(endDate);
		Mockito.when(source.getName()).thenReturn(COUPON_NAME);
		Mockito.when(source.getDescription()).thenReturn(DESCRIPTION);
		Mockito.when(source.getActive()).thenReturn(true);
	}

	@Test
	public void testPopulator()
	{
		populator.populate(source, target);

		Assert.assertEquals(COUPON_ID, target.getCouponId());
		Assert.assertEquals(COUPON_ID, target.getCouponCode());
		Assert.assertEquals(STATUS, target.getStatus());
		Assert.assertEquals(COUPON_NAME, target.getName());
		Assert.assertEquals(DESCRIPTION, target.getDescription());
		Assert.assertEquals(true, target.isActive());
		Assert.assertEquals(startDate, target.getStartDate());
		Assert.assertEquals(endDate, target.getEndDate());
		Assert.assertEquals(false, target.isBindingAnyProduct());
	}
}
