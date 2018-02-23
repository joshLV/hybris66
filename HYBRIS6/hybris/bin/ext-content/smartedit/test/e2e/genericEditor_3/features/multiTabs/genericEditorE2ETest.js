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
/* jshint undef:false */
describe("GenericEditor MultiTabs -", function() {

    var page = require("./../../componentObjects/genericEditorPageObject");
    var genericEditor = require("./../../componentObjects/genericEditorComponentObject");

    var DEFAULT_TAB_KEY = "GENERICEDITOR.TAB.DEFAULT.TITLE".toLowerCase();
    var ADMIN_TAB_KEY = "GENERICEDITOR.TAB.ADMINISTRATION.TITLE".toLowerCase();

    // Constants
    // var TEXT_FIELD = page.constants.HEADLINE_FIELD; 
    // var TEXT_FIELD_INVALID_VALUE = page.constants.HEADLINE_INVALID_TEXT; 
    // var TEXT_FIELD_UNKNOWN_TYPE = page.constants.HEADLINE_UNKNOWN_TYPE;

    // var RICH_TEXT_FIELD = page.constants.CONTENT_FIELD;
    // var RICH_TEXT_FIELD_INVALID_VALUE = page.constants.CONTENT_FIELD_INVALID_TEXT;
    // var RICH_TEXT_FIELD_INVALID_VALUE_IT = page.constants.CONTENT_FIELD_INVALID_TEXT_IT;
    // var RICH_TEXT_FIELD_ERROR_MSG = page.constants.CONTENT_FIELD_ERROR_MSG;

    // var NOT_LOCALIZED = null; 
    // var ENGLISH_TAB = 'en';
    // var ITALIAN_TAB = 'it'; 
    // var FRENCH_TAB = 'fr'; 
    // var POLISH_TAB = 'pl'; 
    // var HINDI_TAB = 'hi'; 

    beforeEach(function(done) {
        page.actions.configureTest({
            multipleTabs: true
        });
        page.actions.bootstrap(__dirname, done);
    });

    describe('basic', function() {
        it('GIVEN tabs have been configured for the editor WHEN the editor opens THEN tabs are displayed and sorted in the right order', function() {
            // THEN 
            // genericEditor.assertions.editorHasTabsDisplayed([
            //     DEFAULT_TAB_ID, 
            //     ADMIN_TAB_ID, 
            //     VISIBILITY_TAB_ID
            // ]);
        });

        it('GIVEN tabs have been configured for the editor WHEN the editor opens THEN fields are organized per tab as per the configuration', function() {
            // THEN 
            // genericEditor.assertions.fieldIsInTab(HEADLINE, DEFAULT_TAB_ID); 
            // genericEditor.assertions.fieldIsInTab(CONTENT, DEFAULT_TAB_ID); 

            // TODO: Add others
        });
    });

    describe('form save', function() {
        it('WHEN the editor is opened THEN it will display cancel button AND not display submit button', function() {
            // THEN 
            // genericEditor.assertions.cancelButtonIsNotDisplayed(); 
            // genericEditor.assertions.submitButtonIsNotDisplayed(); 
        });

        it('WHEN the editor is opened THEN it will display cancel and submit buttons when an attribute is modified in another tab', function() {
            // // GIVEN 
            // genericEditor.actions.selectEditorTab(ADMIN_TAB_ID); 

            // // WHEN 
            // genericEditor.actions.setTextFieldValue(TODO, NOT_LOCALIZED, 'some value');

            // // THEN 
            // genericEditor.assertions.cancelButtonIsDisplayed(); 
            // genericEditor.assertions.submitButtonIsDisplayed(); 
        });

        it('GIVEN field has invalid information in another tab WHEN the form is saved THEN it will display validation errors in the correct tab', function() {
            // // GIVEN 

            // // WHEN 
            // genericEditor.actions.setTextFieldValue(TEXT_FIELD, NOT_LOCALIZED, TEXT_FIELD_INVALID_VALUE); 
            // genericEditor.actions.submitForm(); 

            // // THEN 
            // genericEditor.assertions.fieldHasValidationErrors(TEXT_FIELD, NOT_LOCALIZED, 2);
        });

        it('GIVEN field has invalid information in multiple tabs WHEN the form is saved THEN it will display validation errors in the correct tabs', function() {
            // GIVEN

            // WHEN

            // THEN 
        });

        it('GIVEN form has validation errors in multiple tabs WHEN reset is clicked THEN validation errors are removed in all tabs', function() {
            // // GIVEN 
            // genericEditor.actions.setTextFieldValue(TEXT_FIELD, NOT_LOCALIZED, TEXT_FIELD_INVALID_VALUE); 
            // genericEditor.actions.submitForm(); 
            // genericEditor.assertions.fieldHasValidationErrors(TEXT_FIELD, NOT_LOCALIZED, 2);

            // // WHEN 
            // genericEditor.actions.cancelForm();

            // // THEN 
            // genericEditor.assertions.fieldHasNoValidationErrors(TEXT_FIELD); 
        });

        it("when errors is about a field on another tab than the current one, the target tab lights up and the field is in error", function() {
            genericEditor.actions.selectTab(ADMIN_TAB_KEY);

            genericEditor.actions.setTextFieldValue("id", null, "some wrong content X");

            genericEditor.actions.selectTab(DEFAULT_TAB_KEY);

            genericEditor.actions.submitForm();

            genericEditor.assertions.tabIsInError(ADMIN_TAB_KEY);

            genericEditor.actions.selectTab(ADMIN_TAB_KEY);

            var elements = genericEditor.elements.getValidationErrors('id');

            expect(elements.count()).toBe(1);
            expect(elements.get(0).getText()).toBe("This field cannot contain an X");
        });
    });
});
