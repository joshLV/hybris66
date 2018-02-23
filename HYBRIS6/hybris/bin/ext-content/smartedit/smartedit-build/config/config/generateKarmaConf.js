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
        targets: ['generateSmarteditKarmaConf', 'generateSmarteditContainerKarmaConf'],
        config: function(data, conf) {

            const paths = global.smartedit.bundlePaths;
            const lodash = require('lodash');
            const karmaConfigTemplates = require('../templates').karmaConfigTemplates;

            conf.generateSmarteditKarmaConf = {
                dest: paths.external.generated.karma.smartedit,
                data: lodash.cloneDeep(karmaConfigTemplates.base)
            };

            conf.generateSmarteditContainerKarmaConf = {
                dest: paths.external.generated.karma.smarteditContainer,
                data: lodash.cloneDeep(karmaConfigTemplates.base)
            };

            return conf;
        }
    };

};
