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

import java.util.Optional;
import java.util.function.Function;


/**
 * Represents a function that will delegate its returned {@link Optional}-wrapped value to another {@link ChainFunction} if its
 * own execution returns empty.
 * <p>
 * This is functional interface whose functional method is {@link #apply(Object)}.
 * </p>
 * @param <SOURCE_TYPE> the in-bound type of the function
 * @param <TARGET_TYPE> the out-bound type of the function
 * @see java.util.function.Function
 */
public interface ChainFunction<SOURCE_TYPE, TARGET_TYPE> extends Function<SOURCE_TYPE, Optional<TARGET_TYPE>>
{
	/**
	 * Enablement of the chaining of a {@link ChainFunction} by specifying the delegation to another candidate at the orElse phase.
	 * @param orElse a {@link ChainFunction}
	 * @return the function's result if it succeeds, otherwise the result of the orElse function
	 */
	default ChainFunction<SOURCE_TYPE, TARGET_TYPE> orElse(final ChainFunction<SOURCE_TYPE, TARGET_TYPE> orElse)
	{
		return (final SOURCE_TYPE value) ->
		{
			final Optional<TARGET_TYPE> apply = apply(value);
			if (apply.isPresent())
			{
				return apply;
			}
			return orElse.apply(value);
		};
	}

}
