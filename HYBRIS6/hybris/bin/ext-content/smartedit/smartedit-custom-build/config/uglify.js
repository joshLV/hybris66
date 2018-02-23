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
            'dist',
            'sanitizeWebApplicationInjectorForDummystorefront',
            'uglifyThirdparties'
        ],
        config: function(data, conf) {
            var paths = require('../paths');

            return {
                dist: {
                    files: {
                        'web/webroot/static-resources/webApplicationInjector.js': ['jsTarget/webApplicationInjector.js'],
                        'web/webroot/static-resources/smarteditloader/js/smarteditloader.js': ['jsTarget/smarteditloader.js'],
                        'web/webroot/static-resources/smarteditcontainer/js/smarteditcontainer.js': ['jsTarget/smarteditcontainer.js'],
                        'web/webroot/static-resources/dist/smartedit/js/presmartedit.js': ['jsTarget/presmartedit.js'],
                        'web/webroot/static-resources/dist/smartedit/js/postsmartedit.js': ['jsTarget/postsmartedit.js'],
                        'web/webroot/static-resources/smarteditcontainer/modules/administrationModule.js': [paths.web.webroot.staticResources.dir + '/smarteditcontainer/modules/administrationModule.js'],
                        'web/webroot/static-resources/smartedit/modules/systemModule.js': [paths.web.webroot.staticResources.dir + '/smartedit/modules/systemModule.js']
                    },
                    options: {
                        mangle: true //ok since one has ng-annotate beforehand
                    }
                },
                //Since uglify properly terminates statements with semi-colon, it thereby sanitizes the not so clean $script js
                sanitizeWebApplicationInjectorForDummystorefront: {
                    files: {
                        'jsTarget/webApplicationInjector.js': ['jsTarget/webApplicationInjector.js']
                    },
                    options: {
                        mangle: true
                    }
                },

                uglifyThirdparties: {
                    files: {
                        'node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls.min.js': ['node_modules/angular-ui-bootstrap/dist/ui-bootstrap-tpls.js']
                    },
                    options: {
                        mangle: true
                    }

                }
            };
        }
    };
};
