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
describe('notificationService', function() {
    var notificationService, NotificationServiceInterface, SE_NOTIFICATION_SERVICE_GATEWAY_ID, gatewayProxy;

    /*
     * This setup method creates a mock for the gateway proxy. It is used to validate
     * that the notification service initializes itself properly to be proxied
     * across the two applications.
     */
    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('notificationServiceModule'));

    beforeEach(inject(function(_notificationService_, _NotificationServiceInterface_, _SE_NOTIFICATION_SERVICE_GATEWAY_ID_) {
        notificationService = _notificationService_;

        NotificationServiceInterface = _NotificationServiceInterface_;
        SE_NOTIFICATION_SERVICE_GATEWAY_ID = _SE_NOTIFICATION_SERVICE_GATEWAY_ID_;
    }));

    describe('initialization', function() {
        it('extends the NotificationServiceInterface', function() {
            expect(notificationService instanceof NotificationServiceInterface).toBe(true);
            expect(notificationService.pushNotification).toBeEmptyFunction();
            expect(notificationService.removeNotification).toBeEmptyFunction();
            expect(notificationService.removeAllNotifications).toBeEmptyFunction();
        });

        it('invokes the gatway proxy with the proper parameter values', function() {
            expect(gatewayProxy.initForService).toHaveBeenCalledWith(
                notificationService, ['pushNotification', 'removeNotification', 'removeAllNotifications'],
                SE_NOTIFICATION_SERVICE_GATEWAY_ID);
        });
    });
});
