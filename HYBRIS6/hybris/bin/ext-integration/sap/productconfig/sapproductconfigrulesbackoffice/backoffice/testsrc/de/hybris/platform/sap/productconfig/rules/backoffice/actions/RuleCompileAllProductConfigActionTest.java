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

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.sap.productconfig.rules.model.ProductConfigSourceRuleModel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class RuleCompileAllProductConfigActionTest
{
	@InjectMocks
	private RuleCompileAllProductConfigAction classUnderTest;

	@Mock
	private RuleService mockedRulesService;

	@Before
	public void setUp()
	{
		classUnderTest = new RuleCompileAllProductConfigAction();
		MockitoAnnotations.initMocks(this);


	}

	@Test
	public void testRetrieveRulesToCompile()
	{
		final ArrayList<AbstractRuleModel> expectedRulesToCompile = new ArrayList<>();
		given(mockedRulesService.getAllToBePublishedRulesForType(ProductConfigSourceRuleModel.class)).willReturn(
				expectedRulesToCompile);
		final List<AbstractRuleModel> rulesToCompile = classUnderTest.retrieveRulesToCompile().get();
		assertSame(expectedRulesToCompile, rulesToCompile);
	}
}
