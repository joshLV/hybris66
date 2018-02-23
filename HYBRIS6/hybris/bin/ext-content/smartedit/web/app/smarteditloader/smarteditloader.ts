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

import 'smarteditloader/smarteditloader_bundle.js';

import * as angular from 'angular';

angular.module('smarteditloader', [
	'configModule',
	'templateCacheDecoratorModule',
	'loadConfigModule',
	'bootstrapServiceModule',
	'coretemplates',
	'translationServiceModule',
	'httpAuthInterceptorModule',
	'systemAlertsModule',
	'httpErrorInterceptorServiceModule',
	'unauthorizedErrorInterceptorModule',
	'resourceNotFoundErrorInterceptorModule',
	'retryInterceptorModule'
])
	.config(($logProvider: angular.ILogProvider) => {
		'ngInject';
		$logProvider.debugEnabled(false);
	})
	.run((loadConfigManagerService: any, bootstrapService: any, httpErrorInterceptorService: any, unauthorizedErrorInterceptor: any, resourceNotFoundErrorInterceptor: any, retryInterceptor: any) => {
		'ngInject';
		httpErrorInterceptorService.addInterceptor(retryInterceptor);
		httpErrorInterceptorService.addInterceptor(unauthorizedErrorInterceptor);
		httpErrorInterceptorService.addInterceptor(resourceNotFoundErrorInterceptor);

		loadConfigManagerService.loadAsObject().then((configurations: any) => {
			bootstrapService.bootstrapContainerModules(configurations);
		});
	});
