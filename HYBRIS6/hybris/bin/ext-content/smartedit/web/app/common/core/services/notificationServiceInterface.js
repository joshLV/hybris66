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
 * @name notificationServiceInterfaceModule
 * 
 * @description
 * The notification module provides a service to display visual cues to inform
 * the user of the state of the application in the container or the iFramed application.
 */
angular.module('notificationServiceInterfaceModule', [])

    /**
     * @ngdoc object
     * @name notificationServiceInterfaceModule.object:SE_NOTIFICATION_SERVICE_GATEWAY_ID
     * 
     * @description
     * The gateway UID used to proxy the NotificationService.
     */
    .constant('SE_NOTIFICATION_SERVICE_GATEWAY_ID', 'SE_NOTIFICATION_SERVICE_GATEWAY_ID')

    /**
     * @ngdoc service
     * @name notificationServiceInterfaceModule.service:NotificationServiceInterface
     * 
     * @description
     * The interface defines the methods required to manage notifications that are to be displayed to the user.
     */
    .factory('NotificationServiceInterface', function() {
        function NotificationServiceInterface() {}

        /**
         * @ngdoc method
         * @name notificationServiceInterfaceModule.service:NotificationServiceInterface#pushNotification
         * @methodOf notificationServiceInterfaceModule.service:NotificationServiceInterface
         * 
         * @description
         * This method creates a new notification based on the given configuration and
         * adds it to the top of the list.
         * 
         * The configuration must contain either a template or template URL, but not both.
         * 
         * @param {Object} configuration The notification's configuration.
         * @param {String} configuration.id The notification's unique identifier.
         * @param {String=} configuration.template The notification's HTML template.
         * @param {String=} configuration.templateUrl The notification's template URL.
         * 
         * @throws An error if no configuration is given.
         * @throws An error if the configuration does not contain a unique identifier.
         * @throws An error if the configuration's unique identifier is an empty string.
         * @throws An error if the configuration does not contain a template or templateUrl.
         * @throws An error if the configuration contains both a template and template Url.
         */
        NotificationServiceInterface.prototype.pushNotification = function() {};

        /**
         * @ngdoc method
         * @name notificationServiceInterfaceModule.service:NotificationServiceInterface#removeNotification
         * @methodOf notificationServiceInterfaceModule.service:NotificationServiceInterface
         * 
         * @description
         * This method removes the notification with the given ID from the list.
         * 
         * @param {String} notificationId The notification's unique identifier.
         */
        NotificationServiceInterface.prototype.removeNotification = function() {};

        /**
         * @ngdoc method
         * @name notificationServiceInterfaceModule.service:NotificationServiceInterface#removeAllNotifications
         * @methodOf notificationServiceInterfaceModule.service:NotificationServiceInterface
         * 
         * @description
         * This method removes all notifications.
         */
        NotificationServiceInterface.prototype.removeAllNotifications = function() {};

        return NotificationServiceInterface;
    });
