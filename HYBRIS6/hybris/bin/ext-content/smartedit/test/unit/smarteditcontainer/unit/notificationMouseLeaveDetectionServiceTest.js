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
    var DUMMY_OUTER_BOUNDS = {
        x: DUMMY_X,
        y: DUMMY_Y,
        width: DUMMY_WIDTH,
        height: DUMMY_HEIGHT
    };
    var DUMMY_INNER_BOUNDS = DUMMY_OUTER_BOUNDS;

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
            expect(notificationMouseLeaveDetectionService._remoteStartDetection).toBeEmptyFunction();
            expect(notificationMouseLeaveDetectionService._remoteStopDetection).toBeEmptyFunction();
        });

        it('intializes the gateway proxy with the proper identifier and list of methods', function() {
            expect(gatewayProxy.initForService).toHaveBeenCalledWith(notificationMouseLeaveDetectionService, ['stopDetection', '_remoteStartDetection', '_remoteStopDetection', '_callCallback'], SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID);
        });
    });

    describe('validateBounds', function() {
        it('throws an error when the bounds object is not provided', function() {
            expect(function() {
                notificationMouseLeaveDetectionService.startDetection();
            }).toThrow('Bounds are required for mouse leave detection');
        });

        it('throws an error if the bounds object does not contain an x coordinate', function() {
            expect(function() {
                notificationMouseLeaveDetectionService.startDetection({
                    y: DUMMY_Y,
                    width: DUMMY_WIDTH,
                    height: DUMMY_HEIGHT
                });
            }).toThrow('Bounds must contain the x coordinate');
        });

        it('throws an error if the bounds object does not contain a y coordinate', function() {
            expect(function() {
                notificationMouseLeaveDetectionService.startDetection({
                    x: DUMMY_X,
                    width: DUMMY_WIDTH,
                    height: DUMMY_HEIGHT
                });
            }).toThrow('Bounds must contain the y coordinate');
        });

        it('throws an error if the bounds object does not contain the width dimension', function() {
            expect(function() {
                notificationMouseLeaveDetectionService.startDetection({
                    x: DUMMY_X,
                    y: DUMMY_Y,
                    height: DUMMY_HEIGHT
                });
            }).toThrow('Bounds must contain the width dimension');
        });

        it('throws an error if the bounds object does not contain the height dimension', function() {
            expect(function() {
                notificationMouseLeaveDetectionService.startDetection({
                    x: DUMMY_X,
                    y: DUMMY_Y,
                    width: DUMMY_WIDTH
                });
            }).toThrow('Bounds must contain the height dimension');
        });
    });

    describe('startDetection', function() {
        it('throws an error if the callback function is not provided', function() {
            expect(function() {
                notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS);
            }).toThrow('Callback function is required');
        });

        it('registers a mouse move event listener on the local frame', function() {
            // Given
            spyOn($document[0], 'addEventListener');

            // When
            notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS, DUMMY_INNER_BOUNDS, function() {});

            // Then
            expect($document[0].addEventListener).toHaveBeenCalledWith(MOUSE_MOVE_EVENT, notificationMouseLeaveDetectionService._onMouseMove);
        });

        it('starts detection in the remote frame if the inner bounds are given', function() {
            // Given
            spyOn(notificationMouseLeaveDetectionService, '_remoteStartDetection');

            // When
            notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS, DUMMY_INNER_BOUNDS, function() {});

            // Then
            expect(notificationMouseLeaveDetectionService._remoteStartDetection).toHaveBeenCalledWith(DUMMY_INNER_BOUNDS);
        });

        it('never starts detection in the remote frame if the inner bounds are not given', function() {
            // Given
            spyOn(notificationMouseLeaveDetectionService, '_remoteStartDetection');

            // When
            notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS, null, function() {});

            // Then
            expect(notificationMouseLeaveDetectionService._remoteStartDetection).not.toHaveBeenCalled();
        });
    });

    describe('stopDetection', function() {
        it('un-registers the mouse move event listener on the local frame', function() {
            // Given
            spyOn($document[0], 'removeEventListener');

            // When
            notificationMouseLeaveDetectionService.stopDetection();

            // Then
            expect($document[0].removeEventListener).toHaveBeenCalledWith(MOUSE_MOVE_EVENT, notificationMouseLeaveDetectionService._onMouseMove);
        });

        it('stops detection in the remote frame', function() {
            // Given
            spyOn(notificationMouseLeaveDetectionService, '_remoteStopDetection');

            // When
            notificationMouseLeaveDetectionService.stopDetection();

            // Then
            expect(notificationMouseLeaveDetectionService._remoteStopDetection).toHaveBeenCalled();
        });

        it('resets the notification panel bounds that were stored', function() {
            // Given
            notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS, DUMMY_INNER_BOUNDS, function() {});

            // When
            notificationMouseLeaveDetectionService.stopDetection();

            // Then
            expect(notificationMouseLeaveDetectionService._getBounds()).toBeFalsy();
        });

        it('resets the mouse leave callback that was stored', function() {
            // Given
            notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS, DUMMY_INNER_BOUNDS, function() {});

            // When
            notificationMouseLeaveDetectionService.stopDetection();

            // Then
            expect(notificationMouseLeaveDetectionService._getCallback()).toBeFalsy();
        });
    });

    describe('_callCallback', function() {
        it('calls the mouse leave callback', function() {
            // Given
            var callback = jasmine.createSpy('callback');

            spyOn(notificationMouseLeaveDetectionService, '_getCallback').and.returnValue(callback);

            // When
            notificationMouseLeaveDetectionService._callCallback();

            // Then
            expect(callback).toHaveBeenCalled();
        });
    });

    describe('_getBounds', function() {
        it('returns the bounds that were given when detection was started', function() {
            // Given
            notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS, DUMMY_INNER_BOUNDS, function() {});

            // When
            var bounds = notificationMouseLeaveDetectionService._getBounds();

            // Then
            expect(bounds).toEqual(DUMMY_OUTER_BOUNDS);
        });
    });

    describe('_getCallback', function() {
        it('returns the callback that was given when detection was started', function() {
            // Given
            var expectedCallback = function() {};

            notificationMouseLeaveDetectionService.startDetection(DUMMY_OUTER_BOUNDS, DUMMY_INNER_BOUNDS, expectedCallback);

            // When
            var actualCallback = notificationMouseLeaveDetectionService._getCallback();

            // Then
            expect(expectedCallback).toEqual(actualCallback);
        });
    });
});
