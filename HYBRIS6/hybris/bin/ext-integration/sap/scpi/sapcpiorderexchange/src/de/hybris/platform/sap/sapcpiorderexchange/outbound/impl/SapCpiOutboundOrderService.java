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
package de.hybris.platform.sap.sapcpiorderexchange.outbound.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubResult;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderConversionService;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderService;
import de.hybris.platform.sap.saporderexchangeoms.outbound.impl.SapOmsSendOrderToDataHubHelper;
import org.springframework.beans.factory.annotation.Required;

public class SapCpiOutboundOrderService extends SapOmsSendOrderToDataHubHelper {

    private SapCpiOrderService sapCpiOrderService;
    private SapCpiOrderConversionService sapCpiOrderConversionService;

    @Override
    public SendToDataHubResult createAndSendRawItem(OrderModel orderModel) {
       return getSapCpiOrderService().sendOrder(getSapCpiOrderConversionService().convertHybrisOrderToSapCpiOrder(orderModel)).toBlocking().first();
    }

    public SapCpiOrderService getSapCpiOrderService() {
        return sapCpiOrderService;
    }

    @Required
    public void setSapCpiOrderService(SapCpiOrderService sapCpiOrderService) {
        this.sapCpiOrderService = sapCpiOrderService;
    }

    public SapCpiOrderConversionService getSapCpiOrderConversionService() {
        return sapCpiOrderConversionService;
    }

    @Required
    public void setSapCpiOrderConversionService(SapCpiOrderConversionService sapCpiOrderConversionService) {
        this.sapCpiOrderConversionService = sapCpiOrderConversionService;
    }
}
