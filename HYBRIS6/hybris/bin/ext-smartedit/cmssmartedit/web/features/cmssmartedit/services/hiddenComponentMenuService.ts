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

import {ContainerInfo, SlotContainerService} from './slotContainerService';

interface ContextualMenuItem {
	key: string;
	i18nKey: string;
	icon: string;
	callback: any;
	condition: (componentInfo: ComponentInfo) => boolean;
	componentInfo?: ComponentInfo;
	slotInfo?: SlotInfo;
}

interface ComponentAttributes {
	smarteditCatalogVersionUuid: string;
	smarteditComponentId: string;
	smarteditComponentType: string;
	smarteditComponentUuid: string;
	smarteditElementUuid: string;
}

interface SlotInfo {
	slotId: string;
	slotUuid: string;
}

interface ComponentInfo {
	componentType: string;
	componentId: string;
	componentAttributes: ComponentAttributes;
	containerType?: string;
	containerId?: string;
	element?: string;
	isComponentHidden: boolean;
}

/**
 * @ngdoc service
 * @name cms.smartedit.services:hiddenComponentMenuService
 *
 * @description
 * This service is used to retrieve menu items that are available to be used with hidden components. 
 */
/* @ngInject */
export class HiddenComponentMenuService {

	// --------------------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------------------
	private readonly MENU_ITEM_EXTERNAL = 'externalcomponentbutton';
	private readonly MENU_ITEM_CLONE = 'clonecomponentbutton';
	private readonly MENU_ITEM_REMOVE = 'se.cms.remove';

	// --------------------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------------------
	private allowedItems: {[key: string]: boolean} = {};

	// --------------------------------------------------------------------------------------
	// Constructor
	// --------------------------------------------------------------------------------------
	constructor(private $q: angular.IQService, private contextualMenuService: any, private componentHandlerService: any, private slotContainerService: SlotContainerService, private CONTENT_SLOT_TYPE: string) {
		this._setDefaultItemsAllowed();
	}

	// --------------------------------------------------------------------------------------
	// Public API
	// --------------------------------------------------------------------------------------
	/**
	 * @ngdoc method
	 * @name cms.smartedit.services:hiddenComponentMenuService#allowItemsInHiddenComponentMenu
	 * @methodOf cms.smartedit.services:hiddenComponentMenuService
	 *
	 * @description
	 * This method is used to set the list of items that can be used with hidden components. 
	 * 
	 * @param {String[]} itemsToAllow The ID of the menu items that can be used with hidden components. 
	 *
	 */
	public allowItemsInHiddenComponentMenu(itemsToAllow: string[]): void {
		itemsToAllow.forEach((item) => {
			this.allowedItems[item] = true;
		});
	}

	/**
	 * @ngdoc method
	 * @name cms.smartedit.services:hiddenComponentMenuService#removeAllowedItemsInHiddenComponentMenu
	 * @methodOf cms.smartedit.services:hiddenComponentMenuService
	 *
	 * @description
	 * This method removes a provided set of allowed menu items if previously allowed. 
	 * 
	 * @param {String[]} itemsToAllow An array containing the ID's of the menu items that cannot be used any longer with hidden components. 
	 *
	 */
	public removeAllowedItemsInHiddenComponentMenu(itemsToDisallow: string[]): void {
		itemsToDisallow.forEach((item) => {
			delete this.allowedItems[item];
		});
	}

	/**
	 * @ngdoc method
	 * @name cms.smartedit.services:hiddenComponentMenuService#removeAllowedItemsInHiddenComponentMenu
	 * @methodOf cms.smartedit.services:hiddenComponentMenuService
	 *
	 * @description
	 * This method retrieves the list of IDs of the menu items that can be used with hidden components. 
	 * 
	 * @returns {String[]} The list of IDs of the menu items that can be used with hidden components. 
	 *
	 */
	public getAllowedItemsInHiddenComponentMenu(): string[] {
		return Object.keys(this.allowedItems);
	}

