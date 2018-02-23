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

import de.hybris.platform.assistedserviceyprofilefacades.data.DeviceFingerprintAffinityData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.data.NeighbourData;

import java.util.Map;

/**
 * Device affinity that is used for populating device fingerprint affinity data
 */
public class DeviceFingerprintAffinityPopulator<SOURCE extends NeighbourData, TARGET extends DeviceFingerprintAffinityData>
        implements Populator<SOURCE, TARGET>
{
    @Override
    public void populate(final SOURCE neighbourData, final TARGET deviceData) throws ConversionException
    {
        final Map<String, String> neighbourProperties = neighbourData.getProperties();

        deviceData.setDevice(neighbourProperties.get(YaasyprofileconnectConstants.DEVICE_TYPE_FIELD));
        deviceData.setViewCount(neighbourProperties.get(YaasyprofileconnectConstants.DEVICE_USED_COUNT_FIELD));
        deviceData.setOperatingSystem(neighbourProperties.get(YaasyprofileconnectConstants.DEVICE_OS_FIELD));
        deviceData.setBrowser(neighbourProperties.get(YaasyprofileconnectConstants.DEVICE_BROWSER_FIELD));
    }
}