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
import './requireLegacyJsFiles';
import './'; // do not remove - import of barrel file that is used to inject featureExtensions modules imports

import {IContextualMenuButton, IFeatureService} from 'smarteditcommons';
import {AssetsService, CmsCommonsModule} from 'cmscommons';
import {CmsSmarteditServicesModule} from 'cmssmartedit/services';

angular.module('cmssmartedit', [
	'resourceLocationsModule',
	'decoratorServiceModule',
	'contextualMenuServiceModule',
	'removeComponentServiceModule',
	'experienceInterceptorModule',
	'editorEnablerServiceModule',
	'alertServiceModule',
	'translationServiceModule',
	'featureServiceModule',
	'slotVisibilityButtonModule',
	'slotVisibilityServiceModule',
	'cmssmarteditTemplates',
	'cmscommonsTemplates',
	'componentHandlerServiceModule',
	'slotSharedButtonModule',
	'cmsDragAndDropServiceModule',
	'syncIndicatorDecoratorModule',
	'slotSyncButtonModule',
	'synchronizationPollingServiceModule',
	'confirmationModalServiceModule',
	'sharedSlotDisabledDecoratorModule',
	'externalSlotDisabledDecoratorModule',
	'slotRestrictionsServiceModule',
	'slotSharedServiceModule',
	'contextualMenuDropdownServiceModule',
	'externalComponentDecoratorModule',
	'externalComponentButtonModule',
	'componentEditingFacadeModule',
	'cmsitemsRestServiceModule',
	'slotUnsharedButtonModule',
	'componentInfoServiceModule',
	CmsCommonsModule,
	CmsSmarteditServicesModule
])

	.run((
		$rootScope: angular.IRootScopeService,
		$q: angular.IQService,
		$translate: angular.translate.ITranslateService,
		alertService: any,
		assetsService: AssetsService,
		cmsDragAndDropService: any,
		componentHandlerService: any,
		confirmationModalService: any,
		contextualMenuService: any,
		decoratorService: any,
		editorEnablerService: any,
		featureService: IFeatureService,
		removeComponentService: any,
		slotRestrictionsService: any,
		slotSharedService: any,
		slotVisibilityService: any,
		componentEditingFacade: any,
		cmsitemsRestService: any,
		componentInfoService: any) => {
		'ngInject';
		editorEnablerService.enableForComponents(['^((?!Slot).)*$']);

		decoratorService.addMappings({
			'^((?!Slot).)*$': ['se.contextualMenu', 'externalComponentDecorator'],
			'^.*Slot$': ['se.slotContextualMenu', 'se.basicSlotContextualMenu', 'syncIndicator', 'sharedSlotDisabledDecorator', 'externalSlotDisabledDecorator']
		});


		featureService.addContextualMenuButton({
			key: 'externalcomponentbutton',
			nameI18nKey: 'se.cms.contextmenu.title.externalcomponent',
			i18nKey: 'se.cms.contextmenu.title.externalcomponentbutton',
			regexpKeys: ['^((?!Slot).)*$'],
			condition: (configuration: any) => {
				const smarteditCatalogVersionUuid = configuration.componentAttributes && configuration.componentAttributes.smarteditCatalogVersionUuid;
				if (smarteditCatalogVersionUuid) {
					return smarteditCatalogVersionUuid !== componentHandlerService.getCatalogVersionUUIDFromPage();
				} else {
					return componentHandlerService.isExternalComponent(configuration.componentId, configuration.componentType);
				}
			},
			callbacks: {},
			action: {
				template: '<external-component-button data-catalog-version-uuid="ctrl.componentAttributes.smarteditCatalogVersionUuid"></external-component-button>'
			},
			callback: () => angular.noop,
			displayClass: 'externalcomponentbutton',
			displayIconClass: 'hyicon hyicon-globe',
			displaySmallIconClass: 'hyicon hyicon-globe'
		} as IContextualMenuButton);

		featureService.addContextualMenuButton({
			key: 'se.cms.dragandropbutton',
			nameI18nKey: 'se.cms.contextmenu.title.dragndrop',
			i18nKey: 'se.cms.contextmenu.title.dragndrop',
			regexpKeys: ['^((?!Slot).)*$'],
			condition: (configuration: any) => {
				const slotId = componentHandlerService.getParentSlotForComponent(configuration.element);
				return slotRestrictionsService.isSlotEditable(slotId);
			},
			callback: () => angular.noop,
			callbacks: {
				mousedown: () => {
					cmsDragAndDropService.update();
				}
			},
			displayClass: 'movebutton',
			displayIconClass: 'hyicon hyicon-dragdroplg',
			displaySmallIconClass: 'hyicon hyicon-dragdroplg',
			permissions: ['se.context.menu.drag.and.drop.component']
		});

		featureService.register({
			key: 'se.cms.html5DragAndDrop',
			nameI18nKey: 'se.cms.dragAndDrop.name',
			descriptionI18nKey: 'se.cms.dragAndDrop.description',
			enablingCallback: () => {
				cmsDragAndDropService.register();
				cmsDragAndDropService.apply();
			},
			disablingCallback: () => {
				cmsDragAndDropService.unregister();
			}
		});

		featureService.addContextualMenuButton({
			key: 'se.cms.remove',
			i18nKey: 'se.cms.contextmenu.title.remove',
			nameI18nKey: 'se.cms.contextmenu.title.remove',
			regexpKeys: ['^((?!Slot).)*$'],
			condition: (configuration: any) => {
				if (!configuration.isComponentHidden) {
					const slotId = componentHandlerService.getParentSlotForComponent(configuration.element);
					return slotRestrictionsService.isSlotEditable(slotId);
				}

				return true;
			},
			callback: (configuration: any, $event: any) => {

				let slotOperationRelatedId: string;
				let slotOperationRelatedType: string;

				if (configuration.element) {
					slotOperationRelatedId = componentHandlerService.getSlotOperationRelatedId(configuration.element);
					slotOperationRelatedType = componentHandlerService.getSlotOperationRelatedType(configuration.element);
				} else {
					slotOperationRelatedId = (configuration.containerId) ? configuration.containerId : configuration.componentId;
					slotOperationRelatedType = (configuration.containerId && configuration.containerType) ? configuration.containerType : configuration.componentType;
				}

				const message: any = {};
				message.description = "se.cms.contextmenu.removecomponent.confirmation.message";
				message.title = "se.cms.contextmenu.removecomponent.confirmation.title";

				confirmationModalService.confirm(message).then(function() {
					removeComponentService.removeComponent({
						slotId: configuration.slotId,
						componentId: configuration.componentId,
						componentType: configuration.componentType,
						slotOperationRelatedId,
						slotOperationRelatedType,
					}).then(
						function() {
							slotVisibilityService.reloadSlotsInfo();
							$translate('se.cms.alert.component.removed.from.slot', {
								componentID: slotOperationRelatedId,
								slotID: configuration.slotId
							}).then(function(translation: string) {
								alertService.showSuccess({
									message: translation
								});
								$event.preventDefault();
								$event.stopPropagation();
							});
						});
				});
			},
			displayClass: 'removebutton',
			displayIconClass: 'hyicon hyicon-removelg',
			displaySmallIconClass: 'hyicon hyicon-removelg',
			permissions: ['se.context.menu.remove.component']
		});

		featureService.addContextualMenuButton({
			key: 'se.slotContextualMenuVisibility',
			nameI18nKey: 'slotcontextmenu.title.visibility',
			regexpKeys: ['^.*ContentSlot$'],
			callback: () => angular.noop,
			templateUrl: 'slotVisibilityWidgetTemplate.html',
			permissions: ['se.slot.context.menu.visibility']
		});

		featureService.addContextualMenuButton({
			key: 'se.slotSharedButton',
			nameI18nKey: 'slotcontextmenu.title.shared.button',
			regexpKeys: ['^.*Slot$'],
			callback: () => angular.noop,
			templateUrl: 'slotSharedTemplate.html',
			permissions: ['se.slot.context.menu.shared.icon']
		});

		featureService.addContextualMenuButton({
			key: 'slotUnsharedButton',
			nameI18nKey: 'slotcontextmenu.title.unshared.button',
			regexpKeys: ['^.*Slot$'],
			callback: () => angular.noop,
			templateUrl: 'slotUnsharedButtonWrapperTemplate.html',
			permissions: ['se.slot.context.menu.unshared.icon']
		});

		featureService.addContextualMenuButton({
			key: 'se.slotSyncButton',
			nameI18nKey: 'slotcontextmenu.title.sync.button',
			regexpKeys: ['^.*Slot$'],
			callback: () => angular.noop,
			templateUrl: 'slotSyncTemplate.html',
			permissions: ['se.sync.slot.context.menu']
		});

		featureService.addDecorator({
			key: 'syncIndicator',
			nameI18nKey: 'syncIndicator',
			permissions: ['se.sync.slot.indicator']
		});

		featureService.register({
			key: 'disableSharedSlotEditing',
			nameI18nKey: 'se.cms.disableSharedSlotEditing',
			descriptionI18nKey: 'se.cms.disableSharedSlotEditing.description',
			enablingCallback: () => {
				slotSharedService.setSharedSlotEnablementStatus(true);
			},
			disablingCallback: () => {
				slotSharedService.setSharedSlotEnablementStatus(false);
			}
		});

		featureService.addDecorator({
			key: 'sharedSlotDisabledDecorator',
			nameI18nKey: 'se.cms.shared.slot.disabled.decorator',
			displayCondition: (componentType: string, componentId: string) => {
				return slotRestrictionsService.isSlotEditable(componentId).then(function(isEditable: boolean) {
					return !isEditable;
				});
			}
		});

		featureService.addDecorator({
			key: 'externalSlotDisabledDecorator',
			nameI18nKey: 'se.cms.external.slot.disabled.decorator',
			displayCondition: (componentType: string, componentId: string) => {
				return $q.when(componentHandlerService.isExternalComponent(componentId, componentType));
			}
		});

		featureService.addDecorator({
			key: 'externalComponentDecorator',
			nameI18nKey: 'se.cms.external.component.decorator',
			displayCondition: (componentType: string, componentId: string) => {
				return $q.when(componentHandlerService.isExternalComponent(componentId, componentType));
			}
		});

		featureService.addContextualMenuButton({
			key: 'clonecomponentbutton',
			nameI18nKey: 'se.cms.contextmenu.title.clone.component',
			i18nKey: 'se.cms.contextmenu.title.clone.component',
			regexpKeys: ['^((?!Slot).)*$'],
			condition: (configuration: any) => {
				const componentUuid = configuration.componentAttributes.smarteditComponentUuid;
				if (!configuration.isComponentHidden) {
					const slotId = componentHandlerService.getParentSlotForComponent(configuration.element);
					return slotRestrictionsService.isSlotEditable(slotId).then(function(slotEditable: boolean) {
						if (slotEditable) {
							return componentInfoService.getById(componentUuid).then((component: any) => {
								return component.cloneable;
							});
						} else {
							return $q.when(false);
						}
					});
				}
				return cmsitemsRestService.getById(componentUuid).then((component: any) => {
					return component.cloneable;
				});
			},
			callback: (configuration: any) => {
				const sourcePosition = componentHandlerService.getComponentPositionInSlot(configuration.slotId, configuration.componentAttributes.smarteditComponentId);
				componentEditingFacade.cloneExistingComponentToSlot({
					targetSlotId: configuration.slotId,
					dragInfo: {
						componentId: configuration.componentAttributes.smarteditComponentId,
						componentType: configuration.componentType,
						componentUuid: configuration.componentAttributes.smarteditComponentUuid
					},
					position: sourcePosition + 1
				});
			},
			displayClass: 'clonebutton',
			displayIconClass: 'hyicon hyicon-clone',
			displaySmallIconClass: 'hyicon hyicon-clone',
			permissions: ['se.clone.component']
		});
	});
