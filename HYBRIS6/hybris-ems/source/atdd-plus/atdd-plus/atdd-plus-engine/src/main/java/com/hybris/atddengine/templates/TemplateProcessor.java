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

package com.hybris.atddengine.templates;

import java.io.Writer;
import java.util.Map;


/**
 * Template processor.
 */
public interface TemplateProcessor
{
	/**
	 * Process template.
	 *
	 * @param writer where to store result
	 * @param templatePath path to the template
	 * @param binding known objects
	 * @throws Exception exception
	 */
	void processTemplate(final Writer writer, final String templatePath, final Map<String, Object> binding) throws Exception;
}
