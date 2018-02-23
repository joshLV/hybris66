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
package de.hybris.platform.adaptivesearch.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.util.ConfigurationUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultAsSearchProfileResultFactoryTest
{
	private DefaultAsSearchProfileResultFactory strategy;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(configurationService.getConfiguration()).thenReturn(configuration);

		strategy = new DefaultAsSearchProfileResultFactory();
		strategy.setConfigurationService(configurationService);
	}

	@Test
	public void create()
	{
		// given
		when(configuration.getString(ConfigurationUtils.DEFAULT_FACETS_MERGE_MODE, AsFacetsMergeMode.ADD_AFTER.name()))
				.thenReturn(AsFacetsMergeMode.ADD_AFTER.name());
		when(configuration.getString(ConfigurationUtils.DEFAULT_BOOST_ITEMS_MERGE_MODE, AsBoostItemsMergeMode.ADD_AFTER.name()))
				.thenReturn(AsBoostItemsMergeMode.ADD_AFTER.name());
		when(configuration.getString(ConfigurationUtils.DEFAULT_BOOST_RULES_MERGE_MODE, AsBoostRulesMergeMode.ADD.name()))
				.thenReturn(AsBoostRulesMergeMode.ADD.name());

		// when
		final AsSearchProfileResult result = strategy.createResult();

		// then
		assertNotNull(result);
		assertEquals(AsFacetsMergeMode.ADD_AFTER, result.getFacetsMergeMode());
		assertNotNull(result.getPromotedFacets());
		assertNotNull(result.getFacets());
		assertNotNull(result.getExcludedFacets());
		assertEquals(AsBoostItemsMergeMode.ADD_AFTER, result.getBoostItemsMergeMode());
		assertNotNull(result.getPromotedItems());
		assertNotNull(result.getExcludedItems());
		assertEquals(AsBoostRulesMergeMode.ADD, result.getBoostRulesMergeMode());
		assertNotNull(result.getBoostRules());
	}
}