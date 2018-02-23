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
angular.module('notificationMouseLeaveDetectionServiceModule', [
        'notificationMouseLeaveDetectionServiceInterfaceModule',
        'gatewayProxyModule',
        'functionsModule'
    ])
    /*
     * This service makes it possible to track the mouse position to detect when it leaves the
     * notification panel.
     * 
     * It is solely meant to be used with the notificationService.
     */
    .factory('notificationMouseLeaveDetectionService', function(NotificationMouseLeaveDetectionServiceInterface, SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID, gatewayProxy, $document, extend) {
        var notificationPanelBounds = null;
        var mouseLeaveCallback = null;

        var NotificationMouseLeaveDetectionService = function() {
            gatewayProxy.initForService(this, ['stopDetection', '_remoteStartDetection', '_remoteStopDetection', '_callCallback'], SE_NOTIFICATION_MOUSE_LEAVE_DETECTION_SERVICE_GATEWAY_ID);

            /*
             * We need to bind the function in order for it to execute within the service's
             * scope and store it to be able to un-register the listener.
             */
            this._onMouseMove = this._onMouseMove.bind(this);
        };

        NotificationMouseLeaveDetectionService = extend(NotificationMouseLeaveDetectionServiceInterface, NotificationMouseLeaveDetectionService);

        var validateBounds = function(bounds) {
            if (!bounds) {
                throw 'Bounds are required for mouse leave detection';
            }

            if (!bounds.hasOwnProperty('x')) {
                throw 'Bounds must contain the x coordinate';
            }

            if (!bounds.hasOwnProperty('y')) {
                throw 'Bounds must contain the y coordinate';
            }

            if (!bounds.hasOwnProperty('width')) {
                throw 'Bounds must contain the width dimension';
            }

            if (!bounds.hasOwnProperty('height')) {
                throw 'Bounds must contain the height dimension';
            }
        };

        NotificationMouseLeaveDetectionService.prototype.startDetection = function(outerBounds, innerBounds, callback) {
            validateBounds(outerBounds);

            if (!callback) {
                throw 'Callback function is required';
            }

            notificationPanelBounds = outerBounds;
            mouseLeaveCallback = callback;

            $document[0].addEventListener('mousemove', this._onMouseMove);

            if (innerBounds) {
                validateBounds(innerBounds);

                this._remoteStartDetection(innerBounds);
            }
        };

        NotificationMouseLeaveDetectionService.prototype.stopDetection = function() {
            $document[0].removeEventListener('mousemove', this._onMouseMove);

            notificationPanelBounds = null;
            mouseLeaveCallback = null;

            this._remoteStopDetection();
        };

        NotificationMouseLeaveDetectionService.prototype._callCallback = function() {
            var callback = this._getCallback();

            if (callback) {
                callback();
            }
        };

        NotificationMouseLeaveDetectionService.prototype._getBounds = function() {
            return notificationPanelBounds;
        };

        NotificationMouseLeaveDetectionService.prototype._getCallback = function() {
            return mouseLeaveCallback;
        };

        return new NotificationMouseLeaveDetectionService();
    });
