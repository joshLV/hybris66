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
            //only in bundle
        ],
        config: function(data, conf) {

            const smarteditEntry = {
                'presmartedit': './jsTarget/web/app/smartedit/index.ts',
                'postsmartedit': './jsTarget/web/app/smartedit/core/smarteditbootstrap.ts',
                'systemModule': './jsTarget/web/app/smartedit/modules/systemModule/system.ts'
            };

            const smarteditContainerEntry = {
                'smarteditloader': './jsTarget/web/app/smarteditloader/smarteditloader.ts',
                'smarteditcontainer': './jsTarget/web/app/smarteditcontainer/index.ts',
                'administrationModule': './jsTarget/web/app/smarteditcontainer/modules/administrationModule/index.ts'
            };

            function setSmarteditProperties(conf) {
                conf.entry = smarteditEntry;
            }

            function setSmarteditContainerProperties(conf) {
                conf.entry = smarteditContainerEntry;
            }


            // ======== PROD ========
            setSmarteditProperties(conf.generateProdSmarteditWebpackConfig.data);
            setSmarteditContainerProperties(conf.generateProdSmarteditContainerWebpackConfig.data);


            // ======== DEV ========
            setSmarteditProperties(conf.generateDevSmarteditWebpackConfig.data);
            setSmarteditContainerProperties(conf.generateDevSmarteditContainerWebpackConfig.data);


            // ======== KARMA ========
            // don't add entry for karma


            return conf;
        }
    };
};
