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
package de.hybris.platform.customercouponservices.daos;

import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


public interface CouponNotificationDao extends GenericDao<CouponNotificationModel>
{
	/**
	 * Find all coupon need send notification.
	 * 
	 * @return list of CouponNotificationModel
	 * 
	 */
	default List<CouponNotificationModel> findAllCouponNotification()
	{
		return find();
	}

	/**
	 * Find coupon notification by coupon code
	 * 
	 * @param couponCode
	 *           coupon code
	 * @return list of CouponNotificationModel
	 * 
	 */
	List<CouponNotificationModel> findCouponNotificationByCouponCode(String couponCode);


}
