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
package de.hybris.platform.cmsfacades.cmsitems.converter;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Spliterator.ORDERED;

import de.hybris.platform.cmsfacades.cmsitems.AttributeContentConverter;
import de.hybris.platform.cmsfacades.cmsitems.AttributeStrategyConverterProvider;
import de.hybris.platform.cmsfacades.common.function.Converter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Supplier;


/**
 * Default implementation of the {@link Converter} interface for Attribute Strategy Conversions.
 * It depends on a default value converter and a {@link AttributeContentConverter} list that maps
 * {@code Predicate<AttributeDescriptorModel>} to {@code Converter<Object, Object>}.
 * The logic requires that the content converters should be an instance of {@link java.util.LinkedList} because the Predicates
 * will be tested in the descending order, and the first one that is accepted will be returned.
 * Returns null if no matching {@link AttributeContentConverter}
 */
public class DefaultAttributeStrategyConverterProvider implements AttributeStrategyConverterProvider, InitializingBean
{
	private List<AttributeContentConverter> attributeContentConverters;

	private Deque<AttributeContentConverter> internalAttributeContentConverters;

	@Override
	public AttributeContentConverter getContentConverter(final AttributeDescriptorModel attributeDescriptor)
	{

		if (getInternalAttributeContentConverters().size() != getAttributeContentConverters().size())
		{
			this.internalAttributeContentConverters = new LinkedList<>(getAttributeContentConverters());
		}

		checkArgument(nonNull(attributeDescriptor), "AttributeDescriptor/Object Value should not be null");

		// navigate the map in the reverse order, to give precedence to the newest elements in the map
		return getDescendingConverterStreamSupplier()
				.get()
				.filter(contentConverter -> contentConverter.getConstrainedBy().test(attributeDescriptor))
				.findFirst()
				.orElse(null);

	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		this.internalAttributeContentConverters = new LinkedList<>(getAttributeContentConverters());
	}

	/**
	 * A Supplier for a descending stream of the entries from the content converter list.
	 * @return a supplier that provides a stream of converters in descending order.
	 */
	protected Supplier<Stream<AttributeContentConverter>>
			getDescendingConverterStreamSupplier()
	{
		return () -> //
		StreamSupport //
				.stream(Spliterators //
						.spliteratorUnknownSize(getInternalAttributeContentConverters() //
								.descendingIterator(), ORDERED), false);
	}

	protected List<AttributeContentConverter> getAttributeContentConverters()
	{
		return attributeContentConverters;
	}

	@Required
	public void setAttributeContentConverters(final List<AttributeContentConverter> attributeContentConverters)
	{
		this.attributeContentConverters = attributeContentConverters;
	}

	protected Deque<AttributeContentConverter> getInternalAttributeContentConverters()
	{
		return internalAttributeContentConverters;
	}
}
