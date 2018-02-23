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

        NotificationMouseLeaveDetectionService.prototype._remoteStartDetection = function(innerBounds) {
            notificationPanelBounds = innerBounds;

            $document[0].addEventListener('mousemove', this._onMouseMove);
        };

        NotificationMouseLeaveDetectionService.prototype._remoteStopDetection = function() {
            $document[0].removeEventListener('mousemove', this._onMouseMove);

            notificationPanelBounds = null;
        };

        NotificationMouseLeaveDetectionService.prototype._getBounds = function() {
            return notificationPanelBounds;
        };

        NotificationMouseLeaveDetectionService.prototype._getCallback = function() {
            return mouseLeaveCallback;
        };

        return new NotificationMouseLeaveDetectionService();
    });
