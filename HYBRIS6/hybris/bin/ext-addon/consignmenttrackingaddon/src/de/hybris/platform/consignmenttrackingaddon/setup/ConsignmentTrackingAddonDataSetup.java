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
package de.hybris.platform.consignmenttrackingaddon.setup;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.consignmenttrackingaddon.constants.ConsignmenttrackingaddonConstants;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;


/**
 * Import data when update under the HAC
 */
@SystemSetup(extension = ConsignmenttrackingaddonConstants.EXTENSIONNAME)
public class ConsignmentTrackingAddonDataSetup extends AbstractSystemSetup
{

	private static final String ELECTRONICS = "electronics";
	private static final String MARKETPLACE = "marketplace";
	public static final String POWERTOOLS = "powertools";
	private static final String IMPORT_CMS_DATA = "import Neccessary CMS Content";

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_CMS_DATA, "Import Neccessary CMS Data", true));
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.UPDATE)
	public void createEssentialData(final SystemSetupContext context)
	{

		BaseSiteModel electronicsSite = baseSiteService.getBaseSiteForUID(ELECTRONICS);
		BaseSiteModel marketplaceSite = baseSiteService.getBaseSiteForUID(MARKETPLACE);
		BaseSiteModel powertoolsSite = baseSiteService.getBaseSiteForUID(POWERTOOLS);
		final List<ImportData> importData = new ArrayList<ImportData>();

		if (electronicsSite != null)
		{
			addImportData(importData, ELECTRONICS);
		}
		if (marketplaceSite != null)
		{
			addImportData(importData, MARKETPLACE);
		}
		if (powertoolsSite != null) {
			addImportData(importData, POWERTOOLS);
		}
		if (getBooleanSystemSetupParameter(context, IMPORT_CMS_DATA))
		{
			importSampleContentCatalog(context, importData);
		}
		getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
	}

	protected void importSampleContentCatalog(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				getSetupImpexService().importImpexFile(
						String.format("/%s/import/contentCatalogs/%sContentCatalog/cms-content.impex", extensionName,
								contentCatalogName), false);
			}
		}
		synchronizeContentCatalog(this, context, importData);
	}


	protected void synchronizeContentCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final List<ImportData> importData)
	{
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				systemSetup.logInfo(context, String.format("Begin synchronizing Content Catalog [%s]", contentCatalogName));
				getSetupSyncJobService().createContentCatalogSyncJob(String.format("%sContentCatalog", contentCatalogName));
				final PerformResult syncCronJobResult = getSetupSyncJobService().executeCatalogSyncJob(
						String.format("%sContentCatalog", contentCatalogName));
				if (isSyncRerunNeeded(syncCronJobResult))
				{
					systemSetup.logInfo(context, String.format("Content Catalog [%s] sync has issues.", contentCatalogName));
				}
			}
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
	protected void addImportData(List<ImportData> importDataList, String site)
	{
		final ImportData importData = new ImportData();

		importData.setProductCatalogName(site);
		importData.setContentCatalogNames(Arrays.asList(site));
		importData.setStoreNames(Arrays.asList(site));

		importDataList.add(importData);
	}

}
