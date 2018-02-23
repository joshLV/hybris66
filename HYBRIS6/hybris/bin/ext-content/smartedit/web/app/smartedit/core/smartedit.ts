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

import {SmarteditCommonsModule} from '../../common';
import {SmarteditServicesModule} from '../services';

export const smarteditModule: string = angular.module('smartedit',
	[
		SmarteditCommonsModule,
		SmarteditServicesModule,
		'yjqueryModule',
		'configModule',
		'templateCacheDecoratorModule',
		'sakExecutorDecorator',
		'restServiceFactoryModule',
		'ui.bootstrap',
		'ngResource',
		'decoratorServiceModule',
		'smartEditContractChangeListenerModule',
		'alertsBoxModule',
		'ui.select',
		'httpAuthInterceptorModule',
		'httpErrorInterceptorServiceModule',
		'unauthorizedErrorInterceptorModule',
		'retryInterceptorModule',
		'resourceNotFoundErrorInterceptorModule',
		'experienceInterceptorModule',
		'gatewayFactoryModule',
		'renderServiceModule',
		'iframeClickDetectionServiceModule',
		'sanitizeHtmlInputModule',
		'perspectiveServiceModule',
		'featureServiceModule',
		'resizeComponentServiceModule',
		'languageServiceModule',
		'notificationServiceModule',
		'slotContextualMenuDecoratorModule',
		'contextualMenuDecoratorModule',
		'crossFrameEventServiceModule',
		'pageSensitiveDirectiveModule',
		'seNamespaceModule',
		'experienceServiceModule'
	])
	.config(($logProvider: angular.ILogProvider) => {
		'ngInject';
		$logProvider.debugEnabled(false);
	})
	.directive('html', () => {
		return {
			restrict: "E",
			replace: false,
			transclude: false,
			priority: 1000,
			link: ($scope: any, element: any) => {
				element.addClass('smartedit-html-container');
			}
		};
	})
	.controller('SmartEditController', angular.noop)
	.run((domain: string, systemEventService: any, EVENTS: any, ID_ATTRIBUTE: string, OVERLAY_RERENDERED_EVENT: string, smartEditContractChangeListener: any, crossFrameEventService: any, perspectiveService: any, languageService: any, restServiceFactory: any, gatewayFactory: any, renderService: any, decoratorService: any, featureService: any, resizeComponentService: any, seNamespace: any, experienceService: any, httpErrorInterceptorService: any, retryInterceptor: any, unauthorizedErrorInterceptor: any, resourceNotFoundErrorInterceptor: any, lodash: any) => {
		'ngInject';
		gatewayFactory.initListener();

		httpErrorInterceptorService.addInterceptor(retryInterceptor);
		httpErrorInterceptorService.addInterceptor(unauthorizedErrorInterceptor);
		httpErrorInterceptorService.addInterceptor(resourceNotFoundErrorInterceptor);

		smartEditContractChangeListener.onComponentsAdded((components: HTMLElement[]) => {
			seNamespace.reprocessPage();
			resizeComponentService._resizeComponents(true);
			renderService._resizeSlots();
			components.forEach((component) => renderService._createComponent(component));
			crossFrameEventService.publish(OVERLAY_RERENDERED_EVENT, {addedComponents: components});
		});

		smartEditContractChangeListener.onComponentsRemoved((components: Array<{component: HTMLElement, parent: HTMLElement}>) => {
			seNamespace.reprocessPage();
			renderService._resizeSlots();
			components.forEach((entry) => renderService._destroyComponent(entry.component, entry.parent));
			crossFrameEventService.publish(OVERLAY_RERENDERED_EVENT, {removedComponents: lodash.map(components, 'component')});
		});

		smartEditContractChangeListener.onComponentResized((component: any) => {
			seNamespace.reprocessPage();
			renderService._resizeSlots();
			renderService._updateComponentSizeAndPosition(component);
		});

		smartEditContractChangeListener.onComponentRepositioned((component: any) => {
			renderService._updateComponentSizeAndPosition(component);
		});

		smartEditContractChangeListener.onComponentChanged((component: any, oldAttributes: any) => {
			seNamespace.reprocessPage();
			renderService._resizeSlots();
			renderService._destroyComponent(component, component.parent, oldAttributes);
			renderService._createComponent(component);
		});

		smartEditContractChangeListener.onPageChanged(() => {
			experienceService.updateExperiencePageContext();
		});

		systemEventService.registerEventHandler(EVENTS.AUTHORIZATION_SUCCESS, (evtId: string, evtData: any) => {
			if (evtData.userHasChanged) {
				perspectiveService.refreshPerspective();
			}
		});

		crossFrameEventService.subscribe(EVENTS.PAGE_CHANGE, () => {
			perspectiveService.refreshPerspective();
			languageService.registerSwitchLanguage();
		});

		smartEditContractChangeListener.initListener();

		restServiceFactory.setDomain(domain);

		// Feature registration
		featureService.register({
			key: 'se.emptySlotFix',
			nameI18nKey: 'se.emptyslotfix',
			enablingCallback: () => {
				resizeComponentService._resizeComponents(true);
			},
			disablingCallback: () => {
				resizeComponentService._resizeComponents(false);
			}
		});

		featureService.addDecorator({
			key: 'se.contextualMenu',
			nameI18nKey: 'contextualMenu'
		});

		featureService.addDecorator({
			key: 'se.slotContextualMenu',
			nameI18nKey: 'se.slot.contextual.menu'
		});

	}).name;
