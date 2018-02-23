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
    const path = require('path');

    const SE_BUILD_FOLDER = 'smartedit-build';
    const EXTENSION_CONFIG_DIR = 'smartedit-custom-build';
    const GEN_PATH = EXTENSION_CONFIG_DIR + '/generated';
    const BUNDLE_ROOT = path.join(process.cwd(), SE_BUILD_FOLDER);

    return {
        bundleDirName: SE_BUILD_FOLDER,
        bundleRoot: BUNDLE_ROOT,
        external: { // Anything outside of the bundle
            grunt: {
                configDir: path.resolve(`${EXTENSION_CONFIG_DIR}/config`),
                tasksDir: path.resolve(`${EXTENSION_CONFIG_DIR}/tasks`)
            },
            generated: {
                karma: {
                    smartedit: `${GEN_PATH}/karma.smartedit.conf.js`,
                    smarteditContainer: `${GEN_PATH}/karma.smarteditContainer.conf.js`,
                    smarteditCommons: `${GEN_PATH}/karma.smarteditcommons.conf.js`
                },
                tsconfig: {
                    prodSmartedit: `${GEN_PATH}/tsconfig.prod.smartedit.json`,
                    prodSmarteditContainer: `${GEN_PATH}/tsconfig.prod.smarteditContainer.json`,
                    devSmartedit: `${GEN_PATH}/tsconfig.dev.smartedit.json`,
                    devSmarteditContainer: `${GEN_PATH}/tsconfig.dev.smarteditContainer.json`,
                    karmaSmartedit: `${GEN_PATH}/tsconfig.karma.smartedit.json`,
                    karmaSmarteditContainer: `${GEN_PATH}/tsconfig.karma.smarteditContainer.json`,
                    ide: `${GEN_PATH}/tsconfig.ide.json`,
                },
                webpack: {
                    prodSmartedit: `${GEN_PATH}/webpack.prod.smartedit.config.js`,
                    prodSmarteditContainer: `${GEN_PATH}/webpack.prod.smarteditContainer.config.js`,
                    devSmartedit: `${GEN_PATH}/webpack.dev.smartedit.config.js`,
                    devSmarteditContainer: `${GEN_PATH}/webpack.dev.smarteditContainer.config.js`,
                    karmaSmartedit: `${GEN_PATH}/webpack.karma.smartedit.config.js`,
                    karmaSmarteditContainer: `${GEN_PATH}/webpack.karma.smarteditContainer.config.js`
                }
            }
        },
        build: {
            tsfmt: `${SE_BUILD_FOLDER}/config/tsfmt.json`,
            jshintrc: `${SE_BUILD_FOLDER}/config/.jshintrc`,
            grunt: {
                gruntLoader: path.resolve(path.join(BUNDLE_ROOT, 'config/grunt-utils/loader.js')),
                configDir: path.resolve(path.join(BUNDLE_ROOT, 'config/config')),
                tasksDir: path.resolve(path.join(BUNDLE_ROOT, 'config/tasks'))
            }
        },
        test: {
            unit: {
                commonUtilModules: [
                    path.join(BUNDLE_ROOT, 'test/unit/**/*.+(js|ts)')
                ],
                smarteditThirdPartyJsFiles: [
                    path.join(BUNDLE_ROOT, 'webroot/static-resources/dist/smartedit/js/prelibraries.js')
                ],
                smarteditContainerUnitTestFiles: [
                    path.join(BUNDLE_ROOT, 'webroot/static-resources/dist/smartedit/js/thirdparties.js'),
                    path.join(BUNDLE_ROOT, 'webroot/static-resources/thirdparties/ckeditor/ckeditor.js')
                ]
            }
        }
    };

}();
