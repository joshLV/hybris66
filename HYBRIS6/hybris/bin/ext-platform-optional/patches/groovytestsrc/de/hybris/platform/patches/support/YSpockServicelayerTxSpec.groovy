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
package de.hybris.platform.patches.support

import de.hybris.platform.testframework.Transactional
import spock.lang.Ignore

/**
 * Spock transactional service layer specification
 * Its just an marker class. Similar to {@link de.hybris.platform.servicelayer.ServicelayerTransactionalTest}
 */
@Ignore
@Transactional
class YSpockServicelayerTxSpec extends YSpockServicelayerSpec
{
    //marker class
}
