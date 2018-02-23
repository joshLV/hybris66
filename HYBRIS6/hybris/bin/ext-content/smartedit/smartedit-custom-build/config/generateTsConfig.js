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

            const declaration = {
                "declaration": true,
                "declarationDir": "../../temp/types"
            };

            const excludedPatterns = ["smarteditcommons", "smartedit", "smarteditcontainer"];

            function addTypesDeclarationAndRemovePathsAndTypeRoots(conf) {
                lodash.merge(conf.compilerOptions, declaration);
                Object.keys(conf.compilerOptions.paths || []).forEach((key) => {
                    if (excludedPatterns.find((pattern) => key.indexOf(pattern) > -1)) {
                        delete conf.compilerOptions.paths[key];
                    }
                });
                conf.compilerOptions.typeRoots = conf.compilerOptions.typeRoots.filter((key) => {
                    return key.indexOf(global.smartedit.bundlePaths.bundleDirName + "/@types") === -1;
                });
            }

            addTypesDeclarationAndRemovePathsAndTypeRoots(conf.generateProdSmarteditTsConfig.data);
            addTypesDeclarationAndRemovePathsAndTypeRoots(conf.generateProdSmarteditContainerTsConfig.data);
            addTypesDeclarationAndRemovePathsAndTypeRoots(conf.generateDevSmarteditTsConfig.data);
            addTypesDeclarationAndRemovePathsAndTypeRoots(conf.generateDevSmarteditContainerTsConfig.data);
            addTypesDeclarationAndRemovePathsAndTypeRoots(conf.generateKarmaSmarteditTsConfig.data);
            addTypesDeclarationAndRemovePathsAndTypeRoots(conf.generateKarmaSmarteditContainerTsConfig.data);

            return conf;
        }
    };

};
