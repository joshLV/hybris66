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
 * @name notificationServiceModule
 * 
 * @description
 * The notification module provides a service to display visual cues to inform
 * the user of the state of the application in the container or the iFramed application.
 */
angular.module('notificationServiceModule', [
        'notificationServiceInterfaceModule',
        'gatewayProxyModule',
        'eventServiceModule',
        'yLoDashModule',
        'functionsModule'
    ])

    /**
     * @ngdoc object
     * @name notificationServiceModule.object:EVENT_NOTIFICATION_CHANGED
     * 
     * @description
     * The ID of the event that is triggered when a notification is pushed or removed.
     */
    .constant('EVENT_NOTIFICATION_CHANGED', 'EVENT_NOTIFICATION_CHANGED')

    /**
     * @ngdoc service
     * @name notificationServiceModule.service:notificationService
     * 
     * @description
     * The notification service is used to display visual cues to inform the user of the state of the application.
     */
    .factory('notificationService', function(NotificationServiceInterface, SE_NOTIFICATION_SERVICE_GATEWAY_ID, EVENT_NOTIFICATION_CHANGED, gatewayProxy, lodash, systemEventService, extend) {
        var NotificationService = function() {
            gatewayProxy.initForService(this, ['pushNotification', 'removeNotification', 'removeAllNotifications'], SE_NOTIFICATION_SERVICE_GATEWAY_ID);
        };

        NotificationService = extend(NotificationServiceInterface, NotificationService);

        var notifications = [];

        var validate = function(configuration) {
            if (!configuration) {
                throw 'notificationService.pushNotification: Configuration is required';
            }

            if (!configuration.hasOwnProperty('id')) {
                throw 'notificationService.pushNotification: Configuration must contain an ID';
            }

            if (configuration.id.length < 1) {
                throw 'notificationService.pushNotification: Notification ID cannot be an empty string';
            }

            if (!configuration.hasOwnProperty('template') && !configuration.hasOwnProperty('templateUrl')) {
                throw 'notificationService.pushNotification: Configuration must contain a template or template URL';
            }

            if (configuration.hasOwnProperty('template') && configuration.hasOwnProperty('templateUrl')) {
                throw 'notificationService.pushNotification: Configuration cannot contain both a template and template URL; use one or the other';
            }
        };

        NotificationService.prototype.pushNotification = function(configuration) {
            validate(configuration);

            if (this.getNotification(configuration.id)) {
                throw 'notificationService.pushNotification: Notification already exists with ID "' + configuration.id + '"';
            }

            notifications.push(lodash.clone(configuration));

            systemEventService.sendAsynchEvent(EVENT_NOTIFICATION_CHANGED);
        };

        NotificationService.prototype.removeNotification = function(notificationId) {
            lodash.remove(notifications, function(notification) {
                return notification.id === notificationId;
            });

            systemEventService.sendAsynchEvent(EVENT_NOTIFICATION_CHANGED);
        };

        NotificationService.prototype.removeAllNotifications = function() {
            notifications = [];

            systemEventService.sendAsynchEvent(EVENT_NOTIFICATION_CHANGED);
        };

        NotificationService.prototype.isNotificationDisplayed = function(notificationId) {
            return !!this.getNotification(notificationId);
        };

        NotificationService.prototype.getNotification = function(notificationId) {
            return lodash.find(notifications, ['id', notificationId]);
        };

        NotificationService.prototype.getNotifications = function() {
            var clonedNotifications = lodash.clone(notifications);
            return lodash.reverse(clonedNotifications);
        };

        return new NotificationService();
    });
