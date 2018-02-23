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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedserviceyprofilefacades.data.KeywordSearchAffinityData;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@UnitTest
public class KeywordSearchAffinityPopulatorTest extends AbstractProfileAffinityTest
{
	protected KeywordSearchAffinityPopulator keywordSearchAffinityPopulator = new KeywordSearchAffinityPopulator();
	private List<NeighbourData> parsedNeighbours;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		parsedNeighbours = getDataFromJson(AbstractProfileAffinityTest.Y_PROFIL_JSON_PATH);
	}

	@Test
	public void getAffinityTest()
	{
		final List<NeighbourData> affinityList = parsedNeighbours.stream()
				.filter(n -> n.getSchema().contains(YaasyprofileconnectConstants.SCHEMA_COMMERCE_KEYWORD_SEARCH_AFFINITY))
				.collect(Collectors.toList());

		assertEquals(3, affinityList.size());

		final NeighbourData neighbourData = affinityList.get(0);
		final KeywordSearchAffinityData keywordSearchAffinityData = new KeywordSearchAffinityData();
		keywordSearchAffinityPopulator.populate(neighbourData, keywordSearchAffinityData);

		assertTrue(keywordSearchAffinityData.getSearchCount()
				.equalsIgnoreCase(neighbourData.getProperties().get(YaasyprofileconnectConstants.KEYWORD_SEARCH_COUNT_FIELD)));
		assertTrue(keywordSearchAffinityData.getSearchText()
				.equalsIgnoreCase(neighbourData.getProperties().get(YaasyprofileconnectConstants.KEYWORD_SEARCHID_FIELD)));
	}
}
