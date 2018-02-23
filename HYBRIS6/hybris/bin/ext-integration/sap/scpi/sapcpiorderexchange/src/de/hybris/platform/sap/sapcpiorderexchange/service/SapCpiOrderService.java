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
package de.hybris.platform.sap.sapcpiorderexchange.service;

import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubResult;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrder;
import rx.Observable;


public interface SapCpiOrderService {

    Observable<SendToDataHubResult> sendOrder(SapCpiOrder sapCpiOrder);
}
