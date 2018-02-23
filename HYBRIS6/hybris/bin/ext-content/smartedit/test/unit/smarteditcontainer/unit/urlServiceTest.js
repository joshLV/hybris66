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
describe('test urlService', function() {

    var urlService, gatewayProxy;

    beforeEach(function() {
        module('urlServiceInterfaceModule');
        angular.module('resourceLocationsModule', []);
    });

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);

        $provide.value('CONTEXT_SITE_ID', 'some site id');
        $provide.value('CONTEXT_CATALOG', 'some context catalog');
        $provide.value('CONTEXT_CATALOG_VERSION', 'some catalog version');
        $provide.value('PAGE_CONTEXT_SITE_ID', 'some page context site ID');
        $provide.value('PAGE_CONTEXT_CATALOG', 'some page context catalog');
        $provide.value('PAGE_CONTEXT_CATALOG_VERSION', 'some page context catalog version');
    }));

    beforeEach(module("urlServiceModule"));

    beforeEach(inject(function(_urlService_) {
        urlService = _urlService_;
    }));

    it('GIVEN urlService is configured openUrl function is not left empty as we have a concrete implementation', function() {
        expect(urlService.openUrlInPopup).not.toBeEmptyFunction();
    });

    it('GIVEN url service is configured it should init a private gateway', function() {
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(urlService, ['openUrlInPopup', 'path']);
    });
});
