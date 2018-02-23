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

import de.hybris.platform.ruleenginebackoffice.actions.RuleCloneAction;


/**
 * CPQ implementaion of the {@link RuleCloneAction}.<br>
 * Clones a rule in context of the sapproductconfig rule module.
 */
public class RuleCloneProductConfigAction extends RuleCloneAction
{
	static final String PRODUCTCONFIG_RULES_NAVIGATION_NODE = "hmc.cpq.productconfig.sourcerules";

	@Override
	protected String getNavigationNode()
	{
		return PRODUCTCONFIG_RULES_NAVIGATION_NODE;
	}
}
