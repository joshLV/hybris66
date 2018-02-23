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
        targets: ['server', 'dev', 'test', 'debug', 'docs'],
        config: function(data, conf) {

            return {
                "server": {
                    "options": {
                        "hostname": 'localhost',
                        "port": 7000
                    }
                },
                "dev": {
                    "options": {
                        "hostname": "0.0.0.0",
                        "port": 8080,
                        "keepalive": true,
                        "open": true
                    }
                },
                "test": {
                    "options": {
                        "hostname": "0.0.0.0",
                        "port": 7000
                    }
                },
                "debug": {
                    "options": {
                        "hostname": "0.0.0.0",
                        "port": 7000,
                        "keepalive": true
                    }
                },
                "docs": {
                    "options": {
                        "hostname": "0.0.0.0",
                        "port": 9090,
                        "keepalive": true,
                        "open": true,
                        "base": "./jsTarget/docs"
                    }
                }
            };
        }
    };
};
