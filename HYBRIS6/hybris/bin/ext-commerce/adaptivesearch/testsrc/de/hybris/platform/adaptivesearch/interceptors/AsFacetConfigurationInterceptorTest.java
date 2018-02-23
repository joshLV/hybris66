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
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel;
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
public class AsFacetConfigurationInterceptorTest extends ServicelayerTransactionalTest
{
	private final static String CATALOG_ID = "hwcatalog";
	private final static String VERSION_STAGED = "Staged";
	private final static String VERSION_ONLINE = "Online";

	private static final String SIMPLE_SEARCH_CONF_UID = "simpleConfiguration";

	private static final String INDEX_PROPERTY_1 = "property1";
	private static final String INDEX_PROPERTY_2 = "property2";
	private static final String INDEX_PROPERTY_3 = "property3";
	private static final String INDEX_PROPERTY_4 = "property4";
	private static final String WRONG_INDEX_PROPERTY = "testPropertyError";

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
	public void createPromotedFacet() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedFacetModel promotedFacet = modelService.create(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(onlineCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration.get());
		promotedFacet.setIndexProperty(INDEX_PROPERTY_1);

		// when
		modelService.save(promotedFacet);

		// then
		assertEquals(onlineCatalogVersion, promotedFacet.getCatalogVersion());
		assertNotNull(promotedFacet.getUid());
		assertFalse(promotedFacet.getUid().isEmpty());
	}

	@Test
	public void failToCreatePromotedFacetWithWrongCatalogVersion() throws Exception
	{
		// given
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedFacetModel promotedFacet = modelService.create(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(stagedCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration.get());
		promotedFacet.setIndexProperty(INDEX_PROPERTY_1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedFacet);
	}

	@Test
	public void failToCreatePromotedFacetWithoutIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedFacetModel promotedFacet = modelService.create(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(onlineCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration.get());

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedFacet);
	}

	@Test
	public void failToCreatePromotedFacetWithWrongIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedFacetModel promotedFacet = modelService.create(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(onlineCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration.get());
		promotedFacet.setIndexProperty(WRONG_INDEX_PROPERTY);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedFacet);
	}

	@Test
	public void failToCreatePromotedFacetWithWrongIndexPropertyType() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedFacetModel promotedFacet = modelService.create(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(onlineCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration.get());
		promotedFacet.setIndexProperty(INDEX_PROPERTY_4);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedFacet);
	}

	@Test
	public void createFacet() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(onlineCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());
		facet.setIndexProperty(INDEX_PROPERTY_1);

		// when
		modelService.save(facet);

		// then
		assertEquals(onlineCatalogVersion, facet.getCatalogVersion());
		assertNotNull(facet.getUid());
		assertFalse(facet.getUid().isEmpty());
	}

	@Test
	public void failToCreateFacetWithWrongCatalogVersion() throws Exception
	{
		// given
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(stagedCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());
		facet.setIndexProperty(INDEX_PROPERTY_1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(facet);
	}

	@Test
	public void failToCreateFacetWithoutIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(onlineCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(facet);
	}

	@Test
	public void failToCreateFacetWithWrongIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(onlineCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());
		facet.setIndexProperty(WRONG_INDEX_PROPERTY);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(facet);
	}

	@Test
	public void failToCreateFacetWithWrongIndexPropertyType() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(onlineCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());
		facet.setIndexProperty(INDEX_PROPERTY_4);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(facet);
	}

	@Test
	public void createExcludedFacet() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedFacetModel excludedFacet = modelService.create(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(onlineCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration.get());
		excludedFacet.setIndexProperty(INDEX_PROPERTY_1);

		// when
		modelService.save(excludedFacet);

		// then
		assertEquals(onlineCatalogVersion, excludedFacet.getCatalogVersion());
		assertNotNull(excludedFacet.getUid());
		assertFalse(excludedFacet.getUid().isEmpty());
	}

	@Test
	public void failToCreateExcludedFacetWithWrongCatalogVersion() throws Exception
	{
		// given
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedFacetModel excludedFacet = modelService.create(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(stagedCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration.get());
		excludedFacet.setIndexProperty(INDEX_PROPERTY_1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(excludedFacet);
	}

	@Test
	public void failToCreateExcludedFacetWithoutIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedFacetModel excludedFacet = modelService.create(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(onlineCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration.get());

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(excludedFacet);
	}

	@Test
	public void failToCreateExcludedFacetWithWrongIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedFacetModel excludedFacet = modelService.create(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(onlineCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration.get());
		excludedFacet.setIndexProperty(WRONG_INDEX_PROPERTY);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(excludedFacet);
	}

	@Test
	public void failToCreateExcludedFacetWithWrongIndexPropertyType() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsExcludedFacetModel excludedFacet = modelService.create(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(onlineCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration.get());
		excludedFacet.setIndexProperty(INDEX_PROPERTY_4);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(excludedFacet);
	}

	@Test
	public void createMultipleFacetConfigurations() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedFacetModel promotedFacet = modelService.create(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(onlineCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration.get());
		promotedFacet.setIndexProperty(INDEX_PROPERTY_1);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(onlineCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());
		facet.setIndexProperty(INDEX_PROPERTY_2);

		final AsExcludedFacetModel excludedFacet = modelService.create(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(onlineCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration.get());
		excludedFacet.setIndexProperty(INDEX_PROPERTY_3);

		// when
		modelService.save(promotedFacet);
		modelService.save(facet);
		modelService.save(excludedFacet);

		// then
		assertEquals(onlineCatalogVersion, promotedFacet.getCatalogVersion());
		assertNotNull(promotedFacet.getUid());
		assertFalse(promotedFacet.getUid().isEmpty());

		assertEquals(onlineCatalogVersion, facet.getCatalogVersion());
		assertNotNull(facet.getUid());
		assertFalse(facet.getUid().isEmpty());

		assertEquals(onlineCatalogVersion, excludedFacet.getCatalogVersion());
		assertNotNull(excludedFacet.getUid());
		assertFalse(excludedFacet.getUid().isEmpty());
	}

	@Test
	public void failToCreateMultipleFacetConfigurationsSameIndexProperty1() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsPromotedFacetModel promotedFacet = modelService.create(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(onlineCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration.get());
		promotedFacet.setIndexProperty(INDEX_PROPERTY_1);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(onlineCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());
		facet.setIndexProperty(INDEX_PROPERTY_1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(promotedFacet);
		modelService.save(facet);
	}

	@Test
	public void failToCreateMultipleFacetConfigurationsSameIndexProperty2() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsFacetModel facet = modelService.create(AsFacetModel.class);
		facet.setCatalogVersion(onlineCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration.get());
		facet.setIndexProperty(INDEX_PROPERTY_2);

		final AsExcludedFacetModel excludedFacet = modelService.create(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(onlineCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration.get());
		excludedFacet.setIndexProperty(INDEX_PROPERTY_2);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(facet);
		modelService.save(excludedFacet);
	}
}
