/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.verticalnavigationaddon.setup;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.verticalnavigationaddon.constants.VerticalnavigationaddonConstants;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;


/**
 *
 */
@SystemSetup(extension = VerticalnavigationaddonConstants.EXTENSIONNAME)
public class VerticalNavigationInitialDataSetup extends AbstractSystemSetup
{
	private static final String ELECTRONICS = "electronics";
	private static final String MARKETPLACE = "marketplace";
	private static final String IMPORT_CONTENT_DATA = "Import Content Data";
	private static final String IMPORT_PRODUCT_DATA = "Import Product Data";
	private static final String CATALOG_TYPE_CONTENT = "Content";
	private static final String CATALOG_TYPE_PRODUCT = "Product";
	private static final String MARKETPLACE_PRODUCT_CATALOG_PREFIX = "globalMarketplace";

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_CONTENT_DATA, "Import Content Sample Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_PRODUCT_DATA, "Import Product Sample Data", true));
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		final BaseSiteModel electronicsSite = baseSiteService.getBaseSiteForUID(ELECTRONICS);
		final BaseSiteModel marketplaceSite = baseSiteService.getBaseSiteForUID(MARKETPLACE);
		final List<ImportData> importData = new ArrayList<ImportData>();

		if (electronicsSite != null)
		{
			addImportData(importData, ELECTRONICS);
		}
		if (marketplaceSite != null)
		{
			addImportData(importData, MARKETPLACE);
		}
		importSampleCatalog(context, importData);
	}

	/**
	 * import sample data for content & product catalog
	 *
	 * @param context
	 * @param importData
	 */
	private void importSampleCatalog(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				getSetupImpexService().importImpexFile(String.format("/%s/import/contentCatalogs/%sContentCatalog/cms-content.impex",
						extensionName, contentCatalogName), false);
			}
			getSetupImpexService()
					.importImpexFile(String.format("/%s/import/productCatalogs/%sProductCatalog/categories-media.impex", extensionName,
							data.getProductCatalogName()), false);
		}
		synchronizeCatalogs(this, context, importData);
	}

	/**
	 * synchronize content & product catalogs
	 *
	 * @param systemSetup
	 * @param context
	 * @param importData
	 */
	private void synchronizeCatalogs(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final List<ImportData> importData)
	{
		for (final ImportData data : importData)
		{
			synchronizeCatalog(systemSetup, context, data.getProductCatalogName(), CATALOG_TYPE_PRODUCT);
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				synchronizeCatalog(systemSetup, context, contentCatalogName, CATALOG_TYPE_CONTENT);
			}
		}
	}

	/**
	 * synchronize a catalog item
	 *
	 * @param systemSetup
	 * @param context
	 * @param catalogName
	 * @param catalogType
	 */
	private void synchronizeCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final String catalogName, final String catalogType)
	{

		systemSetup.logInfo(context, MessageFormat.format("Begin synchronizing {0} Catalog [{1}]", catalogType, catalogName));
		final String catalogId = MessageFormat.format("{0}{1}Catalog", catalogName, catalogType);
		if (catalogType.equals(CATALOG_TYPE_CONTENT))
		{
			getSetupSyncJobService().createContentCatalogSyncJob(catalogId);
		}
		if (catalogType.equals(CATALOG_TYPE_PRODUCT))
		{
			getSetupSyncJobService().createProductCatalogSyncJob(catalogId);
		}
		final PerformResult syncCronJobResult = getSetupSyncJobService().executeCatalogSyncJob(catalogId);
		if (isSyncRerunNeeded(syncCronJobResult))
		{
			systemSetup.logInfo(context, MessageFormat.format("{0} Catalog [{1}] sync has issues.", catalogType, catalogName));
		}
	}

	/**
	 * add importData to importDatalist
	 *
	 * @param importDataList
	 *           is used for setting parameter of ProductCatalogName,ContentCatalogName,StoreName. That is used for
	 *           import data.
	 * @param site
	 *           is parameter for chose which site's data should be import.
	 */
	protected void addImportData(final List<ImportData> importDataList, final String site)
	{
		final String productCatalogName = site.equals(MARKETPLACE) ? MARKETPLACE_PRODUCT_CATALOG_PREFIX : site;
		final ImportData importData = new ImportData();
		importData.setProductCatalogName(productCatalogName);
		importData.setContentCatalogNames(Arrays.asList(site));
		importData.setStoreNames(Arrays.asList(site));
		importDataList.add(importData);
	}
}
