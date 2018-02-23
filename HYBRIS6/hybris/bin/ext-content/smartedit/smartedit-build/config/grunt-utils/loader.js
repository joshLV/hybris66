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
    'use strict';

    const fs = require('fs-extra');
    const path = require('path');
    const lodash = require('lodash');
    const gruntHooks = require('grunt-before-after-hooks');
    const bundlePaths = require('../../bundlePaths');
    const deepFreeze = require('./deepFreeze');

    if (!grunt) {
        throw `Smeeb - require() missing grunt object`; // TODO <----- rename
    }

    class Smeeb {

        constructor() {
            global.smartedit = {
                bundlePaths
            };
            deepFreeze(global.smartedit);

            this.config = {
                taskPaths: [
                    bundlePaths.build.grunt.tasksDir,
                    bundlePaths.external.grunt.tasksDir
                ],
                configPaths: [
                    bundlePaths.build.grunt.configDir,
                    bundlePaths.external.grunt.configDir
                ],
                configData: {} // TODO get rid of this param
            };
        }

        load() {

            function logStage(stageName) {
                grunt.log.writeln('');
                grunt.log.write('>>> ' ['green']);
                grunt.log.writeln(stageName.cyan);
            }

            // Make sure a bundleRoot property is specified
            if (!fs.existsSync(bundlePaths.bundleRoot)) {
                grunt.fail.fatal(`No bundle found at ${bundlePaths.bundleRoot}`);
            }

            // Load custom built tasks
            // The rest loaded via the config
            logStage('Loading grunt tasks');
            this.config.taskPaths.forEach(function(tasksRoot) {
                grunt.loadTasks(tasksRoot);
            });

            // =============================================================================
            // =============================================================================
            // LOADING DUMMY GRUNT INITCONFIG OBJECT

            function recursiveCallForEachFile(rootDir, fn) {
                const files = fs.readdirSync(rootDir);
                files.forEach(function(file) {
                    let fullPath = path.join(rootDir, file);
                    if (fs.statSync(fullPath).isDirectory()) {
                        recursiveCallForEachFile(fullPath, fn);
                    } else {
                        fn(rootDir, file, fullPath);
                    }
                });
            }
            let gruntConfig = {};
            // Iterate over all config files, and reading them into config object
            logStage('Searching for task and target names');
            this.config.configPaths.forEach(function(configRoot) {
                grunt.log.writeln(`Searching in directory:\n${configRoot}` ['cyan']);
                recursiveCallForEachFile(configRoot, function(dir, file, fullPath) {
                    const dot = file.lastIndexOf('.');
                    const simpleName = file.substr(0, dot);
                    grunt.log.debug(`Task ${simpleName}`);
                    try {
                        let conf = require(fullPath)();
                        gruntConfig[simpleName] = gruntConfig[simpleName] || {};
                        (conf.targets || []).forEach((target) => {
                            grunt.log.debug(`  - ${target}`);
                            gruntConfig[simpleName][target] = {};
                        });
                    } catch (e) {
                        grunt.log.error(`ERROR: Problem reading grunt task config for ${fullPath}` ['red']);
                        grunt.fail.fatal(e);
                    }
                });
            });
            logStage('Initializing grunt with task and target names');
            grunt.log.debug(JSON.stringify(gruntConfig, null, 4));
            grunt.initConfig(gruntConfig);
            // =============================================================================
            // =============================================================================

            let loadedConfigs = [];
            const originalConfigGet = grunt.config.get;

            grunt.loadConfigForTask = function(taskName) {
                if (loadedConfigs.indexOf(taskName) === -1) {
                    grunt.log.debug(`\nLoading config for task: ${taskName}`);
                    this.config.configPaths.forEach(function(configRoot) {
                        let configFile = path.join(configRoot, taskName + '.js');
                        grunt.log.debug(`Loading from file: ${configFile}`);
                        if (fs.existsSync(configFile)) {
                            try {
                                grunt.config.set(taskName, require(configFile)(grunt).config(this.config.configData, lodash.cloneDeep(originalConfigGet(taskName) || {})));
                            } catch (e) {
                                grunt.log.error(`ERROR: Problem reading grunt task config for ${configFile}` ['red']);
                                grunt.fail.fatal(e);
                            }
                        }
                    }.bind(this));
                    grunt.log.debug(`Final config loaded:\n${JSON.stringify(originalConfigGet(taskName), null, 2)}`);

                    loadedConfigs.push(taskName);
                }
            }.bind(this);

            grunt.config.get = function(taskName) {
                grunt.loadConfigForTask(taskName);
                return originalConfigGet(taskName);
            };

            gruntHooks(grunt, {
                beforeEach(currentTask) {
                    grunt.loadConfigForTask(currentTask.name);
                }
            });

        }
    }


    return new Smeeb();
};
