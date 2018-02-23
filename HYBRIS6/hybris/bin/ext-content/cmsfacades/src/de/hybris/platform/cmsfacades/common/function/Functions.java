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

import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;


/**
 * Helper class handling specialized types of {@link ChainFunction}
 */
public class Functions
{

	/**
	 * Returns a {@link ChainFunction} that will delegate when the provided {@link Predicate} is not satisfied
	 * @param supplier the supplier to be called when the predicate is satisfied
	 * @param conditionalTo the predicate that will cause the return of the supplier execution or will delegate to the next
	 *           {@link ChainFunction}
	 * @return a {@link ChainFunction}
	 */
	public static <SOURCE, TARGET> ChainFunction<SOURCE, TARGET> ofSupplierConstrainedBy(
			final Supplier<TARGET> supplier,
			final Predicate<SOURCE> conditionalTo
			)
	{
		return source ->
		{
			if (conditionalTo.test(source))
			{
				return ofNullable(supplier.get());
			}
			return empty();
		};
	}

}
