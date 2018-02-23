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
package de.hybris.platform.adaptivesearchsolr.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.impl.DefaultAsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsCatalogVersionData;
import de.hybris.platform.adaptivesearch.data.AsCurrencyData;
import de.hybris.platform.adaptivesearch.data.AsFacetData;
import de.hybris.platform.adaptivesearch.data.AsFacetValueData;
import de.hybris.platform.adaptivesearch.data.AsIndexConfigurationData;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.data.AsIndexTypeData;
import de.hybris.platform.adaptivesearch.data.AsLanguageData;
import de.hybris.platform.adaptivesearch.data.AsPaginationData;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileResult;
import de.hybris.platform.adaptivesearch.data.AsSearchResultData;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexedPropertyDao;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexedTypeDao;
import de.hybris.platform.solrfacetsearch.enums.SolrPropertiesTypes;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultDocument;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;
import de.hybris.platform.solrfacetsearch.solr.IndexedPropertyTypeInfo;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexedPropertyTypeRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class SolrAsSearchProviderTest
{
	private static final String ITEM_TYPE_CODE = "itemTypeCode";

	private static final String CATALOG_VERSION = "catalogVersion";

	private static final String CATALOG_ID = "catalogID";
	private static final String CATALOG_NAME = "catalogName";

	private static final String LANGUAGE_CODE = "EN";
	private static final String CURRENCY_CODE = "USD";

	private static final String FACET_SEARCH_CONFIG_NAME = "facetSearchConfig";
	private static final String FACET_SEARCH_CONFIG_DESCRIPTION = "description";

	private static final String INDEX_CONFIGURATION = "indexConfiguration";
	private static final String INDEX_TYPE = "indexType";
	private static final String INDEX_PROPERTY = "indexedProperty";

	private static final String FACET_NAME = "facet";
	private static final String FACET_VALUE_NAME = "facetValue";
	private static final String ADAPTIVE_SEARCH_RESULT = "adaptiveSearchResult";
	private static final String DEFAULT_QUERY_TEMPLATE = "DEFAULT";
	private static final String SEARCH_TEXT = "searchText";
	private static final int ACTIVE_PAGE = 1;
	private static final int PAGE_SIZE = 20;
	private static final int OFFSET = 1;

	private SolrAsSearchProvider solrAsSearchProvider;

	@Mock
	private SolrFacetSearchConfigDao solrFacetSearchConfigDao;

	@Mock
	private SolrIndexedTypeDao solrIndexedTypeDao;

	@Mock
	private SolrIndexedPropertyDao solrIndexedPropertyDao;

	@Mock
	private SolrIndexedPropertyTypeRegistry solrIndexedPropertyTypeRegistry;

	@Mock
	private FacetSearchConfigService facetSearchConfigService;

	@Mock
	private FacetSearchService facetSearchService;

	@Mock
	private SolrIndexedTypeModel indexedType;

	@Mock
	private ComposedTypeModel itemType;

	@Mock
	private SolrFacetSearchConfigModel solrIndexConfiguration;

	@Mock
	private SolrIndexedPropertyModel solrIndexedProperty;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private CatalogModel catalog;

	@Mock
	private LanguageModel language;

	@Mock
	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		solrAsSearchProvider = new SolrAsSearchProvider();
		solrAsSearchProvider.setSolrFacetSearchConfigDao(solrFacetSearchConfigDao);
		solrAsSearchProvider.setSolrIndexedTypeDao(solrIndexedTypeDao);
		solrAsSearchProvider.setSolrIndexedPropertyDao(solrIndexedPropertyDao);
		solrAsSearchProvider.setSolrIndexedPropertyTypeRegistry(solrIndexedPropertyTypeRegistry);
		solrAsSearchProvider.setFacetSearchConfigService(facetSearchConfigService);
		solrAsSearchProvider.setFacetSearchService(facetSearchService);

		when(indexedType.getIdentifier()).thenReturn(INDEX_PROPERTY);
		when(indexedType.getType()).thenReturn(itemType);

		when(itemType.getCode()).thenReturn(ITEM_TYPE_CODE);
		when(itemType.getCatalogItemType()).thenReturn(Boolean.TRUE);

		when(solrIndexConfiguration.getSolrIndexedTypes()).thenReturn(Collections.singletonList(indexedType));
		when(solrIndexConfiguration.getCatalogVersions()).thenReturn(Collections.singletonList(catalogVersion));

		when(solrIndexedProperty.getName()).thenReturn(INDEX_PROPERTY);
		when(solrIndexedProperty.getType()).thenReturn(SolrPropertiesTypes.STRING);

		when(catalogVersion.getCatalog()).thenReturn(catalog);
		when(catalogVersion.getVersion()).thenReturn(CATALOG_VERSION);
		when(catalogVersion.getActive()).thenReturn(Boolean.TRUE);

		when(catalog.getId()).thenReturn(CATALOG_ID);
		when(catalog.getName()).thenReturn(CATALOG_NAME);

		when(solrIndexConfiguration.getLanguages()).thenReturn(Collections.singletonList(language));
		when(solrIndexConfiguration.getCurrencies()).thenReturn(Collections.singletonList(currency));

		when(this.language.getIsocode()).thenReturn(LANGUAGE_CODE);
		when(this.currency.getIsocode()).thenReturn(CURRENCY_CODE);
	}

	@Test
	public void testGetIndexConfigurations() throws Exception
	{
		// given
		final SolrFacetSearchConfigModel solrFacetSearchConfig = new SolrFacetSearchConfigModel();
		solrFacetSearchConfig.setName(FACET_SEARCH_CONFIG_NAME);
		solrFacetSearchConfig.setDescription(FACET_SEARCH_CONFIG_DESCRIPTION);

		when(solrFacetSearchConfigDao.findAllFacetSearchConfigs()).thenReturn(Collections.singletonList(solrFacetSearchConfig));

		// when
		final List<AsIndexConfigurationData> indexConfigurations = solrAsSearchProvider.getIndexConfigurations();

		// then
		assertTrue(CollectionUtils.isNotEmpty(indexConfigurations));
		assertEquals(1, indexConfigurations.size());

		final AsIndexConfigurationData indexConfiguration = indexConfigurations.get(0);
		assertEquals(solrFacetSearchConfig.getDescription(), indexConfiguration.getName());
	}

	@Test
	public void testGetIndexTypes() throws Exception
	{
		// given
		when(solrIndexedTypeDao.findAllIndexedTypes()).thenReturn(Collections.singletonList(indexedType));

		// when
		final List<AsIndexTypeData> indexTypes = solrAsSearchProvider.getIndexTypes();

		// then
		assertTrue(CollectionUtils.isNotEmpty(indexTypes));
		assertEquals(1, indexTypes.size());

		final AsIndexTypeData indexType = indexTypes.get(0);
		assertEquals(indexedType.getIdentifier(), indexType.getName());
		assertEquals(indexedType.getIdentifier(), indexType.getCode());
		assertEquals(itemType.getCode(), indexType.getItemType());
	}

	@Test
	public void testGetIndexTypes1() throws Exception
	{
		// given
		when(solrFacetSearchConfigDao.findFacetSearchConfigByName(INDEX_CONFIGURATION)).thenReturn(solrIndexConfiguration);

		// when
		final List<AsIndexTypeData> indexTypes = solrAsSearchProvider.getIndexTypes(INDEX_CONFIGURATION);

		// then
		assertTrue(CollectionUtils.isNotEmpty(indexTypes));
		assertEquals(1, indexTypes.size());

		final AsIndexTypeData indexType = indexTypes.get(0);
		assertEquals(indexedType.getIdentifier(), indexType.getName());
		assertEquals(indexedType.getIdentifier(), indexType.getCode());
		assertEquals(itemType.getCode(), indexType.getItemType());
	}

	@Test
	public void testGetIndexTypeForCode() throws Exception
	{
		// given
		when(solrIndexedTypeDao.findIndexedTypeByIdentifier(INDEX_TYPE)).thenReturn(indexedType);

		// when
		final Optional<AsIndexTypeData> indexTypeData = solrAsSearchProvider.getIndexTypeForCode(INDEX_TYPE);

		// then
		assertTrue(indexTypeData.isPresent());
		assertEquals(indexedType.getIdentifier(), indexTypeData.get().getName());
		assertEquals(indexedType.getIdentifier(), indexTypeData.get().getCode());
		assertEquals(itemType.getCode(), indexTypeData.get().getItemType());
	}

	@Test
	public void testGetIndexProperties() throws Exception
	{
		// given
		when(solrIndexedTypeDao.findIndexedTypeByIdentifier(INDEX_TYPE)).thenReturn(indexedType);
		when(solrIndexedPropertyDao.findIndexedPropertiesByIndexedType(indexedType))
				.thenReturn(Collections.singletonList(solrIndexedProperty));
		when(solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(solrIndexedProperty.getType().getCode()))
				.thenReturn(mock(IndexedPropertyTypeInfo.class));

		// when
		final List<AsIndexPropertyData> indexProperties = solrAsSearchProvider.getIndexProperties(INDEX_TYPE);

		// then
		assertTrue(CollectionUtils.isNotEmpty(indexProperties));
		assertEquals(1, indexProperties.size());

		final AsIndexPropertyData indexPropertyData = indexProperties.get(0);
		assertEquals(INDEX_PROPERTY, indexPropertyData.getCode());
	}

	@Test
	public void testGetIndexPropertyForCode() throws Exception
	{
		// given
		when(solrIndexedTypeDao.findIndexedTypeByIdentifier(INDEX_TYPE)).thenReturn(indexedType);
		when(solrIndexedPropertyDao.findIndexedPropertyByName(indexedType, INDEX_PROPERTY)).thenReturn(solrIndexedProperty);
		when(solrIndexedPropertyTypeRegistry.getIndexPropertyTypeInfo(solrIndexedProperty.getType().getCode()))
				.thenReturn(mock(IndexedPropertyTypeInfo.class));
		// when
		final Optional<AsIndexPropertyData> indexPropertyData = solrAsSearchProvider.getIndexPropertyForCode(INDEX_TYPE,
				INDEX_PROPERTY);

		// then
		assertTrue(indexPropertyData.isPresent());
		assertEquals(INDEX_PROPERTY, indexPropertyData.get().getCode());
	}

	@Test
	public void testGetCatalogVersions() throws Exception
	{
		// given
		when(solrFacetSearchConfigDao.findFacetSearchConfigByName(INDEX_CONFIGURATION)).thenReturn(solrIndexConfiguration);

		// when
		final List<AsCatalogVersionData> catalogVersions = solrAsSearchProvider.getCatalogVersions(INDEX_CONFIGURATION, INDEX_TYPE);

		// then
		assertTrue(CollectionUtils.isNotEmpty(catalogVersions));
		assertEquals(1, catalogVersions.size());

		final AsCatalogVersionData catalogVersionData = catalogVersions.get(0);
		assertEquals(catalog.getName() + " : " + catalogVersion.getVersion(), catalogVersionData.getName());
	}

	@Test
	public void testGetLanguages() throws Exception
	{
		// given
		when(solrFacetSearchConfigDao.findFacetSearchConfigByName(INDEX_CONFIGURATION)).thenReturn(solrIndexConfiguration);

		// when
		final List<AsLanguageData> languages = solrAsSearchProvider.getLanguages(INDEX_CONFIGURATION, INDEX_TYPE);

		// then
		assertTrue(CollectionUtils.isNotEmpty(languages));
		assertEquals(1, languages.size());

		final AsLanguageData languageData = languages.get(0);
		assertEquals(LANGUAGE_CODE, languageData.getIsocode());
	}

	@Test
	public void testGetCurrencies() throws Exception
	{
		// given
		when(solrFacetSearchConfigDao.findFacetSearchConfigByName(INDEX_CONFIGURATION)).thenReturn(solrIndexConfiguration);

		// when
		final List<AsCurrencyData> currencies = solrAsSearchProvider.getCurrencies(INDEX_CONFIGURATION, INDEX_TYPE);

		// then
		assertTrue(CollectionUtils.isNotEmpty(currencies));
		assertEquals(1, currencies.size());

		final AsCurrencyData currencyData = currencies.get(0);
		assertEquals(CURRENCY_CODE, currencyData.getIsocode());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSearch() throws Exception
	{
		// given
		final AsSearchProfileContext searchProfileContext = buildSearchProfileContext();

		final IndexedType indexedType = new IndexedType();
		indexedType.setIdentifier(INDEX_TYPE);

		final FacetSearchConfig facetSearchConfig = buildFacetSearchConfig(indexedType);

		final SearchQuery searchQuery = new SearchQuery(facetSearchConfig, indexedType);
		searchQuery.setOffset(OFFSET);
		searchQuery.setPageSize(PAGE_SIZE);

		when(facetSearchConfigService.getConfiguration(any(String.class))).thenReturn(facetSearchConfig);

		when(facetSearchService.createFreeTextSearchQueryFromTemplate(facetSearchConfig, indexedType, DEFAULT_QUERY_TEMPLATE,
				SEARCH_TEXT)).thenReturn(searchQuery);

		when(facetSearchService.search(searchQuery)).thenReturn(buildSearchResult(searchQuery));

		// when
		final AsSearchResultData searchResult = solrAsSearchProvider.search(searchProfileContext, SEARCH_TEXT, ACTIVE_PAGE,
				PAGE_SIZE);

		// then
		assertNotNull(searchResult);

		final List<AsFacetData> facets = searchResult.getFacets();
		assertTrue(CollectionUtils.isNotEmpty(facets));
		assertEquals(1, facets.size());

		final AsFacetData facetData = facets.get(0);
		assertEquals(FACET_NAME, facetData.getName());
		assertEquals(FACET_VALUE_NAME, ((List<AsFacetValueData>) facetData.getValues()).get(0).getName());

		assertTrue(CollectionUtils.isNotEmpty(searchResult.getResults()));

		final AsPaginationData pagination = searchResult.getPagination();
		assertNotNull(pagination);
		assertEquals(PAGE_SIZE, pagination.getPageSize());
		assertEquals(1, pagination.getPageCount());
		assertEquals(1, pagination.getActivePage());
		assertEquals(20, pagination.getResultCount());

		assertNotNull(searchResult.getSearchProfileResult());
	}

	protected AsSearchProfileContext buildSearchProfileContext()
	{
		final DefaultAsSearchProfileContext searchProfileContext = new DefaultAsSearchProfileContext();
		searchProfileContext.setIndexType(INDEX_TYPE);
		searchProfileContext.setIndexConfiguration("indexConfiguration");
		searchProfileContext.setCatalogVersions(Collections.singletonList(new CatalogVersionModel()));
		searchProfileContext.setCategoryPath(Collections.emptyList());
		searchProfileContext.setLanguage(language);
		searchProfileContext.setCurrency(currency);

		return searchProfileContext;
	}

	protected FacetSearchConfig buildFacetSearchConfig(final IndexedType indexedType)
	{
		final IndexConfig indexConfig = new IndexConfig();
		indexConfig.setIndexedTypes(Collections.singletonMap("", indexedType));

		final FacetSearchConfig facetSearchConfig = new FacetSearchConfig();
		facetSearchConfig.setIndexConfig(indexConfig);

		return facetSearchConfig;
	}

	protected SearchResult buildSearchResult(final SearchQuery searchQuery)
	{
		final SolrSearchResult searchResult = new SolrSearchResult();
		final FacetValue facetValue = new FacetValue(FACET_VALUE_NAME, 10l, true);
		final Facet facet = new Facet(FACET_NAME, Collections.singletonList(facetValue));

		searchResult.setFacetsMap(Collections.singletonMap(any(String.class), facet));

		searchResult.setDocuments(Collections.singletonList(new DefaultDocument()));
		searchResult.setSearchQuery(searchQuery);
		searchResult.setNumberOfResults(PAGE_SIZE);

		final Map<String, Object> attributes = searchResult.getAttributes();
		attributes.put(ADAPTIVE_SEARCH_RESULT, new AsSearchProfileResult());

		return searchResult;
	}
}
