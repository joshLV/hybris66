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
angular.module('trashedPageListDatePrinterModule', [])
    .controller('trashedPageListDatePrinterController', function() {

        this.$onChanges = function() {

            this.date = new Date(this.trashedDate).toLocaleDateString() + " " + new Date(this.trashedDate).toLocaleTimeString();

        };
    })
    .component('trashedPageListDatePrinter', {
        template: '<div>{{$ctrl.date}}</div>',
        controller: 'trashedPageListDatePrinterController',
        bindings: {
            trashedDate: '<'
        }
    });
