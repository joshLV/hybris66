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
angular.module('featureServiceModule', ['functionsModule', 'featureInterfaceModule', 'gatewayProxyModule', 'toolbarModule'])

    .factory('featureService', function(extend, copy, FeatureServiceInterface, gatewayProxy, toolbarServiceFactory) {

        /////////////////////////////////////
        // PerspectiveService Prototype
        /////////////////////////////////////

        var FeatureService = function() {
            this.features = [];
            this.gatewayId = "featureService";
            gatewayProxy.initForService(this, ['_registerAliases', 'addToolbarItem', 'register', 'enable', 'disable', '_remoteEnablingFromInner', '_remoteDisablingFromInner', 'addDecorator', 'getFeatureProperty', 'addContextualMenuButton']);
        };

        FeatureService = extend(FeatureServiceInterface, FeatureService);

        FeatureService.prototype._registerAliases = function(configuration) {

            var feature = this.features.filter(function(feature) {
                return feature.key === configuration.key;
            })[0];
            if (!feature) {
                configuration.id = btoa(configuration.key);
                this.features.push(configuration);
            }
        };

        FeatureService.prototype.getFeatureProperty = function(featureKey, propertyName) {
            var feature = this.features.filter(function(feature) {
                return feature.key === featureKey;
            })[0];

            return feature ? feature[propertyName] : null;
        };

        FeatureService.prototype.getFeatureKeys = function() {
            return this.features.map(function(feature) {
                return feature.key;
            });
        };

        FeatureService.prototype.addToolbarItem = function(configuration) {

            var toolbar = toolbarServiceFactory.getToolbarService(configuration.toolbarId);
            configuration.enablingCallback = function() {
                this.addItems([configuration]);
            }.bind(toolbar);

            configuration.disablingCallback = function() {
                this.removeItemByKey(configuration.key);
            }.bind(toolbar);

            this.register(configuration);
        };
        return new FeatureService();

    });
