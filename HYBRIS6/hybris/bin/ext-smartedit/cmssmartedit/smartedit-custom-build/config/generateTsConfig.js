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
        config: function(data, conf) {
            const lodash = require('lodash');

            const cmsSmarteditPaths = {
                "cmssmartedit/*": ["web/features/cmssmartedit/*"],
                "cmscommons": ["web/features/cmscommons"],
                "cmscommons*": ["web/features/cmscommons*"]
            };

            const cmsSmarteditContainerPaths = {
                "cmssmarteditcontainer/*": ["web/features/cmssmarteditContainer/*"],
                "cmscommons": ["web/features/cmscommons"],
                "cmscommons*": ["web/features/cmscommons*"]
            };

            function addCmsSmarteditPaths(conf) {
                lodash.merge(conf.compilerOptions.paths, lodash.cloneDeep(cmsSmarteditPaths));
            }

            function addCmsSmarteditContainerPaths(conf) {
                lodash.merge(conf.compilerOptions.paths, lodash.cloneDeep(cmsSmarteditContainerPaths));
            }

            addCmsSmarteditPaths(conf.generateProdSmarteditTsConfig.data);
            addCmsSmarteditContainerPaths(conf.generateProdSmarteditContainerTsConfig.data);
            addCmsSmarteditPaths(conf.generateDevSmarteditTsConfig.data);
            addCmsSmarteditContainerPaths(conf.generateDevSmarteditContainerTsConfig.data);
            addCmsSmarteditPaths(conf.generateKarmaSmarteditTsConfig.data);
            addCmsSmarteditContainerPaths(conf.generateKarmaSmarteditContainerTsConfig.data);
            addCmsSmarteditContainerPaths(conf.generateIDETsConfig.data);
            addCmsSmarteditPaths(conf.generateIDETsConfig.data);

            return conf;
        }
    };
};
