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
package de.hybris.platform.cmsfacades.catalogversiondetails;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.CatalogVersionData;


/**
 * simplification of CatalogVersionDetailData related interactions.
 *
 * @deprecated since version 6.4. To retrieve the catalog and catalog information, please use the {@code CatalogFacade}
 *             in the cmssmarteditwebservices extension instead.
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public interface CatalogVersionDetailFacade
{
	/**
	 * Given a site uid this method will return a List of all the configured pairs of permitted content catalogs and versions.
	 * @deprecated since version 6.3, use {@link CatalogVersionDetailFacade#getContentCatalogVersionDetailDataForSite(String)}
	 * @param siteUid
	 *           the site uid
	 *
	 * @return All catalog and version pairs that are configured for a site; never <tt>null</tt>
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.3")
	CatalogVersionData getCatalogVersionDetailDataForSite(String siteUid);

	/**
	 * Given a site uid this method will return a List of all the configured pairs of permitted content catalogs and
	 * versions.
	 *
	 * @deprecated Deprecated since 6.4.
	 *             <p>
	 *             To retrieve the catalog and catalog information, please use the {@code CatalogFacade} in the
	 *             cmssmarteditwebservices extension instead.
	 *             <p>
	 *             Otherwise, to retrieve only the catalog information, please use the
	 *             {@link de.hybris.platform.cmsfacades.catalogs.CatalogFacade#getContentCatalogs(String)} instead.
	 * @param siteUid
	 *           the site uid
	 *
	 * @return All catalog and version pairs that are configured for a site; never <tt>null</tt>
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.4")
	CatalogVersionData getContentCatalogVersionDetailDataForSite(String siteUid);

	/**
	 * Given a site uid this method will return a List of all the configured pairs of permitted product catalogs and
	 * versions.
	 *
	 * @deprecated Deprecated since 6.4.
	 *             <p>
	 *             To retrieve the catalog and catalog information, please use the {@code CatalogFacade} in the
	 *             cmssmarteditwebservices extension instead.
	 *             <p>
	 *             Otherwise, to retrieve only the catalog information, please use the
	 *             {@link de.hybris.platform.cmsfacades.catalogs.CatalogFacade#getProductCatalogs(String)} instead.
	 *
	 * @param siteUid
	 *           the site uid
	 *
	 * @return All catalog and version pairs that are configured for a site; never <tt>null</tt>
	 */
	@Deprecated
	@HybrisDeprecation(sinceVersion = "6.4")
	CatalogVersionData getProductCatalogVersionDetailDataForSite(String siteUid);

}
