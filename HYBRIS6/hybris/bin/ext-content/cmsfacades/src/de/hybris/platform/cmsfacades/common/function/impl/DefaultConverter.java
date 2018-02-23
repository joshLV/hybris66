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
package de.hybris.platform.cmsfacades.common.function.impl;

import de.hybris.platform.cmsfacades.common.function.Converter;
import de.hybris.platform.converters.Populator;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link Converter}. 
 * Requires an {@link ObjectFactory} to create instances of the prototyped target object and a list of {@link Populator} to populate the target object.   
 *
 * @param <S> the type of the input of the converter.
 * @param <T> the type of the output of the converter.
 */
public class DefaultConverter<S, T> implements Converter<S, T>
{

	private List<Populator<S, T>> populators;
	
	private ObjectFactory<T> objectFactory;

	@Override
	public T convert(final S s)
	{
		if (s == null)
		{
			return null;
		}
		if (getPopulators() == null)
		{
			return null;
		}
		final T t = getObjectFactory().getObject();

		getPopulators() //
				.stream() //
				.filter(Objects::nonNull) //
				.forEach(populator -> populator.populate(s, t));
		return t;
	}

	protected List<Populator<S, T>> getPopulators()
	{
		return populators;
	}

	@Required
	public void setPopulators(final List<Populator<S, T>> populators)
	{

		this.populators = populators;
	}

	public ObjectFactory<T> getObjectFactory()
	{
		return objectFactory;
	}

	@Required
	public void setObjectFactory(final ObjectFactory<T> objectFactory)
	{
		this.objectFactory = objectFactory;
	}
}
