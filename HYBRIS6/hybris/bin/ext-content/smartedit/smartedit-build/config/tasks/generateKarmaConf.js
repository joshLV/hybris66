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
    const serialize = require('serialize-javascript');


    const taskName = 'generateKarmaConf';

    function validateConfig(config) {
        if (!config.data) {
            grunt.fail.fatal(`${taskName} - invalid config, [data] param is required`);
        }
        if (!config.dest) {
            grunt.fail.fatal(`${taskName} - invalid config, [dest] param is required`);
        }
    }


    grunt.registerMultiTask(taskName, function() {

        grunt.verbose.writeln(`${taskName} config: ${JSON.stringify(this.data)}`);

        validateConfig(this.data);

        const config = this.data;

        // const outputContent = JSON.stringify(config.data, null, 4);
        const tpl =
            `module.exports = function(config) {
    config.set(
        ${serialize(config.data, {space: 4})}
    );
};`;

        // WRITE
        grunt.log.writeln(`Writting to: ${config.dest}`);
        fs.outputFileSync(config.dest, tpl);

    });

};