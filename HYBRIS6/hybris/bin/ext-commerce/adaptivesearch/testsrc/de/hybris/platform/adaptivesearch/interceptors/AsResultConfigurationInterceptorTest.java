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
package de.hybris.platform.adaptivesearch.interceptors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.CharEncoding;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class AsResultConfigurationInterceptorTest extends ServicelayerTransactionalTest
{
	private final static String CATALOG_ID = "hwcatalog";
	private final static String VERSION_STAGED = "Staged";
	private final static String VERSION_ONLINE = "Online";

	private static final String SIMPLE_SEARCH_CONF_UID = "simpleConfiguration";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private AsSearchConfigurationService asSearchConfigurationService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/interceptors/asResultConfigurationInterceptorTest.impex", CharEncoding.UTF_8);
	}

	@Test
	public void createPromotedItem() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedItemModel promotedItem = modelService.create(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(onlineCatalogVersion);
		promotedItem.setSearchConfiguration(searchConfiguration.get());
		promotedItem.setItem(searchConfiguration.get());

		// when
		modelService.save(promotedItem);

		// then
		assertEquals(onlineCatalogVersion, promotedItem.getCatalogVersion());
		assertNotNull(promotedItem.getUid());
		assertFalse(promotedItem.getUid().isEmpty());
	}

	@Test
	public void failToCreatePromotedItemWithWrongCatalogVersion() throws Exception
	{
		// given
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedItemModel promotedItem = modelService.create(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(stagedCatalogVersion);
		promotedItem.setSearchConfiguration(searchConfiguration.get());
		promotedItem.setItem(searchConfiguration.get());

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedItem);
	}

	@Test
	public void failToCreatePromotedItemWithoutItem() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedItemModel promotedItem = modelService.create(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(onlineCatalogVersion);
		promotedItem.setSearchConfiguration(searchConfiguration.get());

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedItem);
	}

	@Test
	public void createExcludedItem() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedItemModel excludedItem = modelService.create(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(onlineCatalogVersion);
		excludedItem.setSearchConfiguration(searchConfiguration.get());
		excludedItem.setItem(searchConfiguration.get());

		// when
		modelService.save(excludedItem);

		// then
		assertEquals(onlineCatalogVersion, excludedItem.getCatalogVersion());
		assertNotNull(excludedItem.getUid());
		assertFalse(excludedItem.getUid().isEmpty());
	}

	@Test
	public void failToCreateExcludedItemWithWrongCatalogVersion() throws Exception
	{
		// given
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedItemModel excludedItem = modelService.create(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(stagedCatalogVersion);
		excludedItem.setSearchConfiguration(searchConfiguration.get());
		excludedItem.setItem(searchConfiguration.get());

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(excludedItem);
	}

	@Test
	public void failToCreateExcludedItemWithoutItem() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedItemModel excludedItem = modelService.create(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(onlineCatalogVersion);
		excludedItem.setSearchConfiguration(searchConfiguration.get());

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(excludedItem);
	}

	@Test
	public void createMultipleResultConfigurations() throws Exception
	{
		// given
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedItemModel promotedItem = modelService.create(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(onlineCatalogVersion);
		promotedItem.setSearchConfiguration(searchConfiguration.get());
		promotedItem.setItem(stagedCatalogVersion);

		final AsExcludedItemModel excludedItem = modelService.create(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(onlineCatalogVersion);
		excludedItem.setSearchConfiguration(searchConfiguration.get());
		excludedItem.setItem(onlineCatalogVersion);

		// when
		modelService.save(promotedItem);
		modelService.save(excludedItem);

		// then
		assertEquals(onlineCatalogVersion, promotedItem.getCatalogVersion());
		assertNotNull(promotedItem.getUid());
		assertFalse(promotedItem.getUid().isEmpty());

		assertEquals(onlineCatalogVersion, excludedItem.getCatalogVersion());
		assertNotNull(excludedItem.getUid());
		assertFalse(excludedItem.getUid().isEmpty());
	}

	@Test
	public void failToCreateMultipleResultConfigurationsSameItem() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedItemModel promotedItem = modelService.create(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(onlineCatalogVersion);
		promotedItem.setSearchConfiguration(searchConfiguration.get());
		promotedItem.setItem(onlineCatalogVersion);

		final AsExcludedItemModel excludedItem = modelService.create(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(onlineCatalogVersion);
		excludedItem.setSearchConfiguration(searchConfiguration.get());
		excludedItem.setItem(onlineCatalogVersion);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedItem);
		modelService.save(excludedItem);
	}
}
