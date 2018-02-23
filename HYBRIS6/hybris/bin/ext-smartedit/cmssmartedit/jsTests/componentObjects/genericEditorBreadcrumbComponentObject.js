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
/* jshint unused:false, undef:false */

var genericEditorCommons, genericEditorWidgets;
if (typeof require !== 'undefined') {
    genericEditorCommons = require('./commonGenericEditorComponentObject');
}

module.exports = (function() {

    var componentObject = {};

    componentObject.constants = {};

    componentObject.elements = {
        getBreadcrumb: function() {
            return genericEditorCommons.elements.getTopEditorModal().element(by.css('generic-editor-breadcrumb'));
        },
        getBreadcrumbElementInLevel: function(expectedNestingLevel) {
            return this.getBreadcrumb().all(by.css('.se-ge-breadcrumb__item')).get(expectedNestingLevel - 1);
        },
        getElementTitleByNestingLevel: function(expectedNestingLevel) {
            return this.getBreadcrumbElementInLevel(expectedNestingLevel).element(by.css('.se-ge-breadcrumb__title'));
        },
        getElementInfoByNestingLevel: function(expectedNestingLevel) {
            return this.getBreadcrumbElementInLevel(expectedNestingLevel).element(by.css('.se-ge-breadcrumb__info'));
        }
    };

    componentObject.actions = {};

    componentObject.assertions = {
        componentIsDisplayedInCorrectNestingLevel: function(componentName, componentType, expectedNestingLevel) {
            expect(componentObject.elements.getElementTitleByNestingLevel(expectedNestingLevel).getText()).toBe(componentName,
                'Expected breadcrumb to show ' + componentName + ' at the ' + expectedNestingLevel + ' level.');
            expect(componentObject.elements.getElementInfoByNestingLevel(expectedNestingLevel).getText()).toBe(componentType,
                'Expected breadcrumb to show ' + componentType + ' at the ' + expectedNestingLevel + ' level.');
        }
    };

    componentObject.utils = {};

    return componentObject;

}());
