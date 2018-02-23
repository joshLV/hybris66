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

var genericEditorCommons;
if (typeof require !== 'undefined') {
    genericEditorCommons = require('./commonGenericEditorComponentObject');
}

module.exports = (function() {

    var componentObject = {};

    componentObject.constants = {};

    componentObject.elements = {


        // // -- Dialog Elements --
        // getSaveButton: function() {
        //     return element(by.id('save'));
        // },
        // getCancelButton: function() {
        //     return element(by.id('cancel'));
        // },
        // getCloseButton: function() {
        //     return element(by.css('button.close'));
        // },

        // // -- Generic Editor Elements -- 
        // getField: function(fieldId) {
        //     return element(by.css('.ySErow #' + fieldId));
        // },
        // getLocalizedFieldLanguageButton: function(fieldId, language) {
        //     return componentObject.elements.getField(fieldId)
        //         .element(by.cssContainingText('localized-element ul li a', language));
        // },
        // getLocalizedFieldInputs: function(fieldId) {
        //     return componentObject.elements.getField(fieldId)
        //         .all(by.css('localized-element y-tab input'));
        // },
        // getLocalizedFieldInputForLanguage: function(fieldId, language) {
        //     return componentObject.elements.getField(fieldId)
        //         .element(by.css('localized-element y-tab[data-tab-id="' + language.toLowerCase() + '"] input'));
        // }
        getTextField: function(fieldQualifier) {
            return genericEditorCommons.elements.getFieldByQualifier(fieldQualifier).element(by.css('input'));
        },
        getBooleanField: function(fieldQualifier) {
            return genericEditorCommons.elements.getFieldByQualifier(fieldQualifier).element(by.css('input'));
        }
    };

    componentObject.actions = {
        // -- Text Widget --
        setTextFieldValue: function(fieldQualifier, value) {
            return componentObject.elements.getTextField(fieldQualifier).clear().sendKeys(value);
        },

        // -- Boolean Widget -- 
        setBooleanFieldValue: function(fieldQualifier, toggleOn) {
            var component = componentObject.elements.getBooleanField(fieldQualifier);
            return component.isSelected().then(function(isSelected) {
                if (isSelected !== toggleOn) {
                    return browser.click(component);
                }
            });
        }
    };

    componentObject.assertions = {
        // -- Text Widget --
        textFieldHasRightValue: function(fieldQualifier, expectedText) {
            expect(componentObject.elements.getTextField(fieldQualifier).getAttribute('value')).toBe(expectedText,
                'Expected field ' + fieldQualifier + ' to have the following text: ' + expectedText);
        },

        // -- Boolean Widget -- 
        booleanFieldHasRightValue: function(fieldQualifier, expectedValue) {
            expect(componentObject.elements.getBooleanField(fieldQualifier).isSelected()).toBe(expectedValue,
                'Expected field ' + fieldQualifier + ' to be ' + expectedValue);
        }
    };

    componentObject.utils = {};

    return componentObject;

}());
