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

    require('time-grunt')(grunt);
    require('./smartedit-build')(grunt).load();


    // -------------------------------------------------------------------------------------------------
    // FILE GENERATION
    grunt.registerTask('generate', [
        'generateWebpackConfig',
        'generateTsConfig',
        'generateKarmaConf',
        'generateSmarteditIndexHtml:landingPage',
        'generateSmarteditIndexHtml:smarteditE2e',
        'generateStorefrontIndexHtml',
        'concat:unitUtilsForBundle',
        'copy:ckeditor',
        'copy:images',
        'ts:rules',
    ]);

    // -------------------------------------------------------------------------------------------------
    // LINTING + SANITIZING
    grunt.registerTask('formatCode', [
        'jsbeautifier',
        'tsformatter',
        'jshint',
        'tslint',
        'checkNoForbiddenNameSpaces',
        'checkI18nKeysCompliancy',
        'checkNoFocus'
    ]);

    // -------------------------------------------------------------------------------------------------
    // PREPARE JSTARGET
    grunt.registerTask('prepareJsTarget_Base', [
        'clean:target',
        'copy:sources',
        'ngtemplates:run',
        'uglify:uglifyThirdparties',
        'concat:containerAdministrationModule',
        'concat:smarteditloader',
        'concat:smarteditcontainer',
        'concat:smartEditSystemModule',
        'concat:presmartedit',
        'concat:webApplicationInjector',
        'uglify:sanitizeWebApplicationInjectorForDummystorefront'
    ]);
    grunt.registerTask('prepareJsTarget_Dev', ['prepareJsTarget_Base', 'webpack:devSmartedit', 'webpack:devSmarteditContainer']);
    grunt.registerTask('prepareJsTarget_Prod', ['prepareJsTarget_Base', 'webpack:prodSmartedit', 'webpack:prodSmarteditContainer', 'ngAnnotate:run', 'ngdocs']);

    // -------------------------------------------------------------------------------------------------
    // TEST
    grunt.registerTask('unit', ['karma:unitSmartedit', 'karma:unitSmarteditContainer']);
    grunt.registerTask('test_only', ['generate', 'unit']); // Legacy - see yunit macro in buildcallbacks.xml
    grunt.registerTask('e2e', ['connect:test', 'protractorRun']);
    grunt.registerTask('e2e_max', ['connect:test', 'protractorMaxrun']);

    // -------------------------------------------------------------------------------------------------
    // PREPARE WEBROOT
    grunt.registerTask('prepareWebroot_Base', [
        'clean:webroot',
        'copy:modules',
        'copy:dev',
        'less',
        'postcss',
        'concat:outerStyling',
        'clean:postConcat'
    ]);

    grunt.registerTask('prepareWebroot_Dev', ['prepareWebroot_Base', 'concat:containerThirdpartiesDev', 'concat:smarteditThirdpartiesDev']);
    grunt.registerTask('prepareWebroot_Prod', ['prepareWebroot_Base', 'concat:containerThirdparties', 'concat:smarteditThirdparties', 'uglify:dist']);

    // -------------------------------------------------------------------------------------------------
    // PREPARE BUNDLE
    grunt.registerTask('declareTypes', ['concat:smarteditcommonsTypes', 'bundleTypes:smarteditcommons', 'concat:smarteditTypes', 'bundleTypes:smartedit', 'concat:smarteditcontainerTypes', 'bundleTypes:smarteditcontainer']);

    grunt.registerTask('prepareBundle', [
        'ts:rules',
        'clean:bundleForNewSymlinks',
        'declareTypes',
        'symlink:appToBundle',
        'copy:toDummystorefront'
    ]);


    grunt.registerTask('prod', ['formatCode', 'prepareJsTarget_Prod', 'prepareWebroot_Prod']);
    grunt.registerTask('dev', ['formatCode', 'prepareJsTarget_Dev', 'prepareWebroot_Dev']);

    grunt.registerTask('packageSkipTests', ['generate', 'prod', 'prepareBundle']);

    grunt.registerTask('verify', ['generate', 'prod', 'prepareBundle', 'unit', 'e2e']);
    grunt.registerTask('verify_max', ['generate', 'prod', 'prepareBundle', 'unit', 'e2e_max']);

    // Please use prod or dev, these should only be used for watch: tasks
    // because people keep comiting code that has not been ran through 'formatCode' task
    grunt.registerTask('test', ['prepareJsTarget_Dev', 'unit']);
    grunt.registerTask('package', ['prepareJsTarget_Prod', 'prepareWebroot_Prod']);
    grunt.registerTask('packageDev', ['prepareJsTarget_Dev', 'prepareWebroot_Dev']);

};