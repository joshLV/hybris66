/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedserviceyprofilefacades.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.assistedserviceyprofilefacades.data.ProductAffinityData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class ProductAffinityPopulatorTest extends AbstractProfileAffinityTest
{

	private final ProductAffinityPopulator productAffinityPopulator = new ProductAffinityPopulator();

	@Mock
	private ProductFacade productFacade;
	private List<NeighbourData> parsedNeighbours;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		productAffinityPopulator.setProductFacade(productFacade);
		parsedNeighbours = getDataFromJson(AbstractProfileAffinityTest.PRODUCT_JSON_PATH);
	}

	@Test
	public void getSingleProductAffinityTest()
	{
		final List<NeighbourData> productAffinityList = parsedNeighbours.stream()
				.filter(n -> n.getSchema().contains(YaasyprofileconnectConstants.SCHEMA_COMMERCE_PRODUCT_AFFINITY))
				.collect(Collectors.toList());

		assertEquals(1, productAffinityList.size());
	}

	@Test
	public void verifyProductAffinityDataTest()
	{
		final List<NeighbourData> productAffinityList = parsedNeighbours.stream()
				.filter(n -> n.getSchema().contains(YaasyprofileconnectConstants.SCHEMA_COMMERCE_PRODUCT_AFFINITY))
				.collect(Collectors.toList());

		final ProductAffinityData productAffinityData = new ProductAffinityData();

		productAffinityPopulator.populate(productAffinityList.get(0), productAffinityData);

		assertEquals("7", productAffinityData.getViewCount());
	}
}
