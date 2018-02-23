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

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));


    beforeEach(module("urlServiceModule"));

    beforeEach(inject(function(_urlService_) {
        urlService = _urlService_;
    }));

    it('openUrlInPopup function is left empty to enable proxying', function() {
        expect(urlService.openUrlInPopup).toBeEmptyFunction();
    });

    it('url service inits a private gateway', function() {
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(urlService, ['openUrlInPopup', 'path']);
    });
});
