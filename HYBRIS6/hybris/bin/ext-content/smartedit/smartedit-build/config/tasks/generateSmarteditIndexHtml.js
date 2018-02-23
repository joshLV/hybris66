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
module.exports = function(grunt) {

    var disclaimer =
        `
<!--========================================-->
<!-- !!!!!!!!!!!!! WARNING !!!!!!!!!!!!!!!! -->
<!-- this file is generated, DO NOT EDIT!   -->
<!--========================================-->
`;

    var taskName = 'generateSmarteditIndexHtml';
    var taskDescription = "Generates a index.html file for the dummy storefront that will load custom extensions layouts, delays, and render strategies";

    var path = require('path');
    var lodash = require('lodash');
    var fs = require('fs-extra');
    var LOGGER = require('../grunt-utils/taskLogger')(grunt, taskName, taskDescription);


    grunt.registerMultiTask(taskName, taskDescription, function() {

        // grunt supposed to do this automatically but for some reaosn its not so I will do it myself...
        var baseConf = grunt.config.get(taskName);
        var conf = lodash.cloneDeep(baseConf[this.target] || {});
        lodash.defaultsDeep(conf, baseConf.options || {});

        LOGGER.startTask();

        var gruntConfig = conf;

        if (!gruntConfig.dest) {
            grunt.fail.fatal(`${taskName} - No destination specified in config.`);
        }

        var templateFile = path.join(global.smartedit.bundlePaths.bundleRoot, 'config/grunt-utils/smartedit.index.tpl.html');
        var templateContents = fs.readFileSync(templateFile, 'utf-8');

        var smarteditContent = gruntConfig.smarteditContent || "";
        var content = gruntConfig.headerContent || "";

        templateContents = disclaimer.concat(templateContents);
        templateContents = templateContents.replace(/SMARTEDIT_PLACEHOLDER/g, smarteditContent);
        templateContents = templateContents.replace(/HEADER_PLACEHOLDER/g, content);

        // bundleRoot is set in the base bundle config, so someone can choose to not load that config
        if (global.smartedit.bundlePaths.bundleRoot) {
            LOGGER.verbose.detail('Bundle root calculated to be at: ' + global.smartedit.bundlePaths.bundleRoot);
            LOGGER.verbose.detail('Output dir calculated to be at: ' + path.resolve(path.dirname(gruntConfig.dest)));
            var relativePath = path.relative(path.resolve(path.dirname(gruntConfig.dest)), global.smartedit.bundlePaths.bundleRoot);
            LOGGER.verbose.detail('Replacing occurrences in content of BUNDLE_LOCATION with: ' + relativePath);
            templateContents = templateContents.replace(/BUNDLE_LOCATION/g, relativePath);
        }

        LOGGER.info(`Writting to: ${gruntConfig.dest}`);
        fs.outputFileSync(gruntConfig.dest, templateContents);

        LOGGER.endTask();

    });


};
