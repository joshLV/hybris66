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
import {SharedDataService, SmarteditContainerServicesModule} from './services';

import * as angular from 'angular';
/* @ngInject */
class SmarteditDefaultController {

	// Variables
	iFrameManager: any;
	experienceService: any;
	sharedDataService: SharedDataService;

	constructor(iFrameManager: any, experienceService: any, sharedDataService: SharedDataService) {
		this.iFrameManager = iFrameManager;
		this.experienceService = experienceService;
		this.sharedDataService = sharedDataService;

		this.setUpIFrame();
	}

	private setUpIFrame() {
		this.iFrameManager.applyDefault();
		this.iFrameManager.initializeCatalogPreview();
		(angular.element(document.querySelector('body')) as JQuery).addClass('is-storefront');
	}
}

export const smarteditContainerModule = angular
	.module('smarteditcontainer', [
		SmarteditContainerServicesModule,
		'componentHandlerServiceModule',
		'configModule',
		'landingPageModule',
		'templateCacheDecoratorModule',
		'ngRoute',
		'ngResource',
		'ui.bootstrap',
		'coretemplates',
		'loadConfigModule',
		'iFrameManagerModule',
		'alertsBoxModule',
		'httpAuthInterceptorModule',
		'experienceInterceptorModule',
		'bootstrapServiceModule',
		'toolbarModule',
		'leftToolbarModule',
		'modalServiceModule',
		'previewTicketInterceptorModule',
		'catalogServiceModule',
		'catalogDetailsModule',
		'experienceSelectorButtonModule',
		'experienceSelectorModule',
		'inflectionPointSelectorModule',
		'paginationFilterModule',
		'resourceLocationsModule',
		'experienceServiceModule',
		'eventServiceModule',
		'urlServiceModule',
		'perspectiveServiceModule',
		'perspectiveSelectorModule',
		'authorizationModule',
		'hasOperationPermissionModule',
		'l10nModule',
		'treeModule',
		'yInfiniteScrollingModule',
		'ySelectModule',
		'yHelpModule',
		'crossFrameEventServiceModule',
		'renderServiceModule',
		'systemAlertsModule',
		'yCollapsibleContainerModule',
		'seDropdownModule',
		'permissionServiceModule',
		'yNotificationPanelModule',
		'catalogAwareRouteResolverModule',
		'catalogVersionPermissionModule',
		'httpErrorInterceptorServiceModule',
		'unauthorizedErrorInterceptorModule',
		'resourceNotFoundErrorInterceptorModule',
		'nonvalidationErrorInterceptorModule',
		'previewErrorInterceptorModule',
		'retryInterceptorModule',
		'seConstantsModule',
		'pageSensitiveDirectiveModule'
	])
	.config((LANDING_PAGE_PATH: string, STOREFRONT_PATH: string, STOREFRONT_PATH_WITH_PAGE_ID: string, $routeProvider: angular.route.IRouteProvider, $logProvider: angular.ILogProvider, catalogAwareRouteResolverFunctions: any) => {
		'ngInject';
		$routeProvider.when(LANDING_PAGE_PATH, {
			template: '<landing-page></landing-page>'
		})
			.when(STOREFRONT_PATH, {
				templateUrl: 'mainview.html',
				controller: 'defaultController',
				resolve: {
					setExperience: catalogAwareRouteResolverFunctions.setExperience
				}
			})
			.when(STOREFRONT_PATH_WITH_PAGE_ID, {
				templateUrl: 'mainview.html',
				controller: 'defaultController',
				resolve: {
					setExperience: catalogAwareRouteResolverFunctions.setExperience
				}
			})
			.otherwise({
				redirectTo: LANDING_PAGE_PATH
			});

		$logProvider.debugEnabled(false);
	})
	.service('smartEditBootstrapGateway', (gatewayFactory: any) => {
		'ngInject';
		return gatewayFactory.createGateway('smartEditBootstrap');
	})
	.run(($rootScope: angular.IRootScopeService, $log: angular.ILogService, $q: angular.IQService, DEFAULT_RULE_NAME: string, EVENTS: any, smartEditBootstrapGateway: any, toolbarServiceFactory: any, perspectiveService: any, gatewayFactory: any, loadConfigManagerService: any, bootstrapService: any, iFrameManager: any, restServiceFactory: any, sharedDataService: SharedDataService, urlService: any, featureService: any, storageService: any, renderService: any, closeOpenModalsOnBrowserBack: any, authorizationService: any, permissionService: any, httpErrorInterceptorService: any, unauthorizedErrorInterceptor: any, resourceNotFoundErrorInterceptor: any, nonValidationErrorInterceptor: any, previewErrorInterceptor: any, retryInterceptor: any, componentHandlerService: any) => {
		'ngInject';
		gatewayFactory.initListener();
		httpErrorInterceptorService.addInterceptor(retryInterceptor);
		httpErrorInterceptorService.addInterceptor(unauthorizedErrorInterceptor);
		httpErrorInterceptorService.addInterceptor(resourceNotFoundErrorInterceptor);
		httpErrorInterceptorService.addInterceptor(nonValidationErrorInterceptor);
		httpErrorInterceptorService.addInterceptor(previewErrorInterceptor);

		loadConfigManagerService.loadAsObject().then((configurations: any) => {
			sharedDataService.set('defaultToolingLanguage', configurations.defaultToolingLanguage);
		});

		const smartEditTitleToolbarService = toolbarServiceFactory.getToolbarService("smartEditTitleToolbar");

		smartEditTitleToolbarService.addItems([{
			key: 'topToolbar.leftToolbarTemplate',
			type: 'TEMPLATE',
			include: 'leftToolbarWrapperTemplate.html',
			priority: 1,
			section: 'left'
		}, {
			key: 'topToolbar.logoTemplate',
			type: 'TEMPLATE',
			include: 'logoTemplate.html',
			priority: 2,
			section: 'left'
		}, {
			key: 'topToolbar.deviceSupportTemplate',
			type: 'TEMPLATE',
			include: 'deviceSupportTemplate.html',
			priority: 1,
			section: 'right'
		}, {
			type: 'TEMPLATE',
			key: 'topToolbar.experienceSelectorTemplate',
			className: 'ySEPreviewSelector',
			include: 'experienceSelectorWrapperTemplate.html',
			priority: 1, // first in the middle
			section: 'middle'
		}]);

		const experienceSelectorToolbarService = toolbarServiceFactory.getToolbarService("experienceSelectorToolbar");

		experienceSelectorToolbarService.addItems([{
			key: "bottomToolbar.perspectiveSelectorTemplate",
			type: 'TEMPLATE',
			section: 'right',
			priority: 1,
			include: 'perspectiveSelectorWrapperTemplate.html'
		}]);

		function offSetStorefront() {
			// Set the storefront offset
			componentHandlerService._getTargetIframeWrapper().css('padding-top', componentHandlerService.getFromSelector('.ySmartEditToolbars').height() + 'px');
		}

		smartEditBootstrapGateway.subscribe("reloadFormerPreviewContext", function() {
			offSetStorefront();
			const deferred = $q.defer();
			iFrameManager.initializeCatalogPreview();
			deferred.resolve();
			return deferred.promise;
		});
		smartEditBootstrapGateway.subscribe("loading", (eventId: string, data: any) => {
			const deferred = $q.defer();

			iFrameManager.setCurrentLocation(data.location);
			iFrameManager.showWaitModal();

			const smartEditBootstrapped = getBootstrapNamespace();
			delete smartEditBootstrapped[data.location];

			perspectiveService.clearActivePerspective();

			return deferred.promise;
		});
		smartEditBootstrapGateway.subscribe("bootstrapSmartEdit", (eventId: string, data: any) => {
			offSetStorefront();
			const deferred = $q.defer();
			const smartEditBootstrapped = getBootstrapNamespace();

			if (!smartEditBootstrapped[data.location]) {
				smartEditBootstrapped[data.location] = true;
				loadConfigManagerService.loadAsObject().then((configurations: any) => {
					bootstrapService.bootstrapSEApp(configurations);
					deferred.resolve();
				});
			} else {
				deferred.resolve();
			}
			return deferred.promise;
		});

		smartEditBootstrapGateway.subscribe("smartEditReady", function() {
			const deferred = $q.defer();
			deferred.resolve();

			iFrameManager.hideWaitModal();
			return deferred.promise;
		});

		$rootScope.$on('$routeChangeSuccess', function() {
			closeOpenModalsOnBrowserBack();
		});

		gatewayFactory.createGateway('accessTokens').subscribe("get", function() {
			return $q.when(storageService.getAuthTokens());
		});

		permissionService.registerDefaultRule({
			names: [DEFAULT_RULE_NAME],
			verify: (permissionNameObjs: any) => {
				const permisssionNames = permissionNameObjs.map((permissionName: any) => {
					return permissionName.name;
				});
				return authorizationService.hasGlobalPermissions(permisssionNames);
			}
		});

		// storefront actually loads twice all the JS files, including webApplicationInjector.js, smartEdit must be protected against receiving twice a smartEditBootstrap event
		function getBootstrapNamespace(): any {
			const smarteditWindow = window as any;
			if (smarteditWindow.smartEditBootstrapped) {
				smarteditWindow.smartEditBootstrapped = {};
			}
			return smarteditWindow.smartEditBootstrapped;
		}
	})
	.controller('defaultController', SmarteditDefaultController)
	.name;
