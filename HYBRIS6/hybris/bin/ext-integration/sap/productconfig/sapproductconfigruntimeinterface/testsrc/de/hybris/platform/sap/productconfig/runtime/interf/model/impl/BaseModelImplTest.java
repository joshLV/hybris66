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
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigModelFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.ConfigModelFactoryImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.BaseModel;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


@UnitTest
public class BaseModelImplTest
{
	private final BaseModel model = new BaseModelImpl();
	@Mock
	ApplicationContext mockApplicationContext;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testClone()
	{

		final String string1 = "String1";
		final String string2 = "String2";
		final String string3 = "String3";
		model.putExtensionData("1", string1);
		model.putExtensionData("2", string2);
		model.putExtensionData("3", string3);

		final BaseModel clonedModel = model.clone();

		final String clonedString1 = clonedModel.getExtensionData("1");
		final String clonedString2 = clonedModel.getExtensionData("2");
		final String clonedString3 = clonedModel.getExtensionData("3");

		assertFalse(model == clonedModel);
		assertTrue(string1 == clonedString1);
		assertTrue(string2 == clonedString2);
		assertTrue(string3 == clonedString3);

		final String newString1 = "New String 1";
		clonedModel.putExtensionData("1", newString1);
		assertFalse(string1 == clonedModel.getExtensionData("1"));
	}

	@Test
	public void testExtension()
	{
		String extensionData = model.getExtensionData("myExt");
		assertNull(extensionData);
		final Map<String, String> extensionMap = new HashMap();
		extensionMap.put("myExt", "123");
		model.setExtensionMap(extensionMap);
		extensionData = model.getExtensionData("myExt");
		assertEquals("123", extensionData);
		model.putExtensionData("myExt", "567");
		extensionData = model.getExtensionMap().get("myExt");
		assertEquals("567", extensionData);
	}

	@Test
	public void testGetConfigModelFactoryContainsBeanFalse()
	{
		final BaseModelImpl modelImpl = Mockito.spy(new BaseModelImpl());
		Mockito.doReturn(mockApplicationContext).when(modelImpl).getApplicationContext();
		Mockito.doReturn(Boolean.FALSE).when(mockApplicationContext).containsBean("sapProductConfigModelFactory");

		final ConfigModelFactory configModelFactory = modelImpl.getConfigModelFactory();
		assertNotNull(configModelFactory);
		assertTrue(configModelFactory instanceof ConfigModelFactoryImpl);
	}

	@Test
	public void testGetConfigModelFactoryContainsBeanTrue()
	{
		final ConfigModelFactoryImpl factory = new ConfigModelFactoryImpl();
		final BaseModelImpl modelImpl = Mockito.spy(new BaseModelImpl());
		Mockito.doReturn(mockApplicationContext).when(modelImpl).getApplicationContext();
		Mockito.when(mockApplicationContext.getBean("sapProductConfigModelFactory")).thenReturn(factory);
		Mockito.doReturn(Boolean.TRUE).when(mockApplicationContext).containsBean("sapProductConfigModelFactory");

		final ConfigModelFactory configModelFactory = modelImpl.getConfigModelFactory();
		assertNotNull(configModelFactory);
		assertTrue(configModelFactory instanceof ConfigModelFactoryImpl);
	}

	@Test
	public void testGetConfigModelFactoryNotNull()
	{
		final BaseModelImpl modelImpl = Mockito.spy(new BaseModelImpl());
		modelImpl.setConfigModelFactory(new ConfigModelFactoryImpl());
		final ConfigModelFactory configModelFactory = modelImpl.getConfigModelFactory();
		assertNotNull(configModelFactory);
		assertTrue(configModelFactory instanceof ConfigModelFactoryImpl);
	}
}
