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
angular.module('notificationMouseLeaveDetectionServiceInterfaceModule', [])
    .constant('SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID', 'SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID')
    /*
     * The interface defines the methods required to detect when the mouse leaves the notification panel
     * in the SmartEdit application and in the SmartEdit container.
     * 
     * It is solely meant to be used with the notificationService.
     */
    .factory('NotificationMouseLeaveDetectionServiceInterface', function() {
        function NotificationMouseLeaveDetectionServiceInterface() {}

        /*
         * This method starts tracking the movement of the mouse pointer in order to detect when it
         * leaves the notification panel.
         * 
         * The innerBounds parameter is considered optional. If it is not provided, it will not be
         * validated and detection will only be started in the SmartEdit container.
         * 
         * Here is an example of a bounds object:
         * 
         * {
         *     x: 100,
         *     y: 100,
         *     width: 200,
         *     height: 50
         * }
         * 
         * This method will throw an error if:
         *     - the bounds parameter is not provided
         *     - a bounds object does not contain the X coordinate
         *     - a bounds object does not contain the Y coordinate
         *     - a bounds object does not contain the width dimension
         *     - a bounds object does not contain the height dimension
         */
        NotificationMouseLeaveDetectionServiceInterface.prototype.startDetection = function() {};

        /*
         * This method stops tracking the movement of the mouse pointer.
         */
        NotificationMouseLeaveDetectionServiceInterface.prototype.stopDetection = function() {};

        /*
         * These two methods are used to start and stop tracking the movement of the mouse pointer within the iFrame.
         */
        NotificationMouseLeaveDetectionServiceInterface.prototype._remoteStartDetection = function() {};
        NotificationMouseLeaveDetectionServiceInterface.prototype._remoteStopDetection = function() {};

        /*
         * This method is used to call the callback function when it is detected from within the iFrame that the mouse
         * left the notification panel 
         */
        NotificationMouseLeaveDetectionServiceInterface.prototype._callCallback = function() {};

        /*
         * This method is triggered when the service has detected that the mouse left the
         * notification panel. It will execute the callback function and stop detection.
         */
        NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseLeave = function() {
            var callbackFn = this._getCallback() || this._callCallback;

            callbackFn();
            this.stopDetection();
        };

        /*
         * This method is called for each mouse movement. It evaluates whether or not the
         * mouse pointer is in the notification panel. If it isn't, it calls the onMouseLeave.
         */
        NotificationMouseLeaveDetectionServiceInterface.prototype._onMouseMove = function(event) {
            var bounds = this._getBounds();
            var isOutsideX = bounds && event && (event.clientX < bounds.x || event.clientX > bounds.x + bounds.width);
            var isOutsideY = bounds && event && (event.clientY < bounds.y || event.clientY > bounds.y + bounds.height);

            if (isOutsideX || isOutsideY) {
                this._onMouseLeave();
            }
        };

        NotificationMouseLeaveDetectionServiceInterface.prototype._getBounds = function() {};

        NotificationMouseLeaveDetectionServiceInterface.prototype._getCallback = function() {};

        return NotificationMouseLeaveDetectionServiceInterface;
    });
