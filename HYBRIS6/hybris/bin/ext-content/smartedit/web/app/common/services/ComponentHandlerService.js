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
/**
 * @ngdoc overview
 * @name componentHandlerServiceModule
 * @description
 * 
 * this module aims at handling smartEdit components both on the original storefront and the smartEdit overlay
 * 
 */
angular.module('componentHandlerServiceModule', ['functionsModule', 'yjqueryModule', 'yLoDashModule'])

    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.OVERLAY_ID
     * @description
     * the identifier of the overlay placed in front of the storefront to where all smartEdit component decorated clones are copied.
     */
    .constant('OVERLAY_ID', 'smarteditoverlay')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.COMPONENT_CLASS
     * @description
     * the css class of the smartEdit components as per contract with the storefront
     */
    .constant('COMPONENT_CLASS', 'smartEditComponent')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.OVERLAY_COMPONENT_CLASS
     * @description
     * the css class of the smartEdit component clones copied to the storefront overlay
     */
    .constant('OVERLAY_COMPONENT_CLASS', 'smartEditComponentX')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.SMARTEDIT_ATTRIBUTE_PREFIX
     * @description
     * If the storefront needs to expose more attributes than the minimal contract, these attributes must be prefixed with this constant value
     */
    .constant('SMARTEDIT_ATTRIBUTE_PREFIX', 'data-smartedit-')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.ID_ATTRIBUTE
     * @description
     * the id attribute of the smartEdit components as per contract with the storefront
     */
    .constant('ID_ATTRIBUTE', 'data-smartedit-component-id')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.UUID_ATTRIBUTE
     * @description
     * the uuid attribute of the smartEdit components as per contract with the storefront
     */
    .constant('UUID_ATTRIBUTE', 'data-smartedit-component-uuid')
    /**
     * @description
     * the front-end randomly generated uuid of the smartEdit components and their equivalent in the overlay
     */
    .constant('ELEMENT_UUID_ATTRIBUTE', 'data-smartedit-element-uuid')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.UUID_ATTRIBUTE
     * @description
     * the uuid attribute of the smartEdit components as per contract with the storefront
     */
    .constant('CATALOG_VERSION_UUID_ATTRIBUTE', 'data-smartedit-catalog-version-uuid')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.TYPE_ATTRIBUTE
     * @description
     * the type attribute of the smartEdit components as per contract with the storefront
     */
    .constant('TYPE_ATTRIBUTE', 'data-smartedit-component-type')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE
     * @description
     * the id attribute of the smartEdit container, when applicable, as per contract with the storefront
     */
    .constant('CONTAINER_ID_ATTRIBUTE', 'data-smartedit-container-id')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE
     * @description
     * the type attribute of the smartEdit container, when applicable, as per contract with the storefront
     */
    .constant('CONTAINER_TYPE_ATTRIBUTE', 'data-smartedit-container-type')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTENT_SLOT_TYPE
     * @description
     * the type value of the smartEdit slots as per contract with the storefront
     */
    .constant('CONTENT_SLOT_TYPE', 'ContentSlot')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.SMARTEDIT_IFRAME_ID
     * @description
     * the id of the iframe which contains storefront
     */
    .constant('SMARTEDIT_IFRAME_ID', 'ySmartEditFrame')

    .constant('SMARTEDIT_IFRAME_WRAPPER_ID', '#js_iFrameWrapper')

    /**
     * @ngdoc service
     * @name componentHandlerServiceModule.componentHandlerService
     * @description
     *
     * The service provides convenient methods to get DOM references of smartEdit components both in the original laye rof the storefornt and in the smartEdit overlay
     */
    .factory(
        'componentHandlerService',
        function(yjQuery, $window, isBlank, lodash, OVERLAY_ID, COMPONENT_CLASS, OVERLAY_COMPONENT_CLASS, ID_ATTRIBUTE, UUID_ATTRIBUTE, CATALOG_VERSION_UUID_ATTRIBUTE, TYPE_ATTRIBUTE, CONTAINER_ID_ATTRIBUTE, CONTAINER_TYPE_ATTRIBUTE, CONTENT_SLOT_TYPE, SMARTEDIT_IFRAME_ID, SMARTEDIT_IFRAME_WRAPPER_ID, ELEMENT_UUID_ATTRIBUTE) {

            var buildComponentQuery = function(smarteditComponentId, smarteditComponentType, cssClass) {
                var query = '';
                query += (cssClass ? '.' + cssClass : '');
                query += '[' + ID_ATTRIBUTE + '=\'' + smarteditComponentId + '\']';
                query += '[' + TYPE_ATTRIBUTE + '=\'' + smarteditComponentType + '\']';
                return query;
            };

            var buildComponentsInSlotQuery = function(slotId) {
                var query = '';
                query += ('.' + COMPONENT_CLASS);
                query += '[' + ID_ATTRIBUTE + '=\'' + slotId + '\']';
                query += '[' + TYPE_ATTRIBUTE + '=\'' + CONTENT_SLOT_TYPE + '\']';
                query += ' > ';
                query += '[' + ID_ATTRIBUTE + '\]';
                return query;
            };

            var buildComponentInSlotQuery = function(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass) {
                var slotQuery = buildComponentQuery(smarteditSlotId, CONTENT_SLOT_TYPE);
                var componentQuery = buildComponentQuery(smarteditComponentId, smarteditComponentType, cssClass);
                return slotQuery + ' ' + componentQuery;
            };

            return {

                _isIframe: function() {
                    return $window.frameElement;
                },

                _getTargetBody: function() {
                    return this._isIframe() ? this.getFromSelector('body') : this._getTargetIframe().contents().find('body');
                },
                _getTargetIframe: function() {
                    return this.getFromSelector('iframe#' + SMARTEDIT_IFRAME_ID);
                },
                _getTargetIframeWrapper: function() {
                    return this.getFromSelector(SMARTEDIT_IFRAME_WRAPPER_ID);
                },


                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getPageUID
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This extracts the pageUID of the storefront page loaded in the smartedit iframe.
                 *
                 * @return {String} a string matching the page's ID
                 */
                getPageUID: function() {
                    try {
                        return /smartedit-page-uid\-(\S+)/.exec(this._getTargetBody().attr('class'))[1];
                    } catch (e) {
                        throw {
                            name: "InvalidStorefrontPageError",
                            message: "Error detected. The page is not a valid storefront page."
                        };
                    }
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getPageUUID
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This extracts the pageUUID of the storefront page loaded in the smartedit iframe.
                 * The UUID is different from the UID in that it is an encoding of uid and catalog version combined
                 *
                 * @return {String} The page's UUID
                 */
                getPageUUID: function() {
                    try {
                        return /smartedit-page-uuid\-(\S+)/.exec(this._getTargetBody().attr('class'))[1];
                    } catch (e) {
                        throw {
                            name: "InvalidStorefrontPageError",
                            message: "Error detected. The page is not a valid storefront page."
                        };
                    }
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getCatalogVersionUUIDFromPage
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This extracts the catalogVersionUUID of the storefront page loaded in the smartedit iframe.
                 * The UUID is different from the UID in that it is an encoding of uid and catalog version combined
                 *
                 * @return {String} The page's UUID
                 */
                getCatalogVersionUUIDFromPage: function() {
                    try {
                        return /smartedit-catalog-version-uuid\-(\S+)/.exec(this._getTargetBody().attr('class'))[1];
                    } catch (e) {
                        throw {
                            name: "InvalidStorefrontPageError",
                            message: "Error detected. The page is not a valid storefront page."
                        };
                    }
                },

                buildOverlayQuery: function() {
                    return '#' + OVERLAY_ID;
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getFromSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This is a wrapper around yjQuery selector
                 *
                 * @param {String} selector String selector as per yjQuery API
                 * 
                 * @return {Object} a yjQuery object for the given selector
                 */
                getFromSelector: function(selector) {
                    return yjQuery(selector);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOverlay
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves a handler on the smartEdit overlay div
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 */
                getOverlay: function() {
                    return this.getFromSelector(this.buildOverlayQuery());
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_isOverlayOn
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * determines whether the overlay is visible
                 * This method can only be invoked from the smartEdit application and not the smartEdit iframe.
                 */
                isOverlayOn: function() {
                    return this.getOverlay().length && this.getOverlay().is(":visible");
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponentUnderSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around a smartEdit component identified by its smartEdit id, smartEdit type and an optional class
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} smarteditSlotId the slot id of the slot containing the component as per the smartEdit contract with the storefront
                 * @param {String} cssClass the css Class to further restrict the search on. This parameter is optional.
                 * 
                 * @return {Object} a yjQuery object wrapping the searched component
                 */
                getComponentUnderSlot: function(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass) {
                    return this.getFromSelector(buildComponentInSlotQuery(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around a smartEdit component identified by its smartEdit id, smartEdit type and an optional class
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} cssClass the css Class to further restrict the search on. This parameter is optional.
                 * 
                 * @return {Object} a yjQuery object wrapping the searched component
                 */
                getComponent: function(smarteditComponentId, smarteditComponentType, cssClass) {
                    return this.getFromSelector(buildComponentQuery(smarteditComponentId, smarteditComponentType, cssClass));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOriginalComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around a smartEdit component of the original storefront layer identified by its smartEdit id, smartEdit type
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * 
                 * @return {Object} a yjQuery object wrapping the searched component
                 */
                getOriginalComponent: function(smarteditComponentId, smarteditComponentType) {
                    return this.getComponent(smarteditComponentId, smarteditComponentType, COMPONENT_CLASS);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOriginalComponentWithinSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around a smartEdit component of the original storefront layer identified by its smartEdit id, smartEdit type and slot ID
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} slotId the ID of the slot within which the component resides
                 * 
                 * @return {Object} a yjQuery object wrapping the searched component
                 */
                getOriginalComponentWithinSlot: function(smarteditComponentId, smarteditComponentType, slotId) {
                    return this.getComponentUnderSlot(smarteditComponentId, smarteditComponentType, slotId, COMPONENT_CLASS);
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOverlayComponentWithinSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around a smartEdit component of the overlay layer identified by its smartEdit id, smartEdit type and slot ID
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} slotId the ID of the slot within which the component resides
                 * 
                 * @return {Object} a yjQuery object wrapping the searched component
                 */
                getOverlayComponentWithinSlot: function(smarteditComponentId, smarteditComponentType, slotId) {
                    return this.getComponentUnderSlot(smarteditComponentId, smarteditComponentType, slotId, OVERLAY_COMPONENT_CLASS);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOverlayComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around the smartEdit component of the overlay layer corresponding to the storefront layer component passed as argument
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {Object} originalComponent the DOM element in the storefront layer
                 * 
                 * @return {Object} a yjQuery object wrapping the searched component
                 */
                getOverlayComponent: function(originalComponent) {
                    var slotId = this.getParentSlotForComponent(originalComponent.parent());
                    if (slotId) {
                        //it is not a slot
                        return this.getComponentUnderSlot(originalComponent.attr(ID_ATTRIBUTE), originalComponent.attr(TYPE_ATTRIBUTE), slotId, OVERLAY_COMPONENT_CLASS);
                    } else {
                        //it is a slot
                        return this.getComponent(originalComponent.attr(ID_ATTRIBUTE), originalComponent.attr(TYPE_ATTRIBUTE), OVERLAY_COMPONENT_CLASS);
                    }
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponentInOverlay
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around a smartEdit component of the overlay div identified by its smartEdit id, smartEdit type
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * 
                 * @return {Object} a yjQuery object wrapping the searched component
                 * @deprecated since 6.5, use {@link componentHandlerServiceModule.componentHandlerService#methodsOf_getOverlayComponentWithinSlot getOverlayComponentWithinSlot} or {@link componentHandlerServiceModule.componentHandlerService#methodsOf_getOverlayComponent getOverlayComponent} 
                 */
                getComponentInOverlay: function(smarteditComponentId, smarteditComponentType) {
                    return this.getComponent(smarteditComponentId, smarteditComponentType, OVERLAY_COMPONENT_CLASS);
                },

                getComponentUnderParentOverlay: function(smarteditComponentId, smarteditComponentType, parentOverlay) {
                    return this.getFromSelector(parentOverlay)
                        .find(buildComponentQuery(smarteditComponentId, smarteditComponentType, OVERLAY_COMPONENT_CLASS));
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponentPositionInSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the position of a component within a slot
                 *
                 * @param {String} slotId the slot id as per the smartEdit contract with the storefront
                 * @param {String} componentId the component id as per the smartEdit contract with the storefront
                 *
                 * @return {Integer} the position of the component within a slot
                 */
                getComponentPositionInSlot: function(slotId, componentId) {
                    var components = this.getOriginalComponentsWithinSlot(slotId);

                    return lodash.findIndex(components, function(component) {
                        return this.getSlotOperationRelatedId(component) === componentId;
                    }.bind(this));
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getParent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the direct smartEdit component parent of a given component.
                 * The parent is fetched in the same layer (original storefront or smartEdit overlay) as the child 
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {Object} component the yjQuery component for which to search a parent
                 * 
                 * @return {Object} a yjQuery object wrapping the smae-layer parent component
                 */
                getParent: function(component) {
                    component = this.getFromSelector(component);
                    var parentClassToLookFor = component.hasClass(COMPONENT_CLASS) ? COMPONENT_CLASS : (component.hasClass(OVERLAY_COMPONENT_CLASS) ? OVERLAY_COMPONENT_CLASS : null);
                    if (isBlank(parentClassToLookFor)) {
                        throw "componentHandlerService.getparent.error.component.from.unknown.layer";
                    }
                    return component.closest("." + parentClassToLookFor + "[" + ID_ATTRIBUTE + "]" + "[" + ID_ATTRIBUTE + "!='" + component.attr(ID_ATTRIBUTE) + "']");
                },



                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getClosestSmartEditComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Returns the closest parent (or self) being a smartEdit component
                 *
                 * @param {Object} component the DOM/yjQuery element for which to search a parent
                 */
                getClosestSmartEditComponent: function(component) {
                    var wrappedComponent = this.getFromSelector(component);
                    return this.getFromSelector(wrappedComponent.closest("." + COMPONENT_CLASS));
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_isSmartEditComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Determines whether a DOM/yjQuery element is a smartEdit component
                 *
                 * @param {Object} component the DOM/yjQuery element
                 */
                isSmartEditComponent: function(component) {
                    return this.getFromSelector(component).hasClass(COMPONENT_CLASS);
                },


                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_setId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Sets the smartEdit component id of a given component
                 *
                 * @param {Object} component the yjQuery component for which to set the id
                 * @param {String} id the id to be set
                 */
                setId: function(component, id) {
                    return this.getFromSelector(component).attr(ID_ATTRIBUTE, id);
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component id of a given component
                 *
                 * @param {Object} component the yjQuery component for which to get the id
                 * 
                 * @return {String} the component id
                 */
                getId: function(component) {
                    return this.getFromSelector(component).attr(ID_ATTRIBUTE);
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getUuid
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component id of a given component
                 *
                 * @param {Object} component the yjQuery component for which to get the id
                 * 
                 * @return {String} the component id
                 */
                getUuid: function(component) {
                    return this.getFromSelector(component).attr(UUID_ATTRIBUTE);
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getCatalogVersionUuid
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component id of a given component
                 *
                 * @param {Object} component the yjQuery component for which to get the id
                 * 
                 * @return {String} the component id
                 */
                getCatalogVersionUuid: function(component) {
                    return this.getFromSelector(component).attr(CATALOG_VERSION_UUID_ATTRIBUTE);
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getSlotOperationRelatedId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the id that is relevant to be able to perform slot related operations for this components
                 * It typically is {@link componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE} when applicable and defaults to {@link componentHandlerServiceModule.ID_ATTRIBUTE}
                 *
                 * @param {Object} component the yjQuery component for which to get the id
                 * 
                 * @return {String} the slot operations related id
                 */
                getSlotOperationRelatedId: function(component) {
                    var containerId = this.getFromSelector(component).attr(CONTAINER_ID_ATTRIBUTE);
                    return containerId && this.getFromSelector(component).attr(CONTAINER_TYPE_ATTRIBUTE) ? containerId : this.getFromSelector(component).attr(ID_ATTRIBUTE);
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getSlotOperationRelatedUuid
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the id that is relevant to be able to perform slot related operations for this components
                 * It typically is {@link componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE} when applicable and defaults to {@link componentHandlerServiceModule.ID_ATTRIBUTE}
                 *
                 * @param {Object} component the yjQuery component for which to get the Uuid
                 *
                 * @return {String} the slot operations related Uuid
                 */
                getSlotOperationRelatedUuid: function(component) {
                    var containerId = this.getFromSelector(component).attr(CONTAINER_ID_ATTRIBUTE);
                    return containerId && this.getFromSelector(component).attr(CONTAINER_TYPE_ATTRIBUTE) ? containerId : this.getFromSelector(component).attr(UUID_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_setType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Sets the smartEdit component type of a given component
                 *
                 * @param {Object} component the yjQuery component for which to set the type
                 * @param {String} type the type to be set
                 */
                setType: function(component, type) {
                    return this.getFromSelector(component).attr(TYPE_ATTRIBUTE, type);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component type of a given component
                 *
                 * @param {Object} component the yjQuery component for which to get the type
                 * 
                 * @return {String} the component type
                 */
                getType: function(component) {
                    return this.getFromSelector(component).attr(TYPE_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getSlotOperationRelatedType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the type that is relevant to be able to perform slot related operations for this components
                 * It typically is {@link componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE} when applicable and defaults to {@link componentHandlerServiceModule.TYPE_ATTRIBUTE}
                 *
                 * @param {Object} component the yjQuery component for which to get the type
                 * 
                 * @return {String} the slot operations related type
                 */
                getSlotOperationRelatedType: function(component) {
                    var containerType = this.getFromSelector(component).attr(CONTAINER_TYPE_ATTRIBUTE);
                    return containerType && this.getFromSelector(component).attr(CONTAINER_ID_ATTRIBUTE) ? containerType : this.getFromSelector(component).attr(TYPE_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getAllComponentsSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery selector matching all smartEdit components that are not of type ContentSlot
                 */
                getAllComponentsSelector: function() {
                    return "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "!='ContentSlot']";
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getAllSlotsSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery selector matching all smartEdit components that are of type ContentSlot
                 */
                getAllSlotsSelector: function() {
                    return "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "='ContentSlot']";
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getParentSlotForComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the the slot ID for a given element
                 * 
                 * @param {Object} the DOM element which represents the component
                 * 
                 * @return {String} the slot ID for that particular component
                 */
                getParentSlotForComponent: function(component) {
                    var parent = component.closest('[' + TYPE_ATTRIBUTE + '=' + CONTENT_SLOT_TYPE + ']');
                    return parent.attr(ID_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getParentSlotUuidForComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the the slot Uuid for a given element
                 * 
                 * @param {Object} the DOM element which represents the component
                 * 
                 * @return {String} the slot Uuid for that particular component
                 */
                getParentSlotUuidForComponent: function(component) {
                    var parent = component.closest('[' + TYPE_ATTRIBUTE + '=' + CONTENT_SLOT_TYPE + ']');
                    return parent.attr(UUID_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOriginalComponentsWithinSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the yjQuery wrapper around a list of smartEdit components contained in the slot identified by the given slotId.
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} slotId the ID of the slot within which the component resides
                 * 
                 * @return {Object} a yjQuery object wrapping the searched components
                 */
                getOriginalComponentsWithinSlot: function(slotId) {
                    return this.getFromSelector(buildComponentsInSlotQuery(slotId));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_isExternalComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Determines whether the component identified by the provided smarteditComponentId and smarteditComponentType
                 * resides in a different catalog version to the one of the current page.  
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * 
                 * @return {Boolean} flag that evaluates to true if the component resides in a catalog version different to 
                 * the one of the current page.  False otherwise. 
                 */
                isExternalComponent: function(smarteditComponentId, smarteditComponentType) {
                    var component = this.getOriginalComponent(smarteditComponentId, smarteditComponentType);
                    var componentCatalogVersionUuid = this.getCatalogVersionUuid(component);

                    return (componentCatalogVersionUuid !== this.getCatalogVersionUUIDFromPage());
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getFirstSmartEditComponentChildren
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * Get first level smartEdit component children for a given node, regardless how deep they are found.
                 * The returned children may have different depths relatively to the parent:
                 * Example: a call on the body would return 4 components with ids: 1,2,3,4
                 * <pre>
                 * <body>
        		 	    <div>
        			        <component smartedit-component-id="1">
        			            <component smartedit-component-id="1_1"></component>
        			        </component>
        			        <component smartedit-component-id="2">
        			            <component smartedit-component-id="2_1"></component>
        			        </component>
        			    </div>

        			    <component smartedit-component-id="3">
        			        <component smartedit-component-id="3_1"></component>
        			    </component>

        			    <div>
        			        <div>
        			            <component smartedit-component-id="4">
        			                <component smartedit-component-id="4_1"></component>
        			            </component>
        			        </div>
        			    </div>
        			</body>
                 * </pre>
                 */
                getFirstSmartEditComponentChildren: function(seComponent) {
                    var stem = this.getFromSelector(seComponent);
                    var parentCssPath = stem.getCssPath();
                    var firstChildrenRegex = new RegExp(COMPONENT_CLASS, 'g');
                    var self = this;
                    return stem.find("." + COMPONENT_CLASS).filter(function() {
                        var match = self.getFromSelector(this).getCssPath().replace(parentCssPath, "").match(firstChildrenRegex);
                        return match && match.length === 1;
                    });
                },

                _getComponentCloneInOverlay: function(component) {
                    var elementUuid = component.attr(ELEMENT_UUID_ATTRIBUTE);
                    return this.getFromSelector("." + OVERLAY_COMPONENT_CLASS + "[" + ELEMENT_UUID_ATTRIBUTE + "='" + elementUuid + "']");
                }
            };

        });
