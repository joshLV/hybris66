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
describe('NotificationMouseLeaveDetectionServiceInterface', function() {
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

    var NotificationMouseLeaveDetectionServiceInterface;

    beforeEach(module('notificationMouseLeaveDetectionServiceInterfaceModule'));

    beforeEach(inject(function(_NotificationMouseLeaveDetectionServiceInterface_) {
        NotificationMouseLeaveDetectionServiceInterface = _NotificationMouseLeaveDetectionServiceInterface_;
    }));

    describe('initialization', function() {
        it('registers unimplemented methods', function() {
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype.startDetection).toBeEmptyFunction();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype.stopDetection).toBeEmptyFunction();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._remoteStartDetection).toBeEmptyFunction();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._remoteStopDetection).toBeEmptyFunction();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._callCallback).toBeEmptyFunction();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._getBounds).toBeEmptyFunction();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._getCallback).toBeEmptyFunction();
        });
    });

    describe('_onMouseLeave', function() {
        it('calls the callback function if it is defined locally', function() {
            // Given
            var callback = jasmine.createSpy('callback');

            spyOn(NotificationMouseLeaveDetectionServiceInterface.prototype, '_getCallback').and.returnValue(callback);
            spyOn(NotificationMouseLeaveDetectionServiceInterface.prototype, '_callCallback');

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave();

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._getCallback).toHaveBeenCalled();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._callCallback).not.toHaveBeenCalled();
            expect(callback).toHaveBeenCalled();
        });

        it('calls the callback function remotely if it is not defined locally', function() {
            // Given
            spyOn(NotificationMouseLeaveDetectionServiceInterface.prototype, '_getCallback').and.returnValue(undefined);
            spyOn(NotificationMouseLeaveDetectionServiceInterface.prototype, '_callCallback');

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave();

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._getCallback).toHaveBeenCalled();
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._callCallback).toHaveBeenCalled();
        });

        it('stops detection', function() {
            // Given
            spyOn(NotificationMouseLeaveDetectionServiceInterface.prototype, 'stopDetection');

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave();

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype.stopDetection).toHaveBeenCalled();
        });
    });

    describe('_onMouseMove', function() {
        /*
         * This setup method returns creates a spy on the interface's _getBounds method that returns
         * a dummy bounds object and a spy on the interface's _onMouseLeave method to avoid having to
         * create them in each individual test case.
         */
        beforeEach(function() {
            spyOn(NotificationMouseLeaveDetectionServiceInterface.prototype, '_getBounds').and.returnValue(DUMMY_BOUNDS);
            spyOn(NotificationMouseLeaveDetectionServiceInterface.prototype, '_onMouseLeave');
        });

        it('never calls _onMouseLeave if bounds are undefined', function() {
            // Given
            NotificationMouseLeaveDetectionServiceInterface.prototype._getBounds.and.returnValue(undefined);

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove();

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave).not.toHaveBeenCalled();
        });

        it('never calls _onMouseLeave when the mouse pointer is within the notification panel', function() {
            // Given
            var event = {
                clientX: DUMMY_BOUNDS.x,
                clientY: DUMMY_BOUNDS.y
            };

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove(event);

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave).not.toHaveBeenCalled();
        });

        it('calls _onMouseLeave when the mouse pointer is outside the notification panel on both the X and Y axes', function() {
            // Given
            var event = {
                clientX: DUMMY_BOUNDS.x + DUMMY_BOUNDS.width + 1,
                clientY: DUMMY_BOUNDS.y + DUMMY_BOUNDS.height + 1
            };

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove(event);

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave).toHaveBeenCalled();
        });

        it('calls _onMouseLeave when the mouse pointer is outside the notification panel to the left but within it on the Y axis', function() {
            // Given
            var event = {
                clientX: DUMMY_BOUNDS.x - 1,
                clientY: DUMMY_BOUNDS.y
            };

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove(event);

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave).toHaveBeenCalled();
        });

        it('calls _onMouseLeave when the mouse pointer is outside the notification panel to the right but within it on the Y axis', function() {
            // Given
            var event = {
                clientX: DUMMY_BOUNDS.x + DUMMY_BOUNDS.width + 1,
                clientY: DUMMY_BOUNDS.y
            };

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove(event);

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave).toHaveBeenCalled();
        });

        it('calls _onMouseLeave when the mouse pointer is outside the notification panel above it but within it on the X axis', function() {
            // Given
            var event = {
                clientX: DUMMY_BOUNDS.x,
                clientY: DUMMY_BOUNDS.y - 1
            };

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove(event);

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave).toHaveBeenCalled();
        });

        it('calls _onMouseLeave when the mouse pointer is outside the notification panel below it but within it on the X axis', function() {
            // Given
            var event = {
                clientX: DUMMY_BOUNDS.x,
                clientY: DUMMY_BOUNDS.y + DUMMY_BOUNDS.height + 1
            };

            // When
            NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove(event);

            // Then
            expect(NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave).toHaveBeenCalled();
        });
    });
});
