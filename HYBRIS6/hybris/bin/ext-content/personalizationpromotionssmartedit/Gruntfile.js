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

    grunt.loadTasks('gruntTasks');
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-file-append');
    grunt.loadNpmTasks("grunt-jsbeautifier");
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-ngdocs');
    grunt.loadNpmTasks('grunt-webpack');
    grunt.loadNpmTasks("grunt-tslint");

    // -----------------------------------------------------------------------------------------------------
    // Variables
    // -----------------------------------------------------------------------------------------------------
    var phantomJSPattern = 'node_modules/**/phantomjs*';
    //var chromeDriverPattern = 'node_modules/**' + '/chromedriver*';

    var testBase = 'jsTests';
    var testJsFiles = testBase + '/**/*Test.js';
    var testTsFiles = testBase + '/**/*.ts';
    //var excludedFiles = testBase + '/**/e2e/_shared/**';
    //var testsRootFiles = testBase + '/tests/*';

    var codeBase = 'web/features';
    var featureJsFiles = codeBase + '/**/*.js';
    var featureTsFiles = codeBase + '/**/*.ts';
    var featureHtmlFiles = codeBase + '/**/*.html';

    // -- Webpack -- 
    var webpackConfig = require('./config/webpack.config');
    var devWebpackConfig = webpackConfig.devWebpackConfig;
    var prodWebpackConfig = webpackConfig.prodWebpackConfig;
    var entryPoints = {
        'personalizationpromotionssmartedit': './jsTarget/web/features/personalizationpromotionssmartedit/personalizationpromotionssmarteditApp.ts',
        'personalizationpromotionssmarteditcontainer': './jsTarget/web/features/personalizationpromotionssmartedit/personalizationpromotionssmarteditContainerApp.ts'
    };


    grunt.initConfig({

        pkg: grunt.file.readJSON('package.json'),

        connect: {
            dev: {
                options: {
                    hostname: '127.0.0.1',
                    port: 8080,
                    keepalive: true,
                    open: true
                }
            },
            test: {
                options: {
                    hostname: '127.0.0.1',
                    port: 7000
                }
            },
            debug: {
                options: {
                    hostname: '127.0.0.1',
                    port: 7000,
                    keepalive: true
                }
            }
        },

        less: {
            dev: {
                files: [{
                    expand: true,
                    cwd: 'web/features/styling',
                    src: 'style.less',
                    dest: 'web/webroot/css/',
                    ext: '.css'
                }]
            },
        },

        /*
         *in preparation for ng-annotate so as not to alter original source files
         */
        jsbeautifier: {
            files: ['Gruntfile.js', featureJsFiles, testJsFiles, featureHtmlFiles, '!web/webroot/**/*', '!web/features/styling/*'],
            options: {
                //config: "path/to/configFile",
                html: {
                    braceStyle: "collapse",
                    indentChar: " ",
                    indentScripts: "keep",
                    indentSize: 4,
                    maxPreserveNewlines: 10,
                    preserveNewlines: true,
                    unformatted: ["a", "sub", "sup", "b", "i", "u"],
                    wrapLineLength: 0
                },
                css: {
                    indentChar: " ",
                    indentSize: 4,
                    fileTypes: [".less"]
                },
                js: {
                    braceStyle: "collapse",
                    breakChainedMethods: false,
                    e4x: false,
                    evalCode: false,
                    indentChar: " ",
                    indentLevel: 0,
                    indentSize: 4,
                    indentWithTabs: false,
                    jslintHappy: false,
                    keepArrayIndentation: false,
                    keepFunctionIndentation: false,
                    maxPreserveNewlines: 10,
                    preserveNewlines: true,
                    spaceBeforeConditional: true,
                    spaceInParen: false,
                    unescapeStrings: false,
                    wrapLineLength: 0,
                    endWithNewline: true
                }
            }
        },

        tsformatter: {
            options: { // https://github.com/vvakame/typescript-formatter
                tsfmt: true,
                tsfmtFile: 'config/tsfmt.json',
                replace: true,
                tsconfig: true,
                tsconfigFile: 'config/tsconfig.json',
                tslint: true,
                verbose: false,
            },
            files: [featureTsFiles, testTsFiles]
        },

        clean: {
            temp: {
                src: ['tmp', 'jsTarget']
            },
            cssfiles: {
                src: ['web/webroot/css/*.css']
            }
        },

        /**
         * code quality
         */
        jshint: {
            options: {
                jshintrc: true,
                reporterOutput: ""
            },
            all: ['Gruntfile.js', featureJsFiles, testJsFiles]
        },

        tslint: {
            options: {
                configuration: "tslint.json",
                force: false,
                fix: false
            },
            files: [featureTsFiles, testTsFiles, featureJsFiles] // lint JS files to verify custom ts rules from 'rulesDirectory'
        },

        ngdocs: {
            options: {
                dest: 'jsTarget/docs',
                title: "personalizationpromotionssmartedit API",
                startPage: '/#/personalizationpromotionssmartedit',
            },
            personalizationpromotionssmartedit: {
                api: true,
                src: ['web/features/personalizationpromotionssmarteditcommons/**/*.js', 'web/features/personalizationpromotionssmartedit/**/*.js'],
                title: 'personalizationpromotionssmartedit'
            },
            personalizationpromotionssmarteditcontainer: {
                api: true,
                src: ['web/features/personalizationpromotionssmarteditcommons/**/*.js', 'web/features/personalizationpromotionssmarteditcontainer/**/*.js'],
                title: 'personalizationpromotionssmarteditcontainer'
            }
        },

        webpack: {
            dev: Object.assign({
                entry: entryPoints
            }, devWebpackConfig),
            prod: Object.assign({
                entry: entryPoints
            }, prodWebpackConfig)
        },

        /*
         * 'annotates' angular JS files to be minify-ready
         *
         * @see https://github.com/mzgol/grunt-ng-annotate
         */
        ngAnnotate: {
            options: {
                singleQuotes: true,
            },
            run: {
                files: [{
                    expand: true,
                    src: ['jsTarget/**/*.js']
                }, ],
            }
        },
        uglify: {
            dist: {
                files: [{
                    expand: true, // Enable dynamic expansion.
                    cwd: 'jsTarget/', // Src matches are relative to this path.
                    src: ['*.js'], // Actual pattern(s) to match.
                    dest: 'web/webroot/personalizationpromotionssmartedit/js/', // Destination path prefix.
                    ext: '.js', // Dest filepaths will have this extension.
                    extDot: 'first' // Extensions in filenames begin after the first dot
                }],
                options: {
                    mangle: true //ok since one has ng-annotate beforehand
                }
            }
        },
        watch: {
            test: {
                files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
                tasks: ['test'],
                options: {
                    atBegin: true
                }
            },
            dev: {
                files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
                tasks: ['dev'],
                options: {
                    atBegin: true
                }
            },
            pack: {
                files: ['Gruntfile.js', 'web/features/**/*', 'jsTests/**/*'],
                tasks: ['package'],
                options: {
                    atBegin: true
                }
            }
        },
        karma: {
            options: {
                configFile: 'jsTests/unit/personalizationpromotionssmarteditcommons/karma.conf.js',
            },
            unitCommons: {
                configFile: 'jsTests/unit/personalizationpromotionssmarteditcommons/karma.conf.js',
                singleRun: true
            },
            unitPersonalizationpromotionssmartedit: {
                configFile: 'jsTests/unit/personalizationpromotionssmartedit/karma.conf.js',
                singleRun: true
            },
            unitPersonalizationpromotionssmarteditcontainer: {
                configFile: 'jsTests/unit/personalizationpromotionssmarteditContainer/karma.conf.js',
                singleRun: true
            }
        }
    });

    grunt.file.setBase('./');

    grunt.registerTask('copyDev', function() {
        var copyDevTask = {
            dev: {
                expand: true,
                flatten: true,
                src: ['jsTarget/*.js'],
                dest: 'web/webroot/personalizationpromotionssmartedit/js'
            }
        };

        grunt.config.set('copy', copyDevTask);
        grunt.task.run('copy');
    });

    grunt.registerTask('concatStyling', function() {
        var concatStylingTask = {
            styling: {
                separator: ';',
                src: ["web/webroot/css/*.css"],
                dest: "web/webroot/css/style.css",
            }
        };

        grunt.config.set('concat', concatStylingTask);
        grunt.task.run('concat');
    });

    grunt.registerTask('multiCopySource', function() {
        var multiCopySourceTask = {};

        // read all subdirectories from your modules folder
        grunt.file.expand({
            filter: 'isDirectory'
        }, 'web/features/*').forEach(function(dir) {
            multiCopySourceTask[dir] = {
                expand: true,
                flatten: false,
                src: [dir + '/**/*.js'],
                dest: 'jsTarget/'
            };
        });
        grunt.config.set('copy', multiCopySourceTask);
        grunt.task.run('copy');
    });

    /*
     * generates angular.module('run').run(['$templateCache', function($templateCache) {}]) module
     * that contains template caches so that they become minifyable !!!
     */
    grunt.registerTask('multiNGTemplates', function() {
        var multiNGTemplatesTask = {};

        grunt.file.expand({
            filter: 'isDirectory'
        }, "web/features/*").forEach(function(dir) {
            var folderName = dir.replace("web/features/", "");

            multiNGTemplatesTask[folderName] = {
                src: [dir + '/**/*Template.html'],
                dest: 'jsTarget/' + dir + '/templates.js',
                options: {
                    standalone: true, //to declare a module as opposed to binding to an existing one
                    module: folderName + 'Templates'
                }
            };
        });
        grunt.config.set('ngtemplates', multiNGTemplatesTask);
        grunt.task.run('ngtemplates');
    });

    grunt.registerTask('multiConcat', function() {
        var multiConcatTask = {};

        grunt.file.expand({
            filter: 'isDirectory'
        }, "jsTarget/web/features/*").forEach(function(dir) {
            if (!endsWith(dir, "/commons")) {
                var folderName = dir.replace("jsTarget/web/features/", "");

                multiConcatTask[folderName] = {
                    src: ["jsTarget/web/features/personalizationpromotionssmarteditcommons/**/*.js", dir + '/**/*.js'],
                    dest: 'jsTarget/' + folderName + '.js'
                };

                grunt.config.set('concat', multiConcatTask);
            }
        });

        grunt.task.run('concat');
    });

    grunt.registerTask("unitPersonalizationpromotionssmartedit", 'Executes unit tests for personalizationpromotionssmartedit', function() {
        //if npmtestancillary is not present, phantomjs drivers won't be present

        if (grunt.file.expand({
                filter: 'isFile'
            }, phantomJSPattern).length > 0) {
            grunt.task.run('karma:unitCommons');
            grunt.task.run('karma:unitPersonalizationpromotionssmartedit');
            grunt.task.run('karma:unitPersonalizationpromotionssmarteditcontainer');
            return;
        } else {
            grunt.log.warn('karma:unitPersonalizationpromotionssmartedit grunt phase was not run since no phantomjs driver found under ' + phantomJSPattern);
        }
    });

    // Helper Functions
    function endsWith(inputStr, suffix) {
        return inputStr.match(suffix + "$");
    }

    grunt.registerTask('styling_only', ['less']);
    grunt.registerTask('styling', ['clean', 'styling_only']);

    grunt.registerTask('sanitize', ['jsbeautifier']);

    grunt.registerTask('compile_only', ['jshint', 'jsbeautifier', 'multiCopySource', 'styling_only', 'multiNGTemplates', 'ngAnnotate:run']);
    grunt.registerTask('compile', ['clean', 'compile_only']);

    grunt.registerTask('test_only', ['unitPersonalizationpromotionssmartedit']);
    grunt.registerTask('test', ['compile', 'test_only']);

    grunt.registerTask('dev_only', ['multiConcat', 'copyDev']);
    grunt.registerTask('dev', ['test', 'dev_only']);

    grunt.registerTask('package_only', ['multiConcat', 'uglify:dist', 'ngdocs']);
    grunt.registerTask('package', ['test', 'package_only']);
    grunt.registerTask('packageSkipTests', ['compile_only', 'package_only']);

    grunt.registerTask('full_dev', ['compile', 'package', 'copyDev']);
};
