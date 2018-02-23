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

import de.hybris.platform.assistedserviceyprofilefacades.data.ProfileLoyaltyHistoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.util.Map;


public class ProfileLoyaltyHistoryPopulator<SOURCE extends NeighbourData, TARGET extends ProfileLoyaltyHistoryData>
		implements Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE neighbourData, final TARGET loyalty) throws ConversionException
	{
		final Map<String, String> neighbourProperties = neighbourData.getProperties();

		loyalty.setCurrentLoyaltyBalance(neighbourProperties.get(YaasyprofileconnectConstants.Loyalty.CURRENT_LOYALTY_BALANCE));
	}
}
