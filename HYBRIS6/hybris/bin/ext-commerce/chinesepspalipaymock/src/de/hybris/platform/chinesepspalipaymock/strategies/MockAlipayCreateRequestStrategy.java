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
package de.hybris.platform.chinesepspalipaymock.strategies;

import de.hybris.platform.chinesepspalipaymock.service.MockService;
import de.hybris.platform.chinesepspalipayservices.data.AlipayCancelPaymentRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayPaymentStatusRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.chinesepspalipayservices.strategies.impl.DefaultAlipayCreateRequestStrategy;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class MockAlipayCreateRequestStrategy extends DefaultAlipayCreateRequestStrategy
{
	private MockService mockService;

	@Override
	public AlipayRawPaymentStatus submitPaymentStatusRequest(final AlipayPaymentStatusRequestData checkRequest)
			throws ReflectiveOperationException
	{
		final Map<String, String> alipayPaymentStatusRequestData = describeRequest(checkRequest);
		final String xmlString = getMockService().getPaymentStatusRequest(alipayPaymentStatusRequestData);
		if (StringUtils.isNotEmpty(xmlString))
		{
			final AlipayRawPaymentStatus alipayRawPaymentStatus = (AlipayRawPaymentStatus) parserXML(xmlString,
					"de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus");
			return alipayRawPaymentStatus;
		}
		return null;
	}

	@Override
	public AlipayRawCancelPaymentResult submitCancelPaymentRequest(final AlipayCancelPaymentRequestData closeRequest)
			throws ReflectiveOperationException
	{
		final Map<String, String> alipayCancelPaymentRequestData = describeRequest(closeRequest);
		final String xmlString = getMockService().getPaymentStatusRequest(alipayCancelPaymentRequestData);
		if (StringUtils.isNotEmpty(xmlString))
		{
			final AlipayRawCancelPaymentResult alipayRawCancelPaymentResult = (AlipayRawCancelPaymentResult) parserXML(xmlString,
					"de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult");
			return alipayRawCancelPaymentResult;
		}
		return null;
	}

	protected MockService getMockService()
	{
		return mockService;
	}

	@Required
	public void setMockService(final MockService mockService)
	{
		this.mockService = mockService;
	}

}
