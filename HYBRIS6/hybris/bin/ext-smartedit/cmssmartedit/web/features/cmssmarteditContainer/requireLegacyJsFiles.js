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
var cache = {};

function importAll(requireContext) {
    requireContext
        .keys()
        .forEach(function(key) {
            cache[key] = requireContext(key);
        });
}

importAll(require.context('./', true, /\.js$/));
importAll(require.context('../cmscommons', true, /\.js$/));
importAll(require.context('../../', true, /.*\/featureExtensions\/.*\/cmssmarteditContainer\/.*\.js$/));