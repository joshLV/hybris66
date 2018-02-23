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
describe('notificationMouseLeaveDetectionService', function() {
    var DUMMY_X = 0;
    var DUMMY_Y = 0;
    var DUMMY_WIDTH = 100;
    var DUMMY_HEIGHT = 100;
    var DUMMY_BOUNDS = {
        x: DUMMY_X,
        y: DUMMY_Y,
        width: DUMMY_WIDTH,
        height: DUMMY_HEIGHT
    };

    var MOUSE_MOVE_EVENT = 'mousemove';

    var notificationMouseLeaveDetectionService, NotificationMouseLeaveDetectionServiceInterface, SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID, gatewayProxy, $document;

    /*
     * This method prepares a mock for the Gateway Proxy. It is used to test that
     * the service properly initializes itself for proxying across the gateay.
     */
    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('notificationMouseLeaveDetectionServiceModule'));

    beforeEach(inject(function(_notificationMouseLeaveDetectionService_, _NotificationMouseLeaveDetectionServiceInterface_, _SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID_, _$document_) {
        notificationMouseLeaveDetectionService = _notificationMouseLeaveDetectionService_;

        NotificationMouseLeaveDetectionServiceInterface = _NotificationMouseLeaveDetectionServiceInterface_;
        SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID = _SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID_;

        $document = _$document_;
    }));

    describe('initialization', function() {
        it('extends the NotificationMouseLeaveDetectionServiceInterface', function() {
            expect(notificationMouseLeaveDetectionService instanceof NotificationMouseLeaveDetectionServiceInterface).toBe(true);
            expect(notificationMouseLeaveDetectionService.startDetection).toBeEmptyFunction();
            expect(notificationMouseLeaveDetectionService.stopDetection).toBeEmptyFunction();
            expect(notificationMouseLeaveDetectionService._callCallback).toBeEmptyFunction();
        });

        it('intializes the gateway proxy with the proper identifier and list of methods', function() {
            expect(gatewayProxy.initForService).toHaveBeenCalledWith(notificationMouseLeaveDetectionService, ['stopDetection', '_remoteStartDetection', '_remoteStopDetection', '_callCallback'], SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID);
        });
    });

    describe('_remoteStartDetection', function() {
        it('registers a mouse move event listener on the local frame', function() {
            // Given
            spyOn($document[0], 'addEventListener');

            // When
            notificationMouseLeaveDetectionService._remoteStartDetection(DUMMY_BOUNDS, function() {});

            // Then
            expect($document[0].addEventListener).toHaveBeenCalledWith(MOUSE_MOVE_EVENT, notificationMouseLeaveDetectionService._onMouseMove);
        });
    });

    describe('_remoteStopDetection', function() {
        it('un-registers the mouse move event listener on the local frame', function() {
            // Given
            spyOn($document[0], 'removeEventListener');

            // When
            notificationMouseLeaveDetectionService._remoteStopDetection();

            // Then
            expect($document[0].removeEventListener).toHaveBeenCalledWith(MOUSE_MOVE_EVENT, notificationMouseLeaveDetectionService._onMouseMove);
        });

        it('resets the notification panel bounds that were stored', function() {
            // Given
            notificationMouseLeaveDetectionService.startDetection(DUMMY_BOUNDS, function() {});

            // When
            notificationMouseLeaveDetectionService.stopDetection();

            // Then
            expect(notificationMouseLeaveDetectionService._getBounds()).toBeFalsy();
        });
    });

    describe('_getBounds', function() {
        it('returns the bounds that were given when detection was started', function() {
            // Given
            notificationMouseLeaveDetectionService._remoteStartDetection(DUMMY_BOUNDS, function() {});

            // When
            var bounds = notificationMouseLeaveDetectionService._getBounds();

            // Then
            expect(bounds).toEqual(DUMMY_BOUNDS);
        });
    });

    describe('_getCallback', function() {
        it('always returns null', function() {
            // Given
            notificationMouseLeaveDetectionService._remoteStartDetection(DUMMY_BOUNDS, function() {});

            // When
            var callback = notificationMouseLeaveDetectionService._getCallback();

            // Then
            expect(callback).toBeFalsy();
        });
    });
});
