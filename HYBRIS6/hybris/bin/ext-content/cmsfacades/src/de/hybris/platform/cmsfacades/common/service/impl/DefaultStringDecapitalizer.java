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
package de.hybris.platform.cmsfacades.common.service.impl;

import de.hybris.platform.cmsfacades.common.service.StringDecapitalizer;

import java.util.Optional;

import org.eclipse.persistence.jaxb.DefaultXMLNameTransformer;


/**
 * Default implementation of {@link StringDecapitalizer}.
 */
public class DefaultStringDecapitalizer implements StringDecapitalizer
{
	@Override
	public Optional<String> decapitalize(final Class theClass)
	{
		if (theClass == null)
		{
			return Optional.empty();
		}
		return Optional.of(new DefaultXMLNameTransformer().transformTypeName(theClass.getSimpleName()));
	}
}
