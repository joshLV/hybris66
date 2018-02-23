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
/**
 * @ngdoc service
 * @name urlServiceInterfaceModule.UrlServiceInterface
 *
 * @description
 * Provides an abstract extensible url service, Used to open a given URL
 * in a new browser url upon invocation. 
 * 
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
angular.module('urlServiceInterfaceModule', ['resourceLocationsModule'])
    .factory('UrlServiceInterface', function(CONTEXT_SITE_ID, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, PAGE_CONTEXT_SITE_ID, PAGE_CONTEXT_CATALOG, PAGE_CONTEXT_CATALOG_VERSION) {

        function UrlServiceInterface() {}


        /** 
         * @ngdoc method
         * @name urlServiceInterfaceModule.UrlServiceInterface#openUrlInPopup
         * @methodOf urlServiceInterfaceModule.UrlServiceInterface
         *
         * @description
         * Opens a given URL in a new browser pop up without authentication.
         *
         * @param {String} url - the URL we wish to open.
         */
        UrlServiceInterface.prototype.openUrlInPopup = function() {};

        /**
         * @ngdoc method
         * @name urlServiceInterfaceModule.UrlServiceInterface#path
         * @methodOf urlServiceInterfaceModule.UrlServiceInterface
         *
         * @description
         * Navigates to the given path in the same browser tab.
         *
         * @param {String} path - the path we wish to navigate to.
         */
        UrlServiceInterface.prototype.path = function() {};


        UrlServiceInterface.prototype.buildUriContext = function(siteId, catalogId, catalogVersion) {
            var uriContext = {};
            uriContext[CONTEXT_SITE_ID] = siteId;
            uriContext[CONTEXT_CATALOG] = catalogId;
            uriContext[CONTEXT_CATALOG_VERSION] = catalogVersion;
            return uriContext;
        };

        UrlServiceInterface.prototype.buildPageUriContext = function(siteId, catalogId, catalogVersion) {
            var uriContext = {};
            uriContext[PAGE_CONTEXT_SITE_ID] = siteId;
            uriContext[PAGE_CONTEXT_CATALOG] = catalogId;
            uriContext[PAGE_CONTEXT_CATALOG_VERSION] = catalogVersion;
            return uriContext;
        };

        return UrlServiceInterface;
    });
