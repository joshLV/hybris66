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
package de.hybris.platform.adaptivesearch.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContextFactory;
import de.hybris.platform.adaptivesearch.data.AsBoostRule;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacet;
import de.hybris.platform.adaptivesearch.data.AsExcludedItem;
import de.hybris.platform.adaptivesearch.data.AsFacet;
import de.hybris.platform.adaptivesearch.data.AsMergeConfiguration;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacet;
import de.hybris.platform.adaptivesearch.data.AsPromotedItem;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileCalculationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.regioncache.CacheStatistics;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import javax.annotation.Resource;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultAsSearchProfileCalculationServiceTest extends ServicelayerTransactionalTest
{
	private static final String INDEX_CONFIGURATION = "indexConfiguration";
	private static final String INDEX_TYPE = "index1";

	private static final String SIMPLE_SEARCH_PROFILE_1_CODE = "simpleProfile1";
	private static final String SIMPLE_SEARCH_PROFILE_2_CODE = "simpleProfile2";
	private static final String CATEGORY_AWARE_SEARCH_PROFILE_1_CODE = "categoryAwareProfile1";
	private static final String CATEGORY_AWARE_SEARCH_PROFILE_2_CODE = "categoryAwareProfile2";

	private static final String PROPERTY1 = "property1";
	private static final String PROPERTY2 = "property2";
	private static final String PROPERTY3 = "property3";

	private static final String PRODUCT1_CODE = "product1";
	private static final String PRODUCT2_CODE = "product2";
	private static final String PRODUCT3_CODE = "product3";

	private static final String BOOST_VALUE = "value";

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductService productService;

	@Resource
	private AsSearchProfileService asSearchProfileService;

	@Resource
	private AsSearchProfileCalculationService asSearchProfileCalculationService;

	@Resource
	private AsSearchProfileContextFactory asSearchProfileContextFactory;

	@Resource
	private CacheRegion adaptiveSearchCacheRegion;

	private CatalogVersionModel catalogVersion;
	private CategoryModel category10;
	private CategoryModel category20;
	private ProductModel product1;
	private ProductModel product2;
	private ProductModel product3;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/adaptivesearch/test/services/defaultAsSearchProfileCalculationServiceTest.impex", "utf-8");

		catalogVersion = catalogVersionService.getCatalogVersion("hwcatalog", "Staged");
		category10 = categoryService.getCategoryForCode(catalogVersion, "cat10");
		category20 = categoryService.getCategoryForCode(catalogVersion, "cat20");
		product1 = productService.getProductForCode(catalogVersion, PRODUCT1_CODE);
		product2 = productService.getProductForCode(catalogVersion, PRODUCT2_CODE);
		product3 = productService.getProductForCode(catalogVersion, PRODUCT3_CODE);
	}

	@Test
	public void calculateSearchProfile() throws Exception
	{
		// given
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Collections.emptyList());
		final Optional<AbstractAsSearchProfileModel> searchProfile = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				SIMPLE_SEARCH_PROFILE_1_CODE);

		// when
		final AsSearchProfileResult result = asSearchProfileCalculationService.calculate(context,
				Collections.singletonList(searchProfile.get()));

		// then
		assertNotNull(result);

		assertNotNull(result.getPromotedFacets());
		assertEquals(1, result.getPromotedFacets().size());
		final AsPromotedFacet promotedFacet = result.getPromotedFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY1, promotedFacet.getIndexProperty());

		assertNotNull(result.getFacets());
		assertEquals(0, result.getFacets().size());

		assertNotNull(result.getExcludedFacets());
		assertEquals(1, result.getExcludedFacets().size());
		final AsExcludedFacet excludedFacet = result.getExcludedFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY2, excludedFacet.getIndexProperty());

		assertNotNull(result.getBoostRules());
		assertEquals(1, result.getBoostRules().size());
		final AsBoostRule boostRule = result.getBoostRules().get(0).getConfiguration();
		assertEquals(PROPERTY1, boostRule.getIndexProperty());
		assertEquals(AsBoostOperator.EQUAL, boostRule.getOperator());
		assertEquals(BOOST_VALUE, boostRule.getValue());
		assertEquals(Float.valueOf(1.1f), boostRule.getBoost()); // should not compare exact value
		assertEquals(AsBoostType.MULTIPLICATIVE, boostRule.getBoostType());

		assertNotNull(result.getPromotedItems());
		assertEquals(1, result.getPromotedItems().size());
		final AsPromotedItem promotedItem = result.getPromotedItems().values().iterator().next().getConfiguration();
		assertEquals(product1.getPk(), promotedItem.getItemPk());

		assertNotNull(result.getExcludedItems());
		assertEquals(1, result.getExcludedItems().size());
		final AsExcludedItem excludedItem = result.getExcludedItems().values().iterator().next().getConfiguration();
		assertEquals(product2.getPk(), excludedItem.getItemPk());
	}

	@Test
	public void calculateCategoryAwareSearchProfile() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Arrays.asList(category20, category10));
		final Optional<AbstractAsSearchProfileModel> searchProfile = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				CATEGORY_AWARE_SEARCH_PROFILE_1_CODE);

		// when
		final AsSearchProfileResult result = asSearchProfileCalculationService.calculate(context,
				Collections.singletonList(searchProfile.get()));

		// then
		assertNotNull(result);

		assertNotNull(result.getPromotedFacets());
		assertEquals(1, result.getPromotedFacets().size());
		final AsPromotedFacet promotedFacet = result.getPromotedFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY2, promotedFacet.getIndexProperty());

		assertNotNull(result.getFacets());
		assertEquals(1, result.getFacets().size());
		final AsFacet facet = result.getFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY1, facet.getIndexProperty());
		assertEquals(Integer.valueOf(12), facet.getPriority());
		assertEquals(AsFacetType.REFINE, facet.getFacetType());

		assertNotNull(result.getExcludedFacets());
		assertEquals(1, result.getExcludedFacets().size());
		final AsExcludedFacet excludedFacet = result.getExcludedFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY3, excludedFacet.getIndexProperty());

		assertNotNull(result.getBoostRules());
		assertEquals(2, result.getBoostRules().size());

		final AsBoostRule boostRule1 = result.getBoostRules().get(0).getConfiguration();
		assertEquals(PROPERTY1, boostRule1.getIndexProperty());
		assertEquals(AsBoostOperator.EQUAL, boostRule1.getOperator());
		assertEquals(BOOST_VALUE, boostRule1.getValue());
		assertEquals(Float.valueOf(1.3f), boostRule1.getBoost()); // should not compare exact value
		assertEquals(AsBoostType.MULTIPLICATIVE, boostRule1.getBoostType());

		final AsBoostRule boostRule2 = result.getBoostRules().get(1).getConfiguration();
		assertEquals(PROPERTY2, boostRule2.getIndexProperty());
		assertEquals(AsBoostOperator.EQUAL, boostRule2.getOperator());
		assertEquals(BOOST_VALUE, boostRule2.getValue());
		assertEquals(Float.valueOf(1.2f), boostRule2.getBoost()); // should not compare exact value
		assertEquals(AsBoostType.MULTIPLICATIVE, boostRule2.getBoostType());

		assertNotNull(result.getPromotedItems());
		assertEquals(1, result.getPromotedItems().size());
		final AsPromotedItem promotedItem = result.getPromotedItems().values().iterator().next().getConfiguration();
		assertEquals(product1.getPk(), promotedItem.getItemPk());

		assertNotNull(result.getExcludedItems());
		assertEquals(1, result.getExcludedItems().size());
		final AsExcludedItem excludedItem = result.getExcludedItems().values().iterator().next().getConfiguration();
		assertEquals(product3.getPk(), excludedItem.getItemPk());
	}

	@Test
	public void calculateMultipleSearchProfiles() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Arrays.asList(category20, category10));

		final Optional<AbstractAsSearchProfileModel> searchProfile = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				SIMPLE_SEARCH_PROFILE_1_CODE);

		final Optional<AbstractAsSearchProfileModel> categoryAwareSearchProfile = asSearchProfileService
				.getSearchProfileForCode(catalogVersion, CATEGORY_AWARE_SEARCH_PROFILE_1_CODE);

		// when
		final AsSearchProfileResult result = asSearchProfileCalculationService.calculate(context,
				Arrays.asList(searchProfile.get(), categoryAwareSearchProfile.get()));

		// then
		assertNotNull(result);

		assertNotNull(result.getPromotedFacets());
		assertEquals(1, result.getPromotedFacets().size());
		final AsPromotedFacet promotedFacet = result.getPromotedFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY2, promotedFacet.getIndexProperty());

		assertNotNull(result.getFacets());
		assertEquals(1, result.getFacets().size());
		final AsFacet facet = result.getFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY1, facet.getIndexProperty());
		assertEquals(Integer.valueOf(12), facet.getPriority());
		assertEquals(AsFacetType.REFINE, facet.getFacetType());

		assertNotNull(result.getExcludedFacets());
		assertEquals(1, result.getExcludedFacets().size());
		final AsExcludedFacet excludedFacet2 = result.getExcludedFacets().values().iterator().next().getConfiguration();
		assertEquals(PROPERTY3, excludedFacet2.getIndexProperty());

		assertNotNull(result.getBoostRules());
		assertEquals(3, result.getBoostRules().size());

		final AsBoostRule boostRule1 = result.getBoostRules().get(0).getConfiguration();
		assertEquals(PROPERTY1, boostRule1.getIndexProperty());
		assertEquals(AsBoostOperator.EQUAL, boostRule1.getOperator());
		assertEquals(BOOST_VALUE, boostRule1.getValue());
		assertEquals(Float.valueOf(1.1f), boostRule1.getBoost()); // should not compare exact value
		assertEquals(AsBoostType.MULTIPLICATIVE, boostRule1.getBoostType());

		final AsBoostRule boostRule2 = result.getBoostRules().get(1).getConfiguration();
		assertEquals(PROPERTY1, boostRule2.getIndexProperty());
		assertEquals(AsBoostOperator.EQUAL, boostRule2.getOperator());
		assertEquals(BOOST_VALUE, boostRule2.getValue());
		assertEquals(Float.valueOf(1.3f), boostRule2.getBoost()); // should not compare exact value
		assertEquals(AsBoostType.MULTIPLICATIVE, boostRule2.getBoostType());

		final AsBoostRule boostRule3 = result.getBoostRules().get(2).getConfiguration();
		assertEquals(PROPERTY2, boostRule3.getIndexProperty());
		assertEquals(AsBoostOperator.EQUAL, boostRule3.getOperator());
		assertEquals(BOOST_VALUE, boostRule3.getValue());
		assertEquals(Float.valueOf(1.2f), boostRule3.getBoost()); // should not compare exact value
		assertEquals(AsBoostType.MULTIPLICATIVE, boostRule3.getBoostType());

		assertNotNull(result.getPromotedItems());
		assertEquals(1, result.getPromotedItems().size());
		final AsPromotedItem promotedItem = result.getPromotedItems().values().iterator().next().getConfiguration();
		assertEquals(product1.getPk(), promotedItem.getItemPk());

		assertNotNull(result.getExcludedItems());
		assertEquals(2, result.getExcludedItems().size());
		final Iterator<AsConfigurationHolder<AsExcludedItem>> excludedItemsIterator = result.getExcludedItems().values().iterator();

		final AsExcludedItem excludedItem1 = excludedItemsIterator.next().getConfiguration();
		assertEquals(product2.getPk(), excludedItem1.getItemPk());

		final AsExcludedItem excludedItem2 = excludedItemsIterator.next().getConfiguration();
		assertEquals(product3.getPk(), excludedItem2.getItemPk());
	}

	@Test
	public void calculateSearchProfileCacheTest() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Collections.emptyList());
		final Optional<AbstractAsSearchProfileModel> searchProfile = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				SIMPLE_SEARCH_PROFILE_1_CODE);

		// when
		adaptiveSearchCacheRegion.clearCache();

		for (int i = 0; i < 10; i++)
		{
			asSearchProfileCalculationService.calculate(context, Collections.singletonList(searchProfile.get()));
		}

		// then
		assertEquals(2, adaptiveSearchCacheRegion.getMaxReachedSize());
		final CacheStatistics cacheRegionStatistics = adaptiveSearchCacheRegion.getCacheRegionStatistics();
		assertEquals(2, cacheRegionStatistics.getMisses());
		// 18 = 10 * 2(caches) - 2(the first hits)
		assertEquals(18, cacheRegionStatistics.getHits());
	}

	@Test
	public void calculateSearchProfilesCacheTest() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Collections.emptyList());
		final Optional<AbstractAsSearchProfileModel> searchProfile1 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				SIMPLE_SEARCH_PROFILE_1_CODE);
		final Optional<AbstractAsSearchProfileModel> searchProfile2 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				SIMPLE_SEARCH_PROFILE_2_CODE);

		// when
		adaptiveSearchCacheRegion.clearCache();

		for (int i = 0; i < 10; i++)
		{
			asSearchProfileCalculationService.calculate(context, Arrays.asList(searchProfile1.get(), searchProfile2.get()));
		}

		// then
		assertEquals(5, adaptiveSearchCacheRegion.getMaxReachedSize());
		final CacheStatistics cacheRegionStatistics = adaptiveSearchCacheRegion.getCacheRegionStatistics();
		assertEquals(5, cacheRegionStatistics.getMisses());
		// 45 = 10 * 5(caches) - 5(the first hits)
		assertEquals(45, cacheRegionStatistics.getHits());
	}

	@Test
	public void calculateSearchProfileGroupCacheTest() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Collections.emptyList());
		final Optional<AbstractAsSearchProfileModel> searchProfile1 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				SIMPLE_SEARCH_PROFILE_1_CODE);
		final Optional<AbstractAsSearchProfileModel> searchProfile2 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				SIMPLE_SEARCH_PROFILE_2_CODE);
		final AsMergeConfiguration mergeConfiguration = new AsMergeConfiguration();
		mergeConfiguration.setFacetsMergeMode(AsFacetsMergeMode.ADD_AFTER);
		mergeConfiguration.setBoostRulesMergeMode(AsBoostRulesMergeMode.ADD);
		mergeConfiguration.setBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_BEFORE);
		mergeConfiguration.setResultFacetsMergeMode(AsFacetsMergeMode.ADD_BEFORE);
		mergeConfiguration.setResultBoostRulesMergeMode(AsBoostRulesMergeMode.REPLACE);
		mergeConfiguration.setResultBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_AFTER);

		final AsSearchProfileActivationGroup group = new AsSearchProfileActivationGroup();
		group.setSearchProfiles(Arrays.asList(searchProfile1.get(), searchProfile2.get()));
		group.setMergeConfiguration(mergeConfiguration);

		// when
		adaptiveSearchCacheRegion.clearCache();

		for (int i = 0; i < 10; i++)
		{
			asSearchProfileCalculationService.calculateGroups(context, Collections.singletonList(group));
		}

		// then
		assertEquals(5, adaptiveSearchCacheRegion.getMaxReachedSize());
		final CacheStatistics cacheRegionStatistics = adaptiveSearchCacheRegion.getCacheRegionStatistics();
		assertEquals(5, cacheRegionStatistics.getMisses());
		// 45 = 10 * 5(caches) - 5(the first hits)
		assertEquals(45, cacheRegionStatistics.getHits());
	}

	@Test
	public void calculateCategoryAwareSearchProfileCacheTest() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Arrays.asList(category20, category10));
		final Optional<AbstractAsSearchProfileModel> searchProfile = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				CATEGORY_AWARE_SEARCH_PROFILE_1_CODE);

		// when
		adaptiveSearchCacheRegion.clearCache();

		for (int i = 0; i < 10; i++)
		{
			asSearchProfileCalculationService.calculate(context, Collections.singletonList(searchProfile.get()));
		}

		// then
		assertEquals(2, adaptiveSearchCacheRegion.getMaxReachedSize());
		final CacheStatistics cacheRegionStatistics = adaptiveSearchCacheRegion.getCacheRegionStatistics();
		assertEquals(2, cacheRegionStatistics.getMisses());
		// 18 = 10 * 2(caches) - 2(the first hits)
		assertEquals(18, cacheRegionStatistics.getHits());
	}

	@Test
	public void calculateCategoryAwareSearchProfileGroupCacheTest() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Arrays.asList(category20, category10));
		final Optional<AbstractAsSearchProfileModel> searchProfile1 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				CATEGORY_AWARE_SEARCH_PROFILE_1_CODE);
		final Optional<AbstractAsSearchProfileModel> searchProfile2 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				CATEGORY_AWARE_SEARCH_PROFILE_2_CODE);

		final AsSearchProfileActivationGroup group1 = new AsSearchProfileActivationGroup();
		group1.setSearchProfiles(Arrays.asList(searchProfile1.get(), searchProfile2.get()));

		final AsSearchProfileActivationGroup group2 = new AsSearchProfileActivationGroup();
		group2.setSearchProfiles(Arrays.asList(searchProfile1.get(), searchProfile2.get()));

		// when
		adaptiveSearchCacheRegion.clearCache();

		for (int i = 0; i < 10; i++)
		{
			asSearchProfileCalculationService.calculateGroups(context, Arrays.asList(group1, group2));
		}

		// then
		assertEquals(5, adaptiveSearchCacheRegion.getMaxReachedSize());
		final CacheStatistics cacheRegionStatistics = adaptiveSearchCacheRegion.getCacheRegionStatistics();
		assertEquals(5, cacheRegionStatistics.getMisses());
		// 95 = 20 * 5(caches) - 5(the first hits)
		assertEquals(95, cacheRegionStatistics.getHits());
	}

	@Test
	public void calculateCategoryAwareSearchProfileGroupCacheWithConfigurationTest() throws Exception
	{
		// given
		final AsSearchProfileContext context = asSearchProfileContextFactory.createContext(INDEX_CONFIGURATION, INDEX_TYPE,
				Collections.singletonList(catalogVersion), Arrays.asList(category20, category10));
		final Optional<AbstractAsSearchProfileModel> searchProfile1 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				CATEGORY_AWARE_SEARCH_PROFILE_1_CODE);
		final Optional<AbstractAsSearchProfileModel> searchProfile2 = asSearchProfileService.getSearchProfileForCode(catalogVersion,
				CATEGORY_AWARE_SEARCH_PROFILE_2_CODE);

		final AsMergeConfiguration mergeConfiguration1 = new AsMergeConfiguration();
		mergeConfiguration1.setFacetsMergeMode(AsFacetsMergeMode.ADD_AFTER);
		mergeConfiguration1.setBoostRulesMergeMode(AsBoostRulesMergeMode.ADD);
		mergeConfiguration1.setBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_BEFORE);
		mergeConfiguration1.setResultFacetsMergeMode(AsFacetsMergeMode.ADD_BEFORE);
		mergeConfiguration1.setResultBoostRulesMergeMode(AsBoostRulesMergeMode.REPLACE);
		mergeConfiguration1.setResultBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_AFTER);

		final AsMergeConfiguration mergeConfiguration2 = new AsMergeConfiguration();
		mergeConfiguration2.setFacetsMergeMode(AsFacetsMergeMode.ADD_BEFORE);
		mergeConfiguration2.setBoostRulesMergeMode(AsBoostRulesMergeMode.REPLACE);
		mergeConfiguration2.setBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_AFTER);
		mergeConfiguration2.setResultFacetsMergeMode(AsFacetsMergeMode.ADD_AFTER);
		mergeConfiguration2.setResultBoostRulesMergeMode(AsBoostRulesMergeMode.ADD);
		mergeConfiguration2.setResultBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_BEFORE);

		final AsSearchProfileActivationGroup group1 = new AsSearchProfileActivationGroup();
		group1.setSearchProfiles(Arrays.asList(searchProfile1.get(), searchProfile2.get()));
		group1.setMergeConfiguration(mergeConfiguration1);

		final AsSearchProfileActivationGroup group2 = new AsSearchProfileActivationGroup();
		group2.setSearchProfiles(Arrays.asList(searchProfile1.get(), searchProfile2.get()));
		group2.setMergeConfiguration(mergeConfiguration2);

		// when
		adaptiveSearchCacheRegion.clearCache();

		for (int i = 0; i < 10; i++)
		{
			asSearchProfileCalculationService.calculateGroups(context, Arrays.asList(group1, group2));
		}

		// then
		assertEquals(6, adaptiveSearchCacheRegion.getMaxReachedSize());
		final CacheStatistics cacheRegionStatistics = adaptiveSearchCacheRegion.getCacheRegionStatistics();
		assertEquals(6, cacheRegionStatistics.getMisses());
		// 94 = (((10 iterations for each group for each profile)20 * 4(caches))80 + (10 iterations for each group)20) - 6(the first hits)
		assertEquals(94, cacheRegionStatistics.getHits());
	}
}
