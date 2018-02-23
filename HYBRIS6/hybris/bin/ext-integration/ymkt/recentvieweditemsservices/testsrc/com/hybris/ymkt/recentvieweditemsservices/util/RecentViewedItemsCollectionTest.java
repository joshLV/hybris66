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
package com.hybris.ymkt.recentvieweditemsservices.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RecentViewedItemsCollectionTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testSerializable() throws Exception
	{
		final RecentViewedItemsCollection c1 = new RecentViewedItemsCollection(2);
		c1.addCode("1");

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(c1);
		oos.close();

		final byte[] bytes = baos.toByteArray();

		final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		final ObjectInputStream ois = new ObjectInputStream(bais);
		final RecentViewedItemsCollection c2 = (RecentViewedItemsCollection) ois.readObject();
		Assert.assertEquals(Collections.singletonList("1"), c2.getCodes());
	}

}
