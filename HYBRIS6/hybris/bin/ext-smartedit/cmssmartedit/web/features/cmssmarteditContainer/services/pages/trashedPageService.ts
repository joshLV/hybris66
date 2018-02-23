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

interface UriContext {
	CONTEXT_CATALOG: string;
	CONTEXT_CATALOG_VERSION: string;
}
/* @ngInject */
export class TrashedPageService {

	constructor(private cmsitemsRestService: any) {}

	getTrashedPagesCount(uriContext: UriContext) {

		const requestParams = {
			pageSize: 10,
			currentPage: 0,
			typeCode: 'AbstractPage',
			itemSearchParams: 'pageStatus:deleted',
			catalogId: uriContext.CONTEXT_CATALOG,
			catalogVersion: uriContext.CONTEXT_CATALOG_VERSION
		};
		return this.cmsitemsRestService.get(requestParams).then(function(result: any) {
			return result.pagination.totalCount;
		});
	}

}
