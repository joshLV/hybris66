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

/**
 * Provider of {@link AttributeContentConverter} by means of a {@link AttributeDescriptorModel} based strategy.
 */
@FunctionalInterface
public interface AttributeStrategyConverterProvider
{

	/**
	 * Will return the most appropriate {@link AttributeContentConverter} to convert back and forth between a property value and a serializable representation
	 * @param source the {@link AttributeDescriptorModel} holding the metadata of a given class property
	 * @return the content converter destined to a given attribute. 
	 */
	AttributeContentConverter getContentConverter(AttributeDescriptorModel source);

}
