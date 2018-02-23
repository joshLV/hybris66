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

import de.hybris.platform.ruleenginebackoffice.actions.RuleCompileAllForModuleAction;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.sap.productconfig.rules.model.ProductConfigSourceRuleModel;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * The class is used to retrieve all product configuration source rules which could be compiled.
 */
public class RuleCompileAllProductConfigAction extends RuleCompileAllForModuleAction
{

	@Override
	protected Supplier<List<AbstractRuleModel>> retrieveRulesToCompile()
	{
		return () -> getRuleService().getAllToBePublishedRulesForType(ProductConfigSourceRuleModel.class);
	}

	@Override
	protected Predicate<AbstractRuleModel> filterSelectedRules()
	{
		return r -> r instanceof ProductConfigSourceRuleModel;
	}
}
