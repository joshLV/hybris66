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

import * as angular from 'angular';

import {ISharedDataService} from '../../common/index';

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
/* @ngInject */
export class SharedDataService implements ISharedDataService {

	public gatewayId: string = 'sharedData';

	// Variables
	private $q: angular.IQService;
	private storage: {[id: string]: any} = {};

	constructor($q: angular.IQService, gatewayProxy: any) {
		this.$q = $q;
		gatewayProxy.initForService(this);
	}

    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#get
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Retrieve the data for the given key.
     *
     * @param {String} key The key of the data to retrieve
     */
	get(name: string) {
		return this.storage[name];
	}

    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#set
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Store data for the given key.
     *
     * @param {String} key The key of the data to store
     * @param {String} value The value of the data to store
     */
	set(name: string, value: any): void {
		this.storage[name] = value;
	}

	update(name: string, modifyingCallback: (oldValue: any) => any): angular.IPromise<void> {
		return this.get(name).then((oldValue: any) => {
			return this.$q.when(modifyingCallback(oldValue)).then((newValue: any) => {
				return this.set(name, newValue);
			});
		});
	}
}

/**
 * @ngdoc overview
 * @name pageToolMenuModule
 * @description
 * # The sharedDataServiceModule
 *
 * The Shared Data Service Module provides a service used to store and retrieve data using a key. The data can be shared
 * between the SmartEdit application and SmartEdit container.
 *
 */
export const SharedDataServiceModule = angular
	.module('sharedDataServiceModule', ['gatewayProxyModule'])
	.service('sharedDataService', SharedDataService)
	.name;
