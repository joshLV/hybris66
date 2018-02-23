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

    var tsfmt = require('typescript-formatter');

    grunt.registerTask('tsformatter', 'TypeScript code formatter', function() {
        var done = this.async();
        var gruntConfig = grunt.config.get("tsformatter");
        var files = grunt.file.expand(gruntConfig.files);
        grunt.log.writeln("tsformatter - processing " + files.length.toString().cyan + " files.");
        tsfmt.processFiles(files, gruntConfig.options).then(function() {
            grunt.log.ok();
            done();
        });
    });
};
