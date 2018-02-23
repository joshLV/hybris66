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
package de.hybris.platform.chineselogisticfacades.delivery.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.chineselogisticfacades.delivery.DeliveryTimeSlotFacade;
import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;
import de.hybris.platform.chineselogisticfacades.delivery.populator.DeliveryTimeSlotPopulator;
import de.hybris.platform.chineselogisticservices.delivery.DeliveryTimeSlotService;
import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.core.model.order.CartModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * 
 * Chinese specific implementation of {@link DeliveryTimeSlotFacade}
 *
 */
public class DefaultChineseDeliveryTimeSlotFacade extends DefaultAcceleratorCheckoutFacade implements DeliveryTimeSlotFacade
{
	private DeliveryTimeSlotPopulator deliveryTimeSlotPopulator;
	private DeliveryTimeSlotService deliveryTimeSlotService;

	@Override
	public List<DeliveryTimeSlotData> getAllDeliveryTimeSlots()
	{
		List<DeliveryTimeSlotModel> deliveryTimeSlotModels = deliveryTimeSlotService.getAllDeliveryTimeSlots();
		if (deliveryTimeSlotModels != null && !deliveryTimeSlotModels.isEmpty())
		{
			List<DeliveryTimeSlotData> deliveryTimeSlots = new ArrayList<>();
			for (DeliveryTimeSlotModel deliveryTimeSlotModel : deliveryTimeSlotModels)
			{
				DeliveryTimeSlotData deliveryTimeSlotData = new DeliveryTimeSlotData();
				deliveryTimeSlotPopulator.populate(deliveryTimeSlotModel, deliveryTimeSlotData);
				deliveryTimeSlots.add(deliveryTimeSlotData);
			}
			return deliveryTimeSlots;
		}
		return Collections.emptyList();
	}

	@Override
	public void setDeliveryTimeSlot(String deliveryTimeSlot)
	{
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			deliveryTimeSlotService.setDeliveryTimeSlot(cartModel, deliveryTimeSlot);
		}
	}

	protected DeliveryTimeSlotPopulator getDeliveryTimeSlotPopulator()
	{
		return deliveryTimeSlotPopulator;
	}

	@Required
	public void setDeliveryTimeSlotPopulator(DeliveryTimeSlotPopulator deliveryTimeSlotPopulator)
	{
		this.deliveryTimeSlotPopulator = deliveryTimeSlotPopulator;
	}

	protected DeliveryTimeSlotService getDeliveryTimeSlotService()
	{
		return deliveryTimeSlotService;
	}

	@Required
	public void setDeliveryTimeSlotService(DeliveryTimeSlotService deliveryTimeSlotService)
	{
		this.deliveryTimeSlotService = deliveryTimeSlotService;
	}

}
