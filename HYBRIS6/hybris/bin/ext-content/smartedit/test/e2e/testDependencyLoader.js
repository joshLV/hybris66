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
module.exports = function() {

    return {
        loadE2eDependencies: function(browser) {
            browser.setStorefrontDelayConfigInSessionStorage = function() {
                // browser.setStorefrontDelayConfigInSessionStorage = function(delayConfig) {
                browser.get('smartedit-build/test/e2e/dummystorefront/fakeAngularEmptyPage.html');
                // browser.executeScript('window.sessionStorage.setItem("STOREFRONT_DELAY_STRATEGY", arguments[0])', delayConfig);
            };
        }
    };

}();
