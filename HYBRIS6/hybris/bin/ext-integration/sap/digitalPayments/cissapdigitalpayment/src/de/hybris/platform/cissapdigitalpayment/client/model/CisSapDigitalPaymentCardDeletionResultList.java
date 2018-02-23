/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.cissapdigitalpayment.client.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * SAP Digital payment deletion result list
 */
public class CisSapDigitalPaymentCardDeletionResultList
{

	@JsonProperty("DeletedPaymentCards")
	private List<CisSapDigitalPaymentCardDeletionResult> cisSapDigitalPaymentCardDeletionResult;

	/**
	 * @return the cisSapDigitalPaymentCardDeletionResult
	 */
	public List<CisSapDigitalPaymentCardDeletionResult> getCisSapDigitalPaymentCardDeletionResult()
	{
		return cisSapDigitalPaymentCardDeletionResult;
	}

	/**
	 * @param cisSapDigitalPaymentCardDeletionResult
	 *           the cisSapDigitalPaymentCardDeletionResult to set
	 */
	public void setCisSapDigitalPaymentCardDeletionResult(
			final List<CisSapDigitalPaymentCardDeletionResult> cisSapDigitalPaymentCardDeletionResult)
	{
		this.cisSapDigitalPaymentCardDeletionResult = cisSapDigitalPaymentCardDeletionResult;
	}


}
