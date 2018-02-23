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
 * @name seConstantsModule
 * @description
 * The seConstantsModule module contains Smartedit's global constants.
 */
angular.module('seConstantsModule', [])
    .constant('DATE_CONSTANTS', {
        ANGULAR_FORMAT: 'short',
        MOMENT_FORMAT: 'M/D/YY h:mm A',
        MOMENT_ISO: 'YYYY-MM-DDTHH:mm:00ZZ',
        ISO: 'yyyy-MM-ddTHH:mm:00Z'
    })
    /**
     * @ngdoc object
     * @name seConstantsModule.object:EVENT_CONTENT_CATALOG_UPDATE
     * @description
     * The ID of the event that is triggered when the content of a catalog is
     * updated (by page edit or page deletion).
     */
    .constant('EVENT_CONTENT_CATALOG_UPDATE', 'EVENT_CONTENT_CATALOG_UPDATE')
    /**
     * @ngdoc object
     * @name seConstantsModule.object:EVENT_PERSPECTIVE_CHANGED
     * @description
     * The ID of the event that is triggered when the perspective (known as mode for users) is changed.
     */
    .constant('EVENT_PERSPECTIVE_CHANGED', 'EVENT_PERSPECTIVE_CHANGED')
    /**
     * @ngdoc object
     * @name seConstantsModule.object:EVENT_PERSPECTIVE_ADDED
     * @description
     * The ID of the event that is triggered when a new perspective (known as mode for users) is registered.
     */
    .constant('EVENT_PERSPECTIVE_ADDED', 'EVENT_PERSPECTIVE_ADDED')
    /**
     * @ngdoc object
     * @name seConstantsModule.object:EVENT_PERSPECTIVE_UNLOADING
     * @description
     * The ID of the event that is triggered when a perspective is about to be unloaded.
     * This event is triggered immediately before the features are disabled.
     */
    .constant('EVENT_PERSPECTIVE_UNLOADING', 'EVENT_PERSPECTIVE_UNLOADING')
    /**
     * @ngdoc object
     * @name seConstantsModule.object:EVENT_PERSPECTIVE_REFRESHED
     * @description
     * The ID of the event that is triggered when the perspective (known as mode for users) is refreshed.
     */
    .constant('EVENT_PERSPECTIVE_REFRESHED', 'EVENT_PERSPECTIVE_REFRESHED')
    /**
     * @ngdoc object
     * @name seConstantsModule.object:ALL_PERSPECTIVE
     * @description
     * The key of the default All Perspective.
     */
    .constant('ALL_PERSPECTIVE', 'se.all')
    /**
     * @ngdoc object
     * @name seConstantsModule.object:NONE_PERSPECTIVE
     * @description
     * The key of the default None Perspective.
     */
    .constant('NONE_PERSPECTIVE', 'se.none')
    /**
     * @ngdoc object
     * @name seConstantsModule.object:VALIDATION_MESSAGE_TYPES
     * @description
     * Validation message types
     */
    .constant('VALIDATION_MESSAGE_TYPES', {
        /**
         * @ngdoc property
         * @name seConstantsModule.object:VALIDATION_MESSAGE_TYPES#VALIDATION_ERROR
         * @propertyOf seConstantsModule.object:VALIDATION_MESSAGE_TYPES
         * @description
         * Validation error type.
         */
        VALIDATION_ERROR: 'ValidationError',
        /**
         * @ngdoc property
         * @name seConstantsModule.object:VALIDATION_MESSAGE_TYPES#WARNING
         * @propertyOf seConstantsModule.object:VALIDATION_MESSAGE_TYPES
         * @description
         * Validation warning type.
         */
        WARNING: 'Warning'
    })
    .constant("CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS", {
        PROCESS_COMPONENTS: 'contractChangeListenerProcessComponents',
        RESTART_PROCESS: 'contractChangeListenerRestartProcess'
    })
    .constant("SMARTEDIT_COMPONENT_PROCESS_STATUS", "smartEditComponentProcessStatus")
    .constant("CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS", {
        PROCESS: "processComponent",
        REMOVE: "removeComponent",
        KEEP_VISIBLE: "keepComponentVisible"
    })
    /**
     * @ngdoc object
     * @name seConstantsModule.object:SORT_DIRECTIONS
     * @description
     * Sort directions
     */
    .constant('SORT_DIRECTIONS', {
        /**
         * @ngdoc property
         * @name seConstantsModule.object:SORT_DIRECTIONS#ASC
         * @propertyOf seConstantsModule.object:SORT_DIRECTIONS
         * @description
         * Sort direction - Ascending
         */
        ASC: 'asc',
        /**
         * @ngdoc property
         * @name seConstantsModule.object:SORT_DIRECTIONS#DESC
         * @propertyOf seConstantsModule.object:SORT_DIRECTIONS
         * @description
         * Sort direction - Descending
         */
        DESC: 'desc'
    })
    /**
     * @ngdoc object
     * @name seConstantsModule.object:EVENT_OUTER_FRAME_CLICKED
     * @description
     * The event that triggeres when user clicks on the outer frame.
     */
    .constant('EVENT_OUTER_FRAME_CLICKED', 'EVENT_OUTER_FRAME_CLICKED')
    .constant('DISABLE_COMPONENT_DECORATOR', 'DISABLE_COMPONENT_DECORATOR')
    .constant('ENABLE_COMPONENT_DECORATOR', 'ENABLE_COMPONENT_DECORATOR');
