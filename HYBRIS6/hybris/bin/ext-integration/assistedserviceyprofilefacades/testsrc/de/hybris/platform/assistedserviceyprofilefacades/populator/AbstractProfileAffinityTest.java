/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.assistedserviceyprofilefacades.populator;

import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public abstract class AbstractProfileAffinityTest
{
	protected static String Y_PROFIL_JSON_PATH = "assistedserviceyprofilefacades/test/yprofile.json";
	protected static String PRODUCT_JSON_PATH = "assistedserviceyprofilefacades/yprofileJSON/productAffinity.json";

	protected List<NeighbourData> getDataFromJson(final String path)
	{
		List<NeighbourData> parsedNeighbours = null;

		final ObjectMapper jacksonObjectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		try
		{
			final InputStream in = getClass().getClassLoader().getResourceAsStream(path);

			parsedNeighbours = jacksonObjectMapper.readValue(in, new TypeReference<List<NeighbourData>>()
			{
				// empty block just for testing purpose
			});
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}

		return parsedNeighbours;
	}
}
