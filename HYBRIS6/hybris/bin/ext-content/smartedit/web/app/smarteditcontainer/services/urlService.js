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
angular.module('urlServiceModule', ['gatewayProxyModule', 'urlServiceInterfaceModule', 'resourceLocationsModule'])
    .factory('urlService', function($location, gatewayProxy, UrlServiceInterface, extend) {

        var UrlService = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this, ['openUrlInPopup', 'path']);
        };

        UrlService = extend(UrlServiceInterface, UrlService);

        UrlService.prototype.openUrlInPopup = function(url) {
            var win = window.open(url, '_blank', 'toolbar=no, scrollbars=yes, resizable=yes');
            win.focus();
        };

        UrlService.prototype.path = function(path) {
            $location.path(path);
        };

        return new UrlService('urlService');
    });
