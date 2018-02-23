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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedserviceyprofilefacades.data.AffinityData;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


@UnitTest
public class AffinityPopulatorTest extends AbstractProfileAffinityTest
{
	@InjectMocks
	private final AffinityPopulator affinityPopulator = new AffinityPopulator();
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
				.filter(n -> StringUtils.containsAny(n.getSchema(), YaasyprofileconnectConstants.SCHEMA_COMMERCE_CATEGORY_AFFINITY,
						YaasyprofileconnectConstants.SCHEMA_COMMERCE_KEYWORD_SEARCH_AFFINITY,
						YaasyprofileconnectConstants.SCHEMA_COMMERCE_PRODUCT_AFFINITY))
				.collect(Collectors.toList());

		final NeighbourData neighbourData = affinityList.get(0);

		final AffinityData affinityData = new AffinityData();

		affinityPopulator.populate(neighbourData, affinityData);

		assertEquals(affinityData.getAffinity(), neighbourData.getProperties().get(YaasyprofileconnectConstants.AFFINITY_FIELD));
		assertEquals(affinityData.getUpdated(), Date.from(
				ZonedDateTime.parse(neighbourData.getProperties().get(YaasyprofileconnectConstants.UPDATED_FIELD)).toInstant()));
	}
}
