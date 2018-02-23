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

export class AssetsService {

	static $inject: any = ['$injector'];

	// Constants
	private readonly TEST_ASSETS_KEY: string = 'testAssets';
	private readonly TEST_ASSETS_SRC: string = '/web/webroot';
	private readonly PROD_ASSETS_SRC: string = '/cmssmartedit';

	// Variables
	private $injector: angular.auto.IInjectorService;

	constructor(injector: angular.auto.IInjectorService) {
		this.$injector = injector;
	}

	public getAssetsRoot(): string {
		let useTestAssets = false;

		if (this.getInjector().has(this.TEST_ASSETS_KEY)) {
			const result = this.getInjector().get(this.TEST_ASSETS_KEY);
			useTestAssets = (typeof result === 'boolean') ? result as boolean : false;
		}

		return useTestAssets ? this.TEST_ASSETS_SRC : this.PROD_ASSETS_SRC;
	}

	getInjector(): angular.auto.IInjectorService {
		return this.$injector;
	}

}

export const AssetsServiceModule = angular

    /**
     * @ngdoc overview
     * @name assetsServiceModule
     * @description
     * # The assetsServiceModule
     *
     * The assetsServiceModule provides methods to handle assets such as images
     *
     */
	.module('assetsServiceModule', [])


    /**
     * @ngdoc object
     * @name cmsConstantsModule.service:assetsService
     *
     * @description
     * returns the assets resources root depending whether or not we are in test mode
     */
	.service('assetsService', AssetsService)
	.name; 
