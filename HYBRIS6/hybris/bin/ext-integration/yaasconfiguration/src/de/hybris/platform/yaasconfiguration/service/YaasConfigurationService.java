/*
* [y] hybris Platform
*
* Copyright (c) 2017 SAP SE or an SAP affiliate company.
* All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*
*/

package de.hybris.platform.yaasconfiguration.service;


import de.hybris.platform.yaasconfiguration.model.AbstractYaasServiceMappingModel;
import de.hybris.platform.yaasconfiguration.model.YaasClientCredentialModel;
import de.hybris.platform.yaasconfiguration.model.YaasServiceModel;

import java.util.List;
import java.util.Map;


/**
 * Focuses on methods to retrieve configuration items like yaas application
 */
public interface YaasConfigurationService
{
	/**
	 *
	 * @param clientCredential
	 * @param clientType
	 * @return YaaS configuration
	 */
	<T> Map<String, String> buildYaasConfig(YaasClientCredentialModel clientCredential, Class<T> clientType);

	/**
	 *
	 * @param id
	 * @return YaasClientCredentialModel
	 */
	YaasClientCredentialModel getYaasClientCredentialForId(final String id);

	/**
	 *
	 * @param id
	 * @return YaasServiceModel
	 */
	YaasServiceModel getYaasServiceForId(final String id);

	/**
	 *
	 * @param id
	 * @param serviceModel
	 * @return AbstractYaasServiceMappingModel
	 */
	AbstractYaasServiceMappingModel getBaseSiteServiceMappingForId(final String id, final YaasServiceModel serviceModel);

	/**
	 *
	 * @return all configured YaasClientCredential
	 */
	List<YaasClientCredentialModel> getYaasClientCredentials();

}
