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
package de.hybris.platform.customercouponservices.cronjob;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customercouponservices.constants.CustomercouponservicesConstants;
import de.hybris.platform.customercouponservices.daos.CouponNotificationDao;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.enums.CouponNotificationStatus;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.service.NotificationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.task.TaskExecutor;


/**
 * Abstract base class for sending BACK_IN_STOCK notification to customer
 */
public abstract class AbstractCouponNotificationJob extends AbstractJobPerformable<CronJobModel>
{
	private static final String COUPON_EXPIRE_NOTIFICATION_DAYS = "coupon.expire.notification.days";
	private static final String COUPON_EFFECTIVE_NOTIFICAITON_DAYS = "coupon.effective.notification.days";
	private static final Integer ZERO = 0;

	private CustomerCouponDao customerCouponDao;
	private CouponNotificationDao couponNotificationDao;
	private TaskExecutor taskExecutor;
	private NotificationService notificationService;

	@Override
	public PerformResult perform(final CronJobModel job)
	{
		final Integer configEffectiveDays = Config.getInt(COUPON_EFFECTIVE_NOTIFICAITON_DAYS, ZERO);
		final DateTime effectiveDay = new DateTime().plusDays(configEffectiveDays);
		final Integer configExpireDays = Config.getInt(COUPON_EXPIRE_NOTIFICATION_DAYS, ZERO);
		final DateTime expireDay = new DateTime().plusDays(configExpireDays);

		final List<CouponNotificationModel> couponNotifications = getCouponNotificationDao().findAllCouponNotification();

		couponNotifications.stream().forEach(couponNotification -> {
			final boolean isUnassignedCoupon = getCustomerCouponDao().countAssignedCouponForCustomer(
					couponNotification.getCustomerCoupon().getCouponId(), couponNotification.getCustomer()) < 1;

			if (new DateTime(couponNotification.getCustomerCoupon().getEndDate()).isBeforeNow() || isUnassignedCoupon)
			{
				modelService.remove(couponNotification);
				return;
			}
			if (couponNotification.getStatus().equals(CouponNotificationStatus.INIT)
					&& new DateTime(couponNotification.getCustomerCoupon().getStartDate()).isBefore(effectiveDay))
			{
				sendCouponNotificaiton(couponNotification, NotificationType.COUPON_EFFECTIVE);
			}
			if ((couponNotification.getStatus().equals(CouponNotificationStatus.INIT)
					|| couponNotification.getStatus().equals(CouponNotificationStatus.EFFECTIVESENT))
					&& new DateTime(couponNotification.getCustomerCoupon().getEndDate()).isBefore(expireDay))
			{
				sendCouponNotificaiton(couponNotification, NotificationType.COUPON_EXPIRE);
			}
		});

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * Send effective coupon notificaiton
	 *
	 * @param couponNotifications
	 */
	protected void sendCouponNotificaiton(final CouponNotificationModel couponNotification,
			final NotificationType notificationType)
	{
		final Map<String, ItemModel> data = new HashMap<>();
		data.put(CustomercouponservicesConstants.LANGUAGE, couponNotification.getLanguage());
		data.put(CustomercouponservicesConstants.COUPON_NOTIFICATION, couponNotification);
		final ItemModel notificationTypeItem = new ItemModel();
		notificationTypeItem.setProperty(CustomercouponservicesConstants.NOTIFICATION_TYPE, notificationType);
		data.put(CustomercouponservicesConstants.NOTIFICATION_TYPE, notificationTypeItem);

		taskExecutor.execute(createTask(data));

		couponNotification.setStatus(CouponNotificationStatus.EFFECTIVESENT);

		if (NotificationType.COUPON_EXPIRE.equals(notificationType))
		{
			couponNotification.setStatus(CouponNotificationStatus.EXPIRESENT);
		}

		modelService.save(couponNotification);
	}

	protected abstract CouponNotificationTask createTask(final Map<String, ItemModel> data);


	protected CustomerCouponDao getCustomerCouponDao()
	{
		return customerCouponDao;
	}

	@Required
	public void setCustomerCouponDao(final CustomerCouponDao customerCouponDao)
	{
		this.customerCouponDao = customerCouponDao;
	}

	@Required
	public void setTaskExecutor(final TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	protected TaskExecutor getTaskExecutor()
	{
		return taskExecutor;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	@Required
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	protected CouponNotificationDao getCouponNotificationDao()
	{
		return couponNotificationDao;
	}

	@Required
	public void setCouponNotificationDao(final CouponNotificationDao couponNotificationDao)
	{
		this.couponNotificationDao = couponNotificationDao;
	}

}
