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
            'run',
            'maxrun'
        ],
        config: function(data, conf) {
            conf = conf || {};

            var path = require('path');

            conf.options = {
                // Required to prevent grunt from exiting with a non-zero status in CI
                keepAlive: process.env.PROTRACTOR_KEEP_ALIVE === 'true',
                configFile: path.join(global.smartedit.bundlePaths.bundleRoot, "test/protractor-conf.js")
            };
            conf.run = {
                // standard e2e
            };

            conf.maxrun = { // multiple instance e2e (more performant)
                options: {
                    args: {
                        capabilities: {
                            shardTestFiles: true,
                            maxInstances: process.env.PROTRACTOR_CHROME_INSTANCES || 5,
                            chromeOptions: {
                                args: ['lang=en-US', 'dummy'] //pass a second dummy value to prevent grutn-protractor from trimming the [] when passing to protractor
                            }
                        }
                    }
                }
            };

            return conf;
        }
    };
};
