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
angular.module('contextualMenuServiceModule', ['functionsModule', 'eventServiceModule'])
    /**
     * @ngdoc object
     * @name contextualMenuServiceModule.object:REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT
     * @description
     * Name of the event triggered whenever SmartEdit decides to update items in contextual menus.
     */
    .constant('REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT', 'REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT')

    /**
     * @ngdoc service
     * @name contextualMenuServiceModule.ContextualMenuServiceClass
     *
     * @description
     * The ContextualMenuServiceClass is used to add contextual menu items for each component.
     *
     * To add items to the contextual menu, you must call the addItems method of the contextualMenuService and pass a map
     * of the component-type array of contextual menu items mapping. The component type names are the keys in the mapping.
     * The component name can be the full name of the component type, an ant-like wildcard (such as  *middle*Suffix), or a
     * valid regex that includes or excludes a set of component types.
     *
     */
    .factory('ContextualMenuServiceClass', function($q, REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT, uniqueArray, regExpFactory, systemEventService) {

        var ContextualMenuServiceClass = function() {
            this.contextualMenus = {};
        };

        var initContextualMenuItems = function(map, componentType) {

            var componentTypeContextualMenus = map[componentType].filter(function(item) {
                this._validateItem(item);
                if (!item.key) {
                    throw new Error("Item doesn't have key.");
                }

                if (this._featuresList.indexOf(item.key) !== -1) {
                    throw new Error("Item with that key already exist.");
                }
                return true;
            }.bind(this));

            this.contextualMenus[componentType] = uniqueArray((this.contextualMenus[componentType] || []), componentTypeContextualMenus);
        };

        var getFeaturesList = function(contextualMenus) {
            // Would be better to use a set for this, but it's not currently supported by all browsers.
            var featuresList = [];
            for (var key in contextualMenus) {
                featuresList = featuresList.concat(contextualMenus[key].map(function(entry) {
                    return entry.key;
                }));
            }

            return featuresList.reduce(function(previous, current) {
                if (previous.indexOf(current) === -1) {
                    previous.push(current);
                }

                return previous;
            }, []);
        };

        var getUniqueItemArray = function(array1, array2) {
            var currItem;
            var notEqualToCurrentItem = function(item) {
                return (currItem.key !== item.key);
            };

            array2.forEach(function(item) {
                currItem = item;
                if (array1.every(notEqualToCurrentItem)) {
                    array1.push(currItem);
                }
            });

            return array1;
        };

        ContextualMenuServiceClass.prototype._validateItem = function(item) {

            // fix legacy callback param, deprecated in 6.5
            if (item.callback && !item.action) {
                item.action = {
                    callback: item.callback
                };
                delete item.callback;
            }

            if (!item.action) {
                throw "Contextual menu item must provide an action: " + item;
            }
            if ((!!item.action.callback ^ !!item.action.template ^ !!item.action.templateUrl) !== 1) {
                throw "Contextual menu item must have exactly ONE of action callback|template|templateUrl";
            }
        };

        /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuServiceClass#addItems
     * @methodOf contextualMenuServiceModule.ContextualMenuServiceClass
     *
     * @description
     * The method called to add contextual menu items to component types in the SmartEdit application.
     * The contextual menu items are then retrieved by the contextual menu decorator to wire the set of menu items to the specified component.
     *
     * Sample Usage:
     * <pre>
     * contextualMenuService.addItems({
     * '.*Component': [{
     *  key: 'itemKey',
     *  i18nKey: 'CONTEXTUAL_MENU',
     *  condition: function(componentType, componentId) {
     * 	return componentId === 'ComponentType';
     * 	},
     *  callback: function(componentType, componentId) {
     * 	alert('callback for ' + componentType + "_" + componentId);
     * 	},
     *  displayClass: 'democlass',
     *  iconIdle: '.../icons/icon.png',
     *  iconNonIdle: '.../icons/icon.png',
     * }]
     * });
     * </pre>
     *
     * @param {Object} contextualMenuItemsMap An object containing list of componentType to contextual menu items mapping
     *
     * The object contains a list that maps component types to arrays of contextual menu items. The mapping is a key-value pair.
     * The key is the name of the component type, for example, Simple Responsive Banner Component, and the value is an array of contextual menu items, like add, edit, localize, etc.
     *
     * The name of the component type is the key in the mapping. The name can be the full name of the component type, an ant-like wildcard (such as *middle), or a vlide regex that includes or excludes a set of component types.
     * The value in the mapping is an array of contextual menu items to be activated for the component type match.
     *
     * The contextual menu items can have the following properties:
     * @param {String} contextualMenuItemsMap.key key Is the key that identifies a contextual menu item.
     * @param {String} contextualMenuItemsMap.i18nKey i18nKey Is the message key of the contextual menu item to be translated.
     * @param {Function} contextualMenuItemsMap.condition condition Is an optional entry that holds the condition function required to activate the menu item. It is invoked with the following payload:
     * <pre>
     * {
                    	componentType: the smartedit component type
                    	componentId: the smartedit component id
                    	containerType: the type of the container wrapping the component, if applicable
                    	containerId: the id of the container wrapping the component, if applicable
                    	element: the dom element of the component onto which the contextual menu is applied
		}
     * </pre>
     * @param {String} contextualMenuItemsMap.displayClass Contains the CSS classes used to style the contextual menu item
     * @param {String} contextualMenuItemsMap.iconIdle iconIdle Contains the location of the idle icon of the contextual menu item to be displayed.
     * @param {String} contextualMenuItemsMap.iconNonIdle iconNonIdle Contains the location of the non-idle icon of the contextual menu item to be displayed.
     * @param {String} contextualMenuItemsMap.smallIcon smallIcon Contains the location of the smaller version of the icon to be displayed when the menu item is part of the More... menu options.
     * @param {Function|String} contextualMenuItemsMap.callback Deprecated in 6.5 - use a action with callback instead
     * @param {Object} contextualMenuItemsMap.action The action to be performed when clicking the menu item.
     * Action is an object that must contain exactly one of callback | template | templateUrl<br />
     * <strong>callback</strong> A function executed on clicking of the menu item. It is invoked with:
     * <pre>
     * {
        componentType: the smartedit component type
        componentId: the smartedit component id
        containerType: the type of the container wrapping the component, if applicable
        containerId: the id of the container wrapping the component, if applicable
        slotId: the id of the content slot containing the component
		}
     * </pre>
     * <strong>template</strong> is an html string that will displayed below the menu item when the item is clicked.<br />
     * <strong>templateUrl</strong> is the same as template but instead of passing a string, pass a url to an html file.
     *
     */
        ContextualMenuServiceClass.prototype.addItems = function(contextualMenuItemsMap) {

            try {
                if (contextualMenuItemsMap !== undefined) {
                    this._featuresList = getFeaturesList(this.contextualMenus);
                    var componentTypes = Object.keys(contextualMenuItemsMap);
                    componentTypes.forEach(initContextualMenuItems.bind(this, contextualMenuItemsMap));
                }
            } catch (e) {
                throw new Error("addItems() - Cannot add items. " + e);
            }
        };

        /**
         * @ngdoc method
         * @name contextualMenuServiceModule.ContextualMenuServiceClass#removeItemByKey
         * @methodOf contextualMenuServiceModule.ContextualMenuServiceClass
         *
         * @description
         * This method removes the menu items identified by the provided key.
         *
         * @param {String} itemKey The key that identifies the menu items to remove.
         */
        ContextualMenuServiceClass.prototype.removeItemByKey = function(itemKey) {
            var filterFunction = function(item) {
                return (item.key !== itemKey);
            };

            for (var contextualMenuKey in this.contextualMenus) {
                var contextualMenuItems = this.contextualMenus[contextualMenuKey];
                this.contextualMenus[contextualMenuKey] = contextualMenuItems.filter(filterFunction);

                if (this.contextualMenus[contextualMenuKey].length === 0) {
                    // Remove if the contextual menu is empty.
                    delete this.contextualMenus[contextualMenuKey];
                }
            }
        };

        /**
         * @ngdoc method
         * @name contextualMenuServiceModule.ContextualMenuServiceClass#getContextualMenuByType
         * @methodOf contextualMenuServiceModule.ContextualMenuServiceClass
         *
         * @description
         * Will return an array of contextual menu items for a specific component type.
         * For each key in the contextual menus' object, the method converts each component type into a valid regex using the regExpFactory of the function module and then compares it with the input componentType and, if matched, will add it to an array and returns the array.
         *
         * @param {String} componentType The type code of the selected component
         *
         * @returns {Array} An array of contextual menu items assigned to the type.
         *
         */
        ContextualMenuServiceClass.prototype.getContextualMenuByType = function(componentType) {
            var contextualMenuArray = [];
            if (this.contextualMenus) {
                for (var regexpKey in this.contextualMenus) {
                    if (regExpFactory(regexpKey).test(componentType)) {
                        contextualMenuArray = getUniqueItemArray(contextualMenuArray, this.contextualMenus[regexpKey]);
                    }
                }
            }
            return contextualMenuArray;
        };

        /**
         * @ngdoc method
         * @name contextualMenuServiceModule.ContextualMenuServiceClass#getContextualMenuItems
         * @methodOf contextualMenuServiceModule.ContextualMenuServiceClass
         *
         * @description
         * Will return an object that contains a list of contextual menu items that are visible and those that are to be added to the More... options.
         *
         * For each component and display limit size, two arrays are generated.
         * One array contains the menu items that can be displayed and the other array contains the menu items that are available under the more menu items action.
         *
         * @param {Object} configuration The configuration used to determine the selected components
         * @param {String} configuration.componentType The type code of the selected component.
         * @param {String} configuration.componentId The ID of the selected component.
         * @param {String} configuration.containerType The type code of the container of the component if applicable, this is optional.
         * @param {String} configuration.containerId The ID of the container of the component if applicable, this is optional.
         * @param {Number} configuration.iLeftBtns The number of visible contextual menu items for a specified component.
         * @param {Element} configuration.element The DOM element of selected component
         * @returns {Promise} A promise that resolves to an array of contextual menu items assigned to the component type.
         *
         * The returned object contains the following properties
         * - leftMenuItems : An array of menu items that can be displayed on the component.
         * - moreMenuItems : An array of menu items that are available under the more menu items action.
         *
         */
        ContextualMenuServiceClass.prototype.getContextualMenuItems = function(configuration) {
            var iLeftBtns = configuration.iLeftBtns;
            delete configuration.iLeftBtns;

            var newMenuItems = [];
            var newMoreItems = [];
            var menuItems = this.getContextualMenuByType(configuration.componentType);

            var promisesToResolve = [];

            menuItems.forEach(function(item) {
                var deferred = $q.defer();
                promisesToResolve.push(deferred.promise);
                $q.when(item.condition ? item.condition(configuration) : true).then(pushEnabledMenuItemsAsync.bind(undefined, deferred, item, newMenuItems, newMoreItems, iLeftBtns));
            });

            return $q.all(promisesToResolve).then(function() {
                return {
                    'leftMenuItems': newMenuItems,
                    'moreMenuItems': newMoreItems
                };
            });
        };

        /**
         * @ngdoc method
         * @name contextualMenuServiceModule.ContextualMenuServiceClass#refreshMenuItems
         * @methodOf contextualMenuServiceModule.ContextualMenuServiceClass
         *
         * @description
         * This method can be used to ask SmartEdit to retrieve again the list of items in the enabled contextual menus.
         */
        ContextualMenuServiceClass.prototype.refreshMenuItems = function() {
            systemEventService.sendAsynchEvent(REFRESH_CONTEXTUAL_MENU_ITEMS_EVENT);
        };

        // Helper Methods
        function pushEnabledMenuItemsAsync(deferredPromise, menuItem, newMenuItems, newMoreItems, iLeftBtns, isItemEnabled) {

            var collection = newMenuItems.length < iLeftBtns ? newMenuItems : newMoreItems;
            if (isItemEnabled) {
                collection.push(menuItem);
            }
            deferredPromise.resolve();
        }

        return ContextualMenuServiceClass;
    })

    /**
     * Maintaining for backwards compatibility, but use ContextualMenuServiceClass from now on
     * @Deprecated 6.5
     */
    .factory('ContextualMenuService', function(ContextualMenuServiceClass) {
        return ContextualMenuServiceClass;
    })


    /**
     * @ngdoc service
     * @name contextualMenuServiceModule.contextualMenuService
     *
     * @description
     * The contextualMenuService returns an instance of
     * {@link contextualMenuServiceModule.ContextualMenuServiceClass ContextualMenuServiceClass}
     * and is used to add items to contextual menus over components and slots.
     */
    .factory('contextualMenuService', function(ContextualMenuServiceClass) {
        return new ContextualMenuServiceClass();
    });
