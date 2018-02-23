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
package de.hybris.platform.cmsfacades.cmsitems;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;


/**
 * Interface that represents the Attribute {@code Converter<Object, Object>} with its respective
 * {@code Predicate<AttributeDescriptorModel>}.
 */
public interface AttributeContentConverter
{

	/**
	 * Returns the predicate that constrains the converter.
	 * This predicate will be tested against a given attribute descriptor and
	 * if this predicate is {@code true}, then it applies the conversion,
	 * @return the predicate related with the converter. Never {@code null}.
	 */
	Predicate<AttributeDescriptorModel> getConstrainedBy();

	/**
	 * Converts a property value to a serializable representation in case that the predicate
	 * defined is true for a given attribute descriptor.
	 * @param attribute the {@link AttributeDescriptorModel} describing the source
	 * @param source the persistent source described by the attribute that needs be converted to some serializable representation
	 * @return the converter instance. Never {@code null}.
	 */
	Object convertModelToData(AttributeDescriptorModel attribute, Object source);

	/**
	 * Converts a serializable representation to a property value in case that the predicate
	 * defined is true for a given attribute descriptor.
	 * @param attribute the {@link AttributeDescriptorModel} describing the source
	 * @param source the serializable representation described by the attribute that needs be converted to some persistent property
	 * @return the converter instance. Never {@code null}.
	 */
	Object convertDataToModel(AttributeDescriptorModel attribute, Object source);

}
