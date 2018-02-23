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
        targets: [
            'ysmarteditmodule',
            'ysmarteditmoduleContainer',
            'ysmarteditmodulecommons'
        ],
        config: function(data, conf) {
            return {
                ysmarteditmodule: conf.unitSmartedit,
                ysmarteditmoduleContainer: conf.unitSmarteditContainer,
                ysmarteditmodulecommons: {
                    options: {
                        configFile: global.smartedit.bundlePaths.external.generated.karma.smarteditCommons,
                    }
                }
            };
        }
    };

};
