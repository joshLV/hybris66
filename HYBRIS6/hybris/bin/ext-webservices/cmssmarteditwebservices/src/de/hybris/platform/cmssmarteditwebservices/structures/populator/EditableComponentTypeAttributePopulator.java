/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cmssmarteditwebservices.structures.populator;

import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populator that populates the editable field of the {@link ComponentTypeAttributeData} POJO.
 * @Deprecated since 6.4, taken care by {@link de.hybris.platform.cmsfacades.types.populator.BasicComponentTypeAttributePopulator}
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public class EditableComponentTypeAttributePopulator implements Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{
	private boolean isEditable;

	@Override
	public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target) throws ConversionException
	{
		target.setEditable(isEditable());
	}

	public void setEditable(final boolean isEditable)
	{
		this.isEditable = isEditable;
	}

	public boolean isEditable()
	{
		return isEditable;
	}
}
