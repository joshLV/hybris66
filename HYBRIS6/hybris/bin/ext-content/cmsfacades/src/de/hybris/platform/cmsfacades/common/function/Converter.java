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
package de.hybris.platform.cmsfacades.common.function;

/**
 * Converter is a Functional interface that accepts one argument and produces another.
 * The converter is expected to create a new instance of the output class.  
 * 
 * @param <S> the type of the input of the converter.
 * @param <T> the type of the output of the converter.
 */
@FunctionalInterface
public interface Converter<S, T>
{
	/**
	 * Uses the source {@code S} object and produces an new instance of {@code T}.   
	 * @param source the input to be converted. 
	 * @return an instance of {@code T}, converted from the input source. 
	 */
	T convert(final S source);
	
}
