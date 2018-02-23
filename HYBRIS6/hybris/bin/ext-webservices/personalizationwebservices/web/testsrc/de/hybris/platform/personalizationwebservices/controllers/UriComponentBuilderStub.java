/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.personalizationwebservices.controllers;

import org.springframework.web.util.UriComponentsBuilder;


public class UriComponentBuilderStub extends UriComponentsBuilder
{

	UriComponentBuilderStub()
	{
		scheme("http");
		port(9090);
		host("test.local");
	}
}