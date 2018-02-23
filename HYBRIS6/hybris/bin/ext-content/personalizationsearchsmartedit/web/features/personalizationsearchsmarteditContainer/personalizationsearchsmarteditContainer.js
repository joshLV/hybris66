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
angular.module('personalizationsearchsmarteditContainer', ['personalizationsearchsmarteditContainerTemplates', 'featureServiceModule', 'personalizationsearchSearchProfilesModule'])
    .run(function(yjQuery, domain) {
        var loadCSS = function(href) {
            var cssLink = yjQuery("<link rel='stylesheet' type='text/css' href='" + href + "'>");
            yjQuery("head").append(cssLink);
        };
        loadCSS(domain + "/personalizationsearchsmartedit/css/style.css");

    });
