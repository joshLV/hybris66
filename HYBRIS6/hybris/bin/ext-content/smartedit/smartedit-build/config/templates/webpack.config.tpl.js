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
    const lodash = require('lodash');

    const baseWebpackConfig = {
        output: {
            path: path.resolve("./jsTarget/"),
            filename: '[name].js',
            sourceMapFilename: '[file].map'
        },
        resolve: {
            modules: [
                path.resolve(process.cwd(), './jsTarget/web/app'),
                path.resolve(process.cwd(), './jsTarget/web/features')
            ],
            extensions: ['.ts', '.js']
        },
        module: {
            rules: [{ // Keep this as the first element in the array. It is reference in the webpack.js config
                test: /\.ts$/,
                loader: 'awesome-typescript-loader'
            }]
        },
        stats: {
            colors: true,
            modules: true,
            reasons: true,
            errorDetails: true
        },
        bail: false // Bug, see: https://github.com/webpack/webpack/issues/708
    };

    const baseExternal = {
        "angular": "angular",
        "angular-route": "angular-route",
        "angular-translate": "angular-translate"
    };

    const devExternal = lodash.defaultsDeep({
        "jasmine": "jasmine",
        "testutils": "testutils",
        "angular-mocks": "angular-mocks"
    }, baseExternal);

    const prodWebpackConfig = Object.assign({
        devtool: 'none',
        externals: baseExternal
    }, baseWebpackConfig);


    const devWebpackConfig = Object.assign({
        devtool: 'source-map',
        externals: devExternal
    }, baseWebpackConfig);

    return {
        // if you change this object, please update the webpack.js in the bundel config
        devWebpackConfig: devWebpackConfig,
        prodWebpackConfig: prodWebpackConfig
    };
}();
