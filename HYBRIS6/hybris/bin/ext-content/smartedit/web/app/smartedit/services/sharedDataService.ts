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
/**
 * @ngdoc overview
 * @name sharedDataServiceModule
 * @description
 * # The sharedDataServiceModule
 *
 * The Shared Data Service Module provides a service used to store and retrieve data using a key. The data can be shared
 * between the SmartEdit application and SmartEdit container.
 *
 */

import {ISharedDataService} from '../../common/index';

import * as angular from 'angular';

/**
 * @ngdoc service
 * @name sharedDataServiceModule.sharedDataService
 *
 * @description
 * The Shared Data Service is used to store data that is to be shared between the SmartEdit application and the
 * SmartEdit container. It uses the {@link gatewayProxyModule.gatewayProxy gatewayProxy} service to share data between
 * the SmartEdit application and the container. It uses the gateway ID "sharedData".
 *
 * The Shared Data Service extends the {@link sharedDataServiceInterfaceModule.SharedDataServiceInterface
 * SharedDataServiceInterface}.
 *
 */
/* tslint:disable:no-empty */
/* @ngInject */
export class SharedDataService implements ISharedDataService {

	// Constants
	readonly GATEWAY_ID = 'sharedData';

	// Variables
	gatewayId: string;

	constructor(gatewayProxy: any) {
		this.gatewayId = this.GATEWAY_ID;
		gatewayProxy.initForService(this);
	}

	get(key: string) {}

	set(key: string, value: string) {}

	update(key: string, modifyingCallback: (oldValue: any) => any) {}
}

export const SharedDataServiceModule = angular
	.module('sharedDataServiceModule', ['gatewayProxyModule'])
	.service('sharedDataService', SharedDataService)
	.name;
