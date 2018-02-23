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
module.exports = (function() {

    var componentObject = {};

    componentObject.constants = {
        TOP_HEADER_SLOT_ID: 'topHeaderSlot',
        BOTTOM_HEADER_SLOT_ID: 'bottomHeaderSlot',
        SEARCH_BOX_SLOT: 'searchBoxSlot',
        FOOTER_SLOT_ID: 'footerSlot',
        OTHER_SLOT_ID: 'otherSlot',
        COMPONENT1_NAME: 'component1',
        COMPONENT4_NAME: 'component4',
        STATIC_SLOT_ID: 'staticDummySlot',
        STATIC_COMPONENT_NAME: 'staticDummyComponent'
    };

    componentObject.elements = {
        getComponentById: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return element(by.css('.smartEditComponent[data-smartedit-component-id=' + componentId + ']'));
            });
        },
        getSlotById: function(slotId) {
            return browser.switchToIFrame().then(function() {
                return element(by.css("#smarteditoverlay .smartEditComponentX[data-smartedit-component-id='" + slotId + "']"));
            });
        },
        getComponentsBySlotId: function(slotId) {
            return browser.switchToIFrame().then(function() {
                return element.all(by.css("[data-smartedit-component-id='" + slotId + "'] .smartEditComponent"));
            });
        },
        getComponentByAttributeAndValue: function(attribute, value) {
            return browser.switchToIFrame().then(function() {
                return element(by.css('.smartEditComponent[' + attribute + '=' + value + ']'));
            });
        }
    };

    componentObject.actions = {
        moveToComponent: function(componentId) {
            browser.waitUntilNoModal();
            return browser.switchToIFrame().then(function() {
                return componentObject.elements.getComponentById(componentId).then(function(element) {
                    return browser.actions()
                        .mouseMove(element)
                        .perform();
                });
            });
        },
        moveToComponentByAttributeAndValue: function(attribute, value) {
            return browser.switchToIFrame().then(function() {
                return componentObject.elements.getComponentByAttributeAndValue(attribute, value).then(function(element) {
                    return browser.actions()
                        .mouseMove(element)
                        .perform();
                });
            });
        },
        goToSecondPage: function() {
            browser.waitForWholeAppToBeReady();
            return browser.switchToIFrame().then(function() {
                browser.click(by.id('deepLink'));
                browser.waitForFrameToBeReady();
                browser.waitUntilNoModal();
                return;
            });
        }
    };

    return componentObject;
})();
