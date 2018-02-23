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
            const lodash = require('lodash');
            const paths = require('../paths');

            let optionSpecs = {
                options: {
                    args: {
                        specs: paths.tests.allE2e
                    }
                }
            };

            lodash.defaultsDeep(conf.run, optionSpecs);
            lodash.defaultsDeep(conf.maxrun, optionSpecs);

            return conf;
        }
    };
};
