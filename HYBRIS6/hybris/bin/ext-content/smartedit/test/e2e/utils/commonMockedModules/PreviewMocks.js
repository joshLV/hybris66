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
angular.module('PreviewMocksModule', ['ngMockE2E', 'functionsModule'])
    .run(function($httpBackend, PREVIEW_RESOURCE_URI, resourceLocationToRegex) {
        $httpBackend.whenPOST(resourceLocationToRegex(PREVIEW_RESOURCE_URI)).respond(function(method, url, data) {

            var returnedPayload = angular.extend({}, data, {
                ticketId: 'dasdfasdfasdfa',
                resourcePath: document.location.origin + '/test/utils/storefront.html'
            });

            return [200, returnedPayload];
        });
    });

angular.module('smarteditloader').requires.push('PreviewMocksModule');
angular.module('smarteditcontainer').requires.push('PreviewMocksModule');
