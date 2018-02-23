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

/**
 * @ngdoc service
 * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface
 *
 * @description
 * Provides an abstract extensible shared data service. Used to store any data to be used either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
export interface ISharedDataService {
    /** 
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#get
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Get the data for the given key.
     *
     * @param {String} key The key of the data to fetch
     */
	get(key: string): any;

    /** 
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#set
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Set data for the given key.
     *
     * @param {String} key The key of the data to set
     * @param {String} value The value of the data to set
     */
	set(key: string, value: string): any;

    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#update
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Convenience method to retrieve and modify on the fly the content stored under a given key
     *
     * @param {String} key The key of the data to store
     * @param {Function} modifyingCallback callback fed with the value stored under the given key. The callback must return the new value of the object to update.
     */
	update(key: string, modifyingCallback: (oldValue: any) => any): any;
}

// Needed only for backwards compatibility. 
class SharedDataServiceInterface implements ISharedDataService {
	get(key: string): any {
		throw new Error("Method not implemented.");
	}
	set(key: string, value: string): any {
		throw new Error("Method not implemented.");
	}
	update(key: string, modifyingCallback: (oldValue: any) => any): any {
		throw new Error("Method not implemented.");
	}
}

export const SharedDataServiceInterfaceModule = angular
	.module('sharedDataServiceInterfaceModule', [])
	.factory('SharedDataServiceInterface', SharedDataServiceInterface)
	.name;
