export interface ICatalog {
	name: string;
	uuid?: string;
}

export interface ICatalogService {
	/**
	 * list catalogs for a given site
	 *
	 * @param {=string} siteId the site identifier
	 *
	 * @return {ng.IPromise<ICatalog[]>} list of catalogs for the given site
	 */
	getCatalogs(siteId?: string): ng.IPromise<ICatalog[]>;
}
