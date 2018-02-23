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
package de.hybris.platform.customercouponfacades.strategies.impl;

import static java.util.Objects.isNull;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.hooks.NotificationChannelHook;
import de.hybris.platform.customercouponfacades.strategies.CustomerNotificationPreferenceCheckStrategy;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link CustomerNotificationPreferenceCheckStrategy}
 */
public class DefaultCustomerNotificationPreferenceCheckStrategy implements CustomerNotificationPreferenceCheckStrategy
{

	private UserService userService;
	private List<NotificationChannelHook> notificationChannelHooks;
	private static final Logger LOG = Logger.getLogger(DefaultCustomerNotificationPreferenceCheckStrategy.class.getName());
	@Override
	public Boolean checkCustomerNotificationPreference()
	{
		final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
		Boolean isAnySwitchOn = Boolean.FALSE;
		if (!isNull(currentCustomer) && CollectionUtils.isNotEmpty(notificationChannelHooks))
		{
			for (final NotificationChannelHook hook : notificationChannelHooks)
			{
				isAnySwitchOn |= hook.isChannelOn(currentCustomer);
			}
		}
		if (!isAnySwitchOn)
		{
			LOG.warn("You haven't chosen any channel in Notification Preference, so no notification would be sent.");
		}
		return isAnySwitchOn;
	}

	@Required
	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Required
	public List<NotificationChannelHook> getNotificationChannelHooks()
	{
		return notificationChannelHooks;
	}

	public void setNotificationChannelHooks(final List<NotificationChannelHook> notificationChannelHooks)
	{
		this.notificationChannelHooks = notificationChannelHooks;
	}


}
