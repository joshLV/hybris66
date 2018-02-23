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
 * @ngdoc overview
 * @name siteServiceModule
 * @description
 * # The siteServiceModule
 *
 * The Site Service module provides a service that fetches all sites that have been configured on the hybris platform.
 */
angular.module('siteServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule', 'eventServiceModule', 'operationContextServiceModule'])

    /**
     * @ngdoc service
     * @name siteServiceModule.service:siteService
     *
     * @description
     * The Site Service fetches all sites configured on the hybris platform using REST calls to the cmswebservices sites API.
     */
    .service('siteService', function(restServiceFactory, systemEventService, operationContextService, OPERATION_CONTEXT, SITES_RESOURCE_URI, EVENTS, $q) {
        var cache = null;
        var siteRestService = restServiceFactory.get(SITES_RESOURCE_URI);
        operationContextService.register(SITES_RESOURCE_URI, OPERATION_CONTEXT.CMS);

        /**
         * @ngdoc method
         * @name siteServiceModule.service:siteService#getSites
         * @methodOf siteServiceModule.service:siteService
         *
         * @description
         * Fetches a list of sites configured on the hybris platform. The list of sites if fetched using REST calls to
         * the cmswebservices sites API.
         *
         * @returns {Array} An array of site descriptors. Each descriptor provides the following site properties: uid,
         * name, previewUrl, and redirectUrl.
         */
        this.getSites = function() {
            return cache ? $q.when(cache) : siteRestService.get().then(function(sitesDTO) {

                var allCatalogs = sitesDTO.sites.reduce(function(catalogs, site) {
                    Array.prototype.push.apply(catalogs, site.contentCatalogs);
                    return catalogs;
                }, []);

                return siteRestService.get({
                    catalogIds: allCatalogs.join(',')
                }).then(function(allSites) {
                    cache = allSites.sites;
                    return cache;
                });

            });
        };
        /**
         * @ngdoc method
         * @name siteServiceModule.service:siteService#getSiteById
         * @methodOf siteServiceModule.service:siteService
         *
         * @description
         * Fetches a site, configured on the hybris platform, by its uid. The sites if fetched using REST calls to
         * the cmswebservices sites API.
         *
         * @returns {object} a site descriptos. a descriptor provides the following site properties: uid,
         * name, previewUrl, and redirectUrl.
         */
        this.getSiteById = function(uid) {
            return this.getSites().then(function(sites) {
                return sites.filter(function(site) {
                    return site.uid === uid;
                })[0];
            });
        };

        this._clearCache = function() {
            cache = null;
        };

        systemEventService.registerEventHandler(EVENTS.AUTHORIZATION_SUCCESS, this._clearCache);
    });
