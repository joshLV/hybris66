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
import de.hybris.platform.cmsfacades.types.populator.DropdownComponentTypeAttributePopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator that populates the collection field of the {@link ComponentTypeAttributeData} POJO.
 * @Deprecated since 6.4, taken care of by {@link DropdownComponentTypeAttributePopulator}
 */
@Deprecated
@HybrisDeprecation(sinceVersion = "6.4")
public class CollectionComponentTypeAttributePopulator implements Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{
	private boolean collection;

	@Override
	public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target)
			throws ConversionException
	{
		target.setCollection(isCollection());
	}

	protected boolean isCollection()
	{
		return collection;
	}

	@Required
	public void setCollection(final boolean collection)
	{
		this.collection = collection;
	}
}
