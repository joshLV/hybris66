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
package de.hybris.platform.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubResult;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultSendToDataHubResult;
import de.hybris.platform.sap.sapcpiadapter.clients.SapCpiOrderClient;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrder;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiResponse;
import de.hybris.platform.sap.sapcpiadapter.service.SapCpiOAuthService;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;

import reactor.tuple.Tuple;
import rx.Observable;
import rx.Subscriber;
import rx.observables.BlockingObservable;

public class SapCpiOrderDefaultService implements SapCpiOrderService {

    private static final Logger LOG = Logger.getLogger(SapCpiOrderDefaultService.class);
    private static final String SCPI_ERROR = "error";
    private static final String SCPI_SUCCESS = "success";

    private SapCpiOrderClient sapCpiOrderClient;
    private SapCpiOAuthService sapOAuthService;

    @Override
    public Observable<SendToDataHubResult> sendOrder(final SapCpiOrder sapCpiOrder) {

        try {

            return getSapOAuthService()
                    .getToken()
                    .flatMap(accessToken -> getSapCpiOrderClient().sendOrder(new StringBuilder().append("Bearer ").append(accessToken).toString(), sapCpiOrder)
                            .flatMap(
                                    sapCpiResponse -> {

                                        if (sapCpiResponse.getStatus().contentEquals(SCPI_SUCCESS)) {

                                            LOG.info(sapCpiResponse.getMessage());
                                            return Observable.just(DefaultSendToDataHubResult.OKAY);

                                        } else if (sapCpiResponse.getStatus().contentEquals(SCPI_ERROR)) {

                                            LOG.error(String.format("Unable to send the order [%s] to SAP backend through SCPI! %s", sapCpiResponse.getId(), sapCpiResponse.getMessage()));
                                            return Observable.just(DefaultSendToDataHubResult.SENDING_FAILED);

                                        } else {

                                            LOG.error(String.format("Unable to send the order [%s] to SAP backend through SCPI!", sapCpiOrder.getOrderId()));
                                            return Observable.just(DefaultSendToDataHubResult.SENDING_FAILED);

                                        }

                                    }

                            ));


        } catch (final Exception ex) {

            LOG.error(String.format("Unable to send the order [%s] to SAP backend through SCPI! %s", sapCpiOrder.getOrderId(), ex.getMessage()));
            return Observable.just(DefaultSendToDataHubResult.SENDING_FAILED);

        }

    }


    protected SapCpiOAuthService getSapOAuthService() {
        return sapOAuthService;
    }

    @Required
    public void setSapOAuthService(final SapCpiOAuthService sapOAuthService) {
        this.sapOAuthService = sapOAuthService;
    }


    protected SapCpiOrderClient getSapCpiOrderClient() {
        return sapCpiOrderClient;
    }

    @Required
    public void setSapCpiOrderClient(final SapCpiOrderClient sapCpiOrderClient) {
        this.sapCpiOrderClient = sapCpiOrderClient;
    }

}
