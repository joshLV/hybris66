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
angular.module('OuterMocksModule', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])

    .constant('SMARTEDIT_ROOT', 'web/webroot')

    .value('CONFIGURATION_MOCK', [{
        "id": "2",
        "value": "\"thepreviewTicketURI\"",
        "key": "previewTicketURI"
    }, {
        "id": "3",
        "value": "{\"smartEditLocation\":\"/test/e2e/domObserver/innerMocksforContextualMenu.js\"}",
        "key": "applications.BackendMockModule"
    }, {
        "id": "4",
        "value": "{\"smartEditLocation\":\"/smartedit-build/test/e2e/smartedit/clickThroughOverlay.js\"}",
        "key": "applications.clickThroughOverlayModule"
    }, {
        "id": "7",
        "value": "{\"smartEditLocation\":\"/test/e2e/domObserver/testDecorators.js\"}",
        "key": "applications.FakeModule"
    }, {
        "id": "8",
        "value": "\"somepath\"",
        "key": "i18nAPIRoot"
    }]);

try {
    angular.module('smarteditloader').requires.push('OuterMocksModule');
    angular.module('smarteditcontainer').requires.push('OuterMocksModule');
} catch (ex) {}
