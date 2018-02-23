// Karma configuration
// Generated on Sat Jul 05 2014 07:57:17 GMT-0400 (EDT)

module.exports = function(config) {
    config.set({

        basePath: '../../../',

        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine'],

        decorators: [
            'karma-phantomjs-launcher',
            'karma-jasmine',
            'karma-ng-html2js-preprocessor'
        ],

        preprocessors: {
            //'web/**/*.html': 'ng-html2js',
        },

        coverageReporter: {
            // specify a common output directory
            dir: 'jsTarget/test/personalizationsmarteditcontainer/coverage/',
            reporters: [{
                type: 'html',
                subdir: 'report-html'
            }, {
                type: 'cobertura',
                subdir: '.',
                file: 'cobertura.xml'
            }, ]
        },

        junitReporter: {
            outputDir: 'jsTarget/test/personalizationsmarteditcontainer/junit/', // results will be saved as $outputDir/$browserName.xml
            outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
            //suite: '' // suite will become the package name attribute in xml testsuite element
        },

        // list of files / patterns to load in the browser
        files: [
            'build/bundle/webroot/static-resources/dist/smartedit/js/thirdparties.js',
            'build/bundle/webroot/static-resources/thirdparties/ckeditor/ckeditor.js',
            'build/bundle/test/unitUtils.js',
            
            'web/**/*.html',
            'web/webroot/css/style.css',
            'web/features/personalizationcommons/**/*.js',
            'web/features/personalizationsmarteditcontainer/**/*.js',
            'jsTarget/web/features/personalizationcommons/templates.js',
            'jsTarget/web/features/personalizationsmarteditcontainer/templates.js',
            'jsTests/unit/testSetup.js',
            'jsTests/unit/testsModule.js',
            'jsTests/unit/mockModules.js',
            'jsTests/unit/personalizationsmarteditcontainer/*Test.js'
        ],

        // list of files to exclude
        exclude: [],

        proxies: {
            '/personalizationsmartedit/css/style.css': 'http://localhost:9876/base/web/webroot/css/style.css'
        },

        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        // coverage reporter generates the coverage
        reporters: ['progress', 'junit'], // 'coverage' interferes with gatewayProxy and proxies empty methods when it should not

        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['PhantomJS'], //Chrome


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false
    });
};
