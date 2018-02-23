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

    componentObject.constants = {};

    componentObject.elements = {
        getOpenedEditorModals: function() {
            return element.all(by.xpath('//div[@uib-modal-window="modal-window"][.//generic-editor]'));
        },
        getTopEditorModal: function() {
            return this.getOpenedEditorModals().get(0);

        },
        getFieldByQualifier: function(fieldQualifier) {
            return this.getTopEditorModal().element(by.css(".ySEGenericEditorFieldStructure[data-cms-field-qualifier='" + fieldQualifier + "']"));
        }
    };

    componentObject.actions = {};

    componentObject.assertions = {};

    componentObject.utils = {};

    return componentObject;

}());
