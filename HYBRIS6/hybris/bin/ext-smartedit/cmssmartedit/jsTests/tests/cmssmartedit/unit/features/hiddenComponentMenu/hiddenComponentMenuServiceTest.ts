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

import 'jasmine';

import {HiddenComponentMenuService, SlotContainerService} from '../../../../../../web/features/cmssmartedit/services';

describe('hiddenComponentMenu', () => {

	// --------------------------------------------------------------------------------------
	// Constants
	// --------------------------------------------------------------------------------------
	const CONTENT_SLOT_TYPE = 'slot';
	const COMPONENT_TYPE = 'componentType1';
	const COMPONENT_ID = 'component1';
	const SLOT_ID = 'some slot ID';
	const CONTAINER_ID = 'some container id';
	const CONTAINER_TYPE = 'some container type';
	const conditionFn = jasmine.createSpy('condition');
	const CONTEXTUAL_MENU_ITEMS = [{
		key: 'key1',
		action: {
			template: "dummy template string"
		},
		i18nKey: 'ICON1',
		icon: 'icon1.png'
	}, {
		key: 'key2',
		action: {
			template: "dummy template string"
		},
		i18nKey: 'ICON2',
		condition: conditionFn,
		icon: 'icon2.png'
	}, {
		key: 'key3',
		action: {
			template: "dummy template string"
		},
		i18nKey: 'ICON3',
		icon: 'icon3.png'
	}];
	const EXPECTED_COMPONENT_INFO = {
		componentType: 'componentType1',
		componentId: 'component1',
		componentAttributes: {smarteditCatalogVersionUuid: undefined as any, smarteditComponentId: 'component1', smarteditComponentType: undefined as any, smarteditComponentUuid: 'SOME UIID', smarteditElementUuid: null as any},
		containerType: CONTAINER_TYPE,
		containerId: CONTAINER_ID,
		element: null as any, isComponentHidden: true
	};

	// --------------------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------------------
	let hiddenComponentMenuService: HiddenComponentMenuService;
	let slotContainerService: jasmine.SpyObj<SlotContainerService>;
	let contextualMenuService: any;
	let condition: boolean;
	let component: any;

	let $q: angular.IQService;
	let $rootScope: angular.IRootScopeService;

	// --------------------------------------------------------------------------------------
	// Tests
	// --------------------------------------------------------------------------------------
	beforeEach(() => {
		const fixture = AngularUnitTestHelper.prepareModule('cms.smartedit.services')
			.mock('contextualMenuService', 'getContextualMenuByType')
			.mock('componentHandlerService', 'getOriginalComponent')
			.mock('componentHandlerService', 'getUuid')
			.mock('slotContainerService', 'getComponentContainer')
			.mockConstant('CONTENT_SLOT_TYPE', CONTENT_SLOT_TYPE)
			.service('hiddenComponentMenuService');

		$rootScope = fixture.injected.$rootScope;
		$q = fixture.injected.$q;

		contextualMenuService = fixture.mocks.contextualMenuService;
		slotContainerService = fixture.mocks.slotContainerService;
		hiddenComponentMenuService = fixture.service;

		slotContainerService.getComponentContainer.and.returnValue($q.when({
			containerId: CONTAINER_ID,
			containerType: CONTAINER_TYPE
		}));
		conditionFn.and.callFake(() => {
			return condition;
		});

		condition = true;
		component = {
			uid: COMPONENT_ID,
			uuid: 'SOME UIID',
			typeCode: COMPONENT_TYPE
		};
	});

	it(`GIVEN there are contextual menu items defined for the given component
        WHEN getMenuItemsForHiddenComponent is called
        THEN it returns an empty list of items`, () => {
			// GIVEN
			contextualMenuService.getContextualMenuByType.and.returnValue([]);

			// WHEN
			const resultPromise = hiddenComponentMenuService.getItemsForHiddenComponent(component, SLOT_ID);

			// THEN
			resultPromise.then((result) => {
				expect(result).toEqualData([]);
			});
			$rootScope.$digest();
		});

	it(`GIVEN there are contextual menu items defined for the given component but they are not enabled for hidden components
        WHEN getMenuItemsForHiddenComponent is called
        THEN it returns an empty list of items`, () => {
			// GIVEN
			contextualMenuService.getContextualMenuByType.and.returnValue(CONTEXTUAL_MENU_ITEMS);

			// WHEN
			const resultPromise = hiddenComponentMenuService.getItemsForHiddenComponent(component, SLOT_ID);

			// THEN
			resultPromise.then((result) => {
				expect(result).toEqualData([]);
			});
			$rootScope.$digest();
		});

	it(`GIVEN contextual menu has items defined for the given component and they are enabled for hidden components
        WHEN getMenuItemsForHiddenComponent is called
        THEN it returns those items`, () => {
			// GIVEN
			contextualMenuService.getContextualMenuByType.and.returnValue(CONTEXTUAL_MENU_ITEMS);
			hiddenComponentMenuService.allowItemsInHiddenComponentMenu(['key2']);

			// WHEN
			const resultPromise = hiddenComponentMenuService.getItemsForHiddenComponent(component, SLOT_ID);

			// THEN
			resultPromise.then((result) => {
				expect(conditionFn).toHaveBeenCalledWith(EXPECTED_COMPONENT_INFO);
				expect(result).toEqualData([{key: 'key2', action: {template: 'dummy template string'}, i18nKey: 'ICON2', icon: 'icon2.png', slotInfo: {slotId: 'some slot ID', slotUuid: undefined}, componentInfo: EXPECTED_COMPONENT_INFO}]);
			});
			$rootScope.$digest();
		});

	it(`GIVEN contextual menu has items defined for the given component and they are enabled for hidden components but have an invalid condition
        WHEN getMenuItemsForHiddenComponent is called
        THEN it doesnt return those items`, () => {
			// GIVEN
			contextualMenuService.getContextualMenuByType.and.returnValue(CONTEXTUAL_MENU_ITEMS);
			hiddenComponentMenuService.allowItemsInHiddenComponentMenu(['key2']);
			condition = false;

			// WHEN
			const resultPromise = hiddenComponentMenuService.getItemsForHiddenComponent(component, SLOT_ID);

			// THEN
			resultPromise.then((result) => {
				expect(conditionFn).toHaveBeenCalledWith(EXPECTED_COMPONENT_INFO);
				expect(result).toEqualData([]);
			});
			$rootScope.$digest();
		});

	it(`GIVEN hiddenComponentMenuService already has allowed items
        WHEN getAllowedItemsInHiddenComponentMenu
        THEN it returns those item keys`, () => {
			// GIVEN

			// WHEN
			const allowedItems = hiddenComponentMenuService.getAllowedItemsInHiddenComponentMenu();

			// THEN
			expect(allowedItems).toEqualData([
				'externalcomponentbutton',
				'clonecomponentbutton',
				'se.cms.remove'
			]);
		});

	it(`GIVEN hiddenComponentMenuService already has allowed items
        WHEN removeAllowedItemsInHiddenComponentMenu
        THEN it removes those items from the allowed items`, () => {
			// GIVEN

			// WHEN
			hiddenComponentMenuService.removeAllowedItemsInHiddenComponentMenu(['other_item', 'externalcomponentbutton']);
			const allowedItems = hiddenComponentMenuService.getAllowedItemsInHiddenComponentMenu();

			// THEN
			expect(allowedItems).toEqualData([
				'clonecomponentbutton',
				'se.cms.remove'
			]);
		});
});