	/**
	 * @ngdoc method
	 * @name cms.smartedit.services:hiddenComponentMenuService#getItemsForHiddenComponent
	 * @methodOf cms.smartedit.services:hiddenComponentMenuService
	 *
	 * @description
	 * This method is used to retrieve the menu items available to be used in the provided component. To do so, 
	 * this method retrieves contextual menu items available for the provided component and filters out the ones that cannot
	 * be used in hidden components. For example, assuming that a visible component has 'drag and drop' and 'remove' 
	 * contextual menu items, if the component is hidden it should only have the remove button available, since the 
	 * drag and drop operation is meaningless if the component is hidden. Hence, this service will retrieve only 
	 * the remove item. 
	 * 
	 * @param {Object} component The hidden component for which to retrieve its menu items. 
	 * @param {String} slotId The SmartEdit id of the slot where the component is located. 
	 * 
	 * @returns {Promise} Promise that resolves to an array of contextual menu items available for the component 
	 * provided. 
	 */
	public getItemsForHiddenComponent(component: any, slotId: string): Promise<ContextualMenuItem[]> {
		this._validateComponent(component);

		return this._buildComponentInfo(slotId, component).then((componentInfo: ComponentInfo) => {
			const slotInfo = this._buildSlotInfo(slotId);
			return this._getAllowedItemsForComponent(component, componentInfo, slotInfo);
		});
	}

	// --------------------------------------------------------------------------------------
	// Helper Methods
	// --------------------------------------------------------------------------------------
	private _validateComponent(component: any) {
		if (!component) {
			throw new Error('HiddenComponentMenuService - Component cannot be null.');
		}
		if (!component.uid) {
			throw new Error('HiddenComponentMenuService - Component needs a uid.');
		}
		if (!component.typeCode) {
			throw new Error('HiddenComponentMenuService - Component needs a type code.');
		}
		if (!component.uuid) {
			throw new Error('HiddenComponentMenuService - Component needs a uuid.');
		}
	}

	private _setDefaultItemsAllowed() {
		this.allowItemsInHiddenComponentMenu([this.MENU_ITEM_EXTERNAL, this.MENU_ITEM_CLONE, this.MENU_ITEM_REMOVE]);
	}

	private _buildComponentInfo(slotId: string, component: any): Promise<ComponentInfo> {
		return this.slotContainerService.getComponentContainer(slotId, component.uuid)
			.then((componentContainer: ContainerInfo) => {
				return {
					componentType: component.typeCode,
					componentId: component.uid,
					componentAttributes: {
						smarteditCatalogVersionUuid: component.catalogVersion,
						smarteditComponentId: component.uid,
						smarteditComponentType: component.componentType,
						smarteditComponentUuid: component.uuid,
						smarteditElementUuid: null
					},
					containerType: componentContainer ? componentContainer.containerType : null,
					containerId: componentContainer ? componentContainer.containerId : null,
					element: null,
					isComponentHidden: true
				};
			});
	}

	private _buildSlotInfo(slotId: string) {
		const component = this.componentHandlerService.getOriginalComponent(slotId, this.CONTENT_SLOT_TYPE);
		const slotUuid = this.componentHandlerService.getUuid(component);

		return {
			slotId,
			slotUuid
		};
	}

	private _getAllowedItemsForComponent(component: any, componentInfo: ComponentInfo, slotInfo: SlotInfo) {
		const itemsToReturn: ContextualMenuItem[] = [];
		const promisesToResolve: Array<angular.IPromise<any>> = [];
		const menuItems = this.contextualMenuService.getContextualMenuByType(component.typeCode) as ContextualMenuItem[];

		// Get items allowed in all hidden components
		menuItems.filter((item) => {
			return this.allowedItems[item.key];
		}).forEach((item) => {

			// Get items enabled for the current hidden component. 
			const deferred = this.$q.defer();
			promisesToResolve.push(deferred.promise);

			this.$q.when(item.condition ? item.condition(componentInfo) : true).then((isEnabled: boolean) => {
				if (isEnabled) {
					item.slotInfo = slotInfo;
					item.componentInfo = componentInfo;

					itemsToReturn.push(item);
				}

				deferred.resolve();
			});
		});

		return this.$q.all(promisesToResolve).then(() => {
			return itemsToReturn;
		});
	}
}
