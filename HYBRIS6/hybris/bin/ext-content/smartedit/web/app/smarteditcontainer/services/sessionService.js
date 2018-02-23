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
angular.module('sessionServiceModule', [
        'functionsModule',
        'gatewayProxyModule',
        'restServiceFactoryModule',
        'sessionServiceInterfaceModule'
    ])

    .factory('sessionService', function(
        $q,
        extend,
        gatewayProxy,
        restServiceFactory,
        SessionServiceInterface,
        WHO_AM_I_RESOURCE_URI
    ) {

        var SessionService = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this, ['getCurrentUsername', 'getCurrentUserDisplayName', 'hasUserChanged', 'resetCurrentUserData', 'setCurrentUsername']);
            this.resetCurrentUserData();
        };

        var _previousUsername = null;
        var _currentUserData;

        var _whoAmIResource = restServiceFactory.get(WHO_AM_I_RESOURCE_URI);

        var _updateCurrentUserData = function() {
            return _whoAmIResource.get().then(function(currentUserData) {
                _currentUserData = currentUserData;
            });
        };

        SessionService = extend(SessionServiceInterface, SessionService);

        SessionService.prototype.getCurrentUserDisplayName = function() {
            return ((this.isUserLoggedIn()) ? $q.when() : _updateCurrentUserData()).then(function() {
                return _currentUserData.displayName;
            });
        };

        SessionService.prototype.getCurrentUsername = function() {
            return ((this.isUserLoggedIn()) ? $q.when() : _updateCurrentUserData()).then(function() {
                return _currentUserData.uid;
            });
        };

        SessionService.prototype.hasUserChanged = function() {
            this.resetCurrentUserData();
            return this.getCurrentUsername().then(function(currentUsername) {
                return (!!_previousUsername && _previousUsername !== currentUsername);
            });
        };

        SessionService.prototype.isUserLoggedIn = function() {
            return (!!_currentUserData);
        };

        SessionService.prototype.resetCurrentUserData = function() {
            _currentUserData = null;
        };

        SessionService.prototype.setCurrentUsername = function(currentUsername) {
            _previousUsername = currentUsername;
        };

        return new SessionService("SessionServiceId");

    });
