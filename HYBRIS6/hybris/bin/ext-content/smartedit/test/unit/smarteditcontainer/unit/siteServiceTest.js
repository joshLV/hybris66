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
describe('siteService', function() {

    var siteService;
    var systemEventService;
    var siteRestService, $q, $rootScope;

    var EVENTS = {
        AUTHORIZATION_SUCCESS: 'AUTHORIZATION_SUCCESS',
        LOGOUT: 'SE_LOGOUT_EVENT'
    };

    beforeEach(function() {
        angular.module('resourceLocationsModule', []);
    });

    beforeEach(module('operationContextServiceModule', function($provide) {
        var operationContextService = jasmine.createSpyObj('operationContextService', ['register']);
        var OPERATION_CONTEXT = {};
        $provide.value('operationContextService', operationContextService);
        $provide.value('OPERATION_CONTEXT', OPERATION_CONTEXT);
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        siteRestService = jasmine.createSpyObj('siteRestService', ['get']);
        var restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.and.returnValue(siteRestService);
        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('eventServiceModule', function($provide) {
        systemEventService = jasmine.createSpyObj('systemEventService', ['registerEventHandler']);
        systemEventService.registerEventHandler.and.returnValue(null);
        $provide.value('systemEventService', systemEventService);
        $provide.value('SITES_RESOURCE_URI', 'some uri');
        $provide.value('EVENTS', EVENTS);
    }));

    beforeEach(module('siteServiceModule'));

    beforeEach(inject(function(_siteService_, _$q_, _$rootScope_) {
        siteService = _siteService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    it('GIVEN sites REST call fails WHEN I request the list of sites THEN I will receive a rejected promise', function() {
        // GIVEN
        siteRestCallFails();

        // WHEN
        var promise = siteService.getSites();

        // THEN
        expect(promise).toBeRejected();
    });

    it('GIVEN site REST call succeeds WHEN I request the list of sites THEN I will receive a promise that will resolve to a list of sites', function() {
        // GIVEN
        sitesRESTCallSucceeds();

        // WHEN
        var promise = siteService.getSites();

        // THEN
        expect(promise).toBeResolvedWithData([{
            previewUrl: "/yacceleratorstorefront?site=electronics",
            name: {
                en: 'Electronics'
            },
            uid: "electronics"
        }, {
            previewUrl: "/yacceleratorstorefront?site=apparel",
            name: {
                en: 'Apparel'
            },
            uid: "apparel"
        }]);

    });

    it('GIVEN site REST call succeeds the first time to fetch all sites and all sites associated to the content catalogs WHEN I request sites for subsequently THEN I will receive a promise that resolves to a cached list of sites AND the rest service will not be called again', function() {
        // GIVEN
        sitesRESTCallSucceeds();

        // WHEN
        siteService.getSites();
        $rootScope.$digest();
        var promise = siteService.getSites();
        $rootScope.$digest();

        // THEN
        expect(promise).toBeResolvedWithData([{
            previewUrl: "/yacceleratorstorefront?site=electronics",
            name: {
                en: 'Electronics'
            },
            uid: "electronics"
        }, {
            previewUrl: "/yacceleratorstorefront?site=apparel",
            name: {
                en: 'Apparel'
            },
            uid: "apparel"
        }]);

        expect(siteRestService.get.calls.count()).toEqual(2);
    });

    it('GIVEN site REST call succeeds WHEN I request a site by id, the promise will resolve to the expected site', function() {
        // GIVEN
        sitesRESTCallSucceeds();

        // WHEN
        var promise = siteService.getSiteById("electronics");

        // THEN
        expect(promise).toBeResolvedWithData({
            previewUrl: "/yacceleratorstorefront?site=electronics",
            name: {
                en: 'Electronics'
            },
            uid: "electronics"
        });
    });

    it('WHEN the site service is created THEN the right event listeners are registered', function() {
        // THEN
        expect(systemEventService.registerEventHandler.calls.count()).toEqual(1);
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.AUTHORIZATION_SUCCESS, siteService._clearCache);
    });

    // Helper functions
    function sitesRESTCallSucceeds() {
        siteRestService.get.and.returnValue($q.when({
            sites: [{
                previewUrl: "/yacceleratorstorefront?site=electronics",
                name: {
                    en: 'Electronics'
                },
                uid: "electronics"
            }, {
                previewUrl: "/yacceleratorstorefront?site=apparel",
                name: {
                    en: 'Apparel'
                },
                uid: "apparel"
            }]
        }));
    }

    function siteRestCallFails() {
        siteRestService.get.and.returnValue($q.reject());
    }

});
