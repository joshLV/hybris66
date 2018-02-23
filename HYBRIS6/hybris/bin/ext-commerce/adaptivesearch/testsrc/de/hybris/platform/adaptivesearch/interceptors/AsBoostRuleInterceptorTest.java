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
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
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
public class AsBoostRuleInterceptorTest extends ServicelayerTransactionalTest
{
	private final static String CATALOG_ID = "hwcatalog";
	private final static String VERSION_STAGED = "Staged";
	private final static String VERSION_ONLINE = "Online";

	private static final String SIMPLE_SEARCH_CONF_UID = "simpleConfiguration";

	private static final String INDEX_PROPERTY = "property1";
	private static final String INDEX_PROPERTY_3 = "property3";
	private static final String WRONG_INDEX_PROPERTY = "testPropertyError";

	private static final String VALUE = "value1";
	private static final Float BOOST = Float.valueOf(10f);

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
		importCsv("/adaptivesearch/test/interceptors/asBoostRuleInterceptorTest.impex", CharEncoding.UTF_8);
	}

	@Test
	public void createBoostRule() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsBoostRuleModel boostRule = modelService.create(AsBoostRuleModel.class);
		boostRule.setCatalogVersion(onlineCatalogVersion);
		boostRule.setSearchConfiguration(searchConfiguration.get());
		boostRule.setIndexProperty(INDEX_PROPERTY);
		boostRule.setOperator(AsBoostOperator.EQUAL);
		boostRule.setValue(VALUE);
		boostRule.setBoost(BOOST);

		// when
		modelService.save(boostRule);

		// then
		assertEquals(onlineCatalogVersion, boostRule.getCatalogVersion());
		assertNotNull(boostRule.getUid());
		assertFalse(boostRule.getUid().isEmpty());
	}

	@Test
	public void failToCreateBoostRuleWithWrongCatalogVersion() throws Exception
	{
		// given
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsBoostRuleModel boostRule = modelService.create(AsBoostRuleModel.class);
		boostRule.setCatalogVersion(stagedCatalogVersion);
		boostRule.setSearchConfiguration(searchConfiguration.get());
		boostRule.setIndexProperty(INDEX_PROPERTY);
		boostRule.setValue(VALUE);
		boostRule.setOperator(AsBoostOperator.EQUAL);
		boostRule.setBoost(BOOST);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(boostRule);
	}

	@Test
	public void failToCreateBoostRuleWithoutIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsBoostRuleModel boostRule = modelService.create(AsBoostRuleModel.class);
		boostRule.setCatalogVersion(onlineCatalogVersion);
		boostRule.setSearchConfiguration(searchConfiguration.get());
		boostRule.setValue(VALUE);
		boostRule.setOperator(AsBoostOperator.EQUAL);
		boostRule.setBoost(BOOST);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(boostRule);
	}

	@Test
	public void failToCreateBoostRuleWithWrongIndexProperty() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsBoostRuleModel boostRule = modelService.create(AsBoostRuleModel.class);
		boostRule.setCatalogVersion(onlineCatalogVersion);
		boostRule.setSearchConfiguration(searchConfiguration.get());
		boostRule.setIndexProperty(WRONG_INDEX_PROPERTY);
		boostRule.setValue(VALUE);
		boostRule.setOperator(AsBoostOperator.EQUAL);
		boostRule.setBoost(BOOST);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(boostRule);
	}

	@Test
	public void failToCreateBoostRuleWithWrongBoostOperator() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsBoostRuleModel boostRule = modelService.create(AsBoostRuleModel.class);
		boostRule.setCatalogVersion(onlineCatalogVersion);
		boostRule.setSearchConfiguration(searchConfiguration.get());
		boostRule.setIndexProperty(INDEX_PROPERTY);
		boostRule.setOperator(AsBoostOperator.GREATER_THAN);
		boostRule.setValue(VALUE);
		boostRule.setBoost(BOOST);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(boostRule);
	}

	@Test
	public void failToCreateBoostRuleWithWrongValue() throws Exception
	{
		// given
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);
		final Optional<AsSimpleSearchConfigurationModel> searchConfiguration = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, SIMPLE_SEARCH_CONF_UID);

		final AsBoostRuleModel boostRule = modelService.create(AsBoostRuleModel.class);
		boostRule.setCatalogVersion(onlineCatalogVersion);
		boostRule.setSearchConfiguration(searchConfiguration.get());
		boostRule.setIndexProperty(INDEX_PROPERTY_3);
		boostRule.setOperator(AsBoostOperator.GREATER_THAN);
		boostRule.setValue(VALUE);
		boostRule.setBoost(BOOST);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(boostRule);
	}
}
