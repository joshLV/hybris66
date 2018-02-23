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
        'notificationMouseLeaveDetectionServiceModule',
        'gatewayProxyModule',
        'functionsModule'
    ])

    /**
     * @ngdoc service
     * @name notificationServiceModule.service:notificationService
     * 
     * @description
     * The notification service is used to display visual cues to inform the user of the state of the application.
     */
    .factory('notificationService', function(NotificationServiceInterface, SE_NOTIFICATION_SERVICE_GATEWAY_ID, notificationMouseLeaveDetectionService, gatewayProxy, extend) {
        var NotificationService = function() {
            gatewayProxy.initForService(this, ['pushNotification', 'removeNotification', 'removeAllNotifications'], SE_NOTIFICATION_SERVICE_GATEWAY_ID);
        };

        NotificationService = extend(NotificationServiceInterface, NotificationService);

        return new NotificationService();
    });
