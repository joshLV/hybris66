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
package de.hybris.platform.cmsfacades.cmsitems.impl;

import de.hybris.platform.cmsfacades.common.function.Converter;
import de.hybris.platform.cmsfacades.cmsitems.AttributeContentConverter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;


/**
 * Default implementation of {@link AttributeContentConverter} is a Java Bean to provide the attributes for the interface.
 */
public class DefaultAttributeContentConverter implements AttributeContentConverter
{
	private Predicate<AttributeDescriptorModel> constrainedBy;

	private Converter<Object, Object> modelToDataConverter;
	private Converter<Object, Object> dataToModelConverter;

	@Override
	public Predicate<AttributeDescriptorModel> getConstrainedBy()
	{
		return constrainedBy;
	}

	public void setConstrainedBy(final Predicate<AttributeDescriptorModel> constrainedBy)
	{
		this.constrainedBy = constrainedBy;
	}

	@Override
	public Object convertModelToData(AttributeDescriptorModel attribute, Object source)
	{
		return getModelToDataConverter().convert(source);
	}


	@Override
	public Object convertDataToModel(AttributeDescriptorModel attribute, Object source)
	{
		return getDataToModelConverter().convert(source);
	}

	protected Converter<Object, Object> getModelToDataConverter()
	{
		return modelToDataConverter;
	}

	public void setModelToDataConverter(final Converter<Object, Object> modelToDataConverter)
	{
		this.modelToDataConverter = modelToDataConverter;
	}

	protected Converter<Object, Object> getDataToModelConverter()
	{
		return dataToModelConverter;
	}

	public void setDataToModelConverter(final Converter<Object, Object> dataToModelConverter)
	{
		this.dataToModelConverter = dataToModelConverter;
	}

}
