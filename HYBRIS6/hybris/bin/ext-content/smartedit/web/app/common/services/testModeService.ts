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
/* @ngInject */
export class TestModeService {

	// Constants
	private readonly TEST_KEY: string = 'e2eMode';

	constructor(private $injector: angular.auto.IInjectorService) {
	}

	public isE2EMode(): boolean {
		return this.$injector.has(this.TEST_KEY) && this.$injector.get(this.TEST_KEY);
	}

}
