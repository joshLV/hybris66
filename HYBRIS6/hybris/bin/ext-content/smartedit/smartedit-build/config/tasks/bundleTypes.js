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

    const fs = require('fs-extra');
    /**
     * bundles types to be exposed to extensions of smartedit
     */
    grunt.registerTask('bundleTypes', 'bundles types to be exposed', function(target) {

        grunt.log.writeln(`bundling types to be exposed for ${target}`, "concat:" + target + "Types");

        // concatenating done outside because asynchronous
        //grunt.task.run("concat:" + target + "Types");

        let fileName = global.smartedit.bundlePaths.bundleRoot + "/@types/" + target + "/index.d.ts";
        // removing import statements
        let content = fs.readFileSync(fileName, 'utf-8');

        content = content.replace(/import.*$/gm, '');
        content = content.replace(/export declare const.*$/gm, '');
        content = content.replace(/export {.*$/gm, '');
        content = content.replace(/export declare class/gm, 'export class'); //because A 'declare' modifier cannot be used in an already ambient context.

        // surrounding by smarteditcommons module declaration
        content = "declare module '" + target + "' {\n" + content;
        content = "import * as angular from 'angular';\n" + content;
        content = content + "\n}";


        fs.outputFileSync(fileName, content);
    });

};
