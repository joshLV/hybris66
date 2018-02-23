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
        config: function(data, baseConf) {
            return {
                prefix: {
                    ignored: ['se.', 'type.'], // keys provided by smartedit-locales_en.properties
                    expected: ['ysmarteditmodule.']
                },
                paths: {
                    files: [
                        "web/features/**/*Template.html",
                        "web/features/**/*.js"
                    ],
                    properties: [
                        "resources/localization/ysmarteditmodule-locales_en.properties",
                        global.smartedit.bundlePaths.bundleRoot + "/localization/smartedit-locales_en.properties"
                    ]
                }
            };
        }
    };

};
