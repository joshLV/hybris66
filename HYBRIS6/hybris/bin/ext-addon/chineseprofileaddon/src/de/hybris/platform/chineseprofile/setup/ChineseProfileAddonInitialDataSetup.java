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
package de.hybris.platform.chineseprofile.setup;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.chineseprofile.constants.ChineseprofileaddonConstants;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


/**
 *
 */
@SystemSetup(extension = ChineseprofileaddonConstants.EXTENSIONNAME)
public class ChineseProfileAddonInitialDataSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ChineseProfileAddonInitialDataSetup.class);

	public static final String ELECTRONICS = "electronics";
	public static final String MARKETPLACE = "marketplace";

	private static final String IMPORT_CMS_DATA = "import Neccessary CMS Content";
	private static final String IMPORT_EMAIL_CONTENT = "import Email Template";

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();
		params.add(createBooleanSystemSetupParameter(IMPORT_CMS_DATA, "Import Neccessary CMS Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_EMAIL_CONTENT, "Import Email Template", true));
		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.UPDATE)
	public void createEssentialData(final SystemSetupContext context)
	{

		BaseSiteModel electronicsSite = baseSiteService.getBaseSiteForUID(ELECTRONICS);
		BaseSiteModel marketplaceSite = baseSiteService.getBaseSiteForUID(MARKETPLACE);
		final List<ImportData> importData = new ArrayList<>();

		if (electronicsSite != null)
		{
			addImportData(importData, ELECTRONICS);
		}
		if (marketplaceSite != null)
		{
			addImportData(importData, MARKETPLACE);
		}
		if (getBooleanSystemSetupParameter(context, IMPORT_CMS_DATA))
		{
			importSampleContentCatalog(context, importData);
		}
		updateEmailTemplate(context, importData);
	}

	private void importSampleContentCatalog(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				getSetupImpexService().importImpexFile(String.format("/%s/import/contentCatalogs/%sContentCatalog/cms-content.impex",
						extensionName, contentCatalogName), false);
			}
		}
		synchronizeContentCatalog(this, context, importData);
	}

	private void synchronizeContentCatalog(final AbstractSystemSetup systemSetup, final SystemSetupContext context,
			final List<ImportData> importData)
	{
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				systemSetup.logInfo(context, String.format("Begin synchronizing Content Catalog [%s]", contentCatalogName));
				getSetupSyncJobService().createContentCatalogSyncJob(String.format("%sContentCatalog", contentCatalogName));
				final PerformResult syncCronJobResult = getSetupSyncJobService()
						.executeCatalogSyncJob(String.format("%sContentCatalog", contentCatalogName));
				if (isSyncRerunNeeded(syncCronJobResult))
				{
					systemSetup.logInfo(context, String.format("Content Catalog [%s] sync has issues.", contentCatalogName));
				}
			}
		}
	}

	public void updateEmailTemplate(final SystemSetupContext context, final List<ImportData> importData)
	{
		final String extensionName = context.getExtensionName();
		for (final ImportData data : importData)
		{
			for (final String contentCatalogName : data.getContentCatalogNames())
			{
				getSetupImpexService().importImpexFile(String.format(
						"/%s/import/contentCatalogs/%sContentCatalog/email-content.impex", extensionName, contentCatalogName), false);
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
