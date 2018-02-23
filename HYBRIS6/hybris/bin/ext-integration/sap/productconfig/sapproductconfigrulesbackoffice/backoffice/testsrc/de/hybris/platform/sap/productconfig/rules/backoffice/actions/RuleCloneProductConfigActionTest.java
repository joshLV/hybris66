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
package de.hybris.platform.sap.productconfig.rules.backoffice.actions;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class RuleCloneProductConfigActionTest
{

	private RuleCloneProductConfigAction classUnderTest;


	@Before
	public void setUp()
	{
		classUnderTest = new RuleCloneProductConfigAction();
	}

	@Test
	public void testGetNavigationNode()
	{
		assertEquals(RuleCloneProductConfigAction.PRODUCTCONFIG_RULES_NAVIGATION_NODE, classUnderTest.getNavigationNode());
	}



}
