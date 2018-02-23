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
package de.hybris.platform.b2b.hmc.util;

import de.hybris.platform.b2b.jalo.B2BCreditLimit;
import de.hybris.platform.b2b.model.B2BCreditLimitModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.util.B2BDateUtils;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.hmc.HMCHelper;
import de.hybris.platform.hmc.util.action.ActionEvent;
import de.hybris.platform.hmc.util.action.ActionResult;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.util.StandardDateRange;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @deprecated Since 4.4.
 */
@Deprecated
@SuppressWarnings("rawtypes")
public class CalculateCreditExtended extends B2BItemAction
{

	protected static final Logger LOG = Logger.getLogger(CalculateCreditExtended.class);
	private B2BDateUtils b2bDateUtils;

	@Override
	public ActionResult perform(final ActionEvent event) throws JaloBusinessException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Action called for " + getItem(event));
		}
		final StringBuilder response = new StringBuilder(40);

		final B2BCreditLimitModel creditLimit = getModelService().get((B2BCreditLimit) getItem(event));
		final Collection<B2BUnitModel> b2bUnits = creditLimit.getUnit();
		final CurrencyModel currentCurrency = getCommonI18NService().getCurrentCurrency();
		final NumberFormat currencyFormat = getFormatFactory().createCurrencyFormat();

		for (final B2BUnitModel b2bunit : b2bUnits)
		{

			final String spent = HMCHelper.getLocalizedString("action.b2bcalculatecreditextended.spent");
			final String remaining = HMCHelper.getLocalizedString("action.b2bcalculatecreditextended.remaining");

			final BigDecimal orderTotal = getOrderTotalForUnit(b2bunit, currentCurrency);

			final BigDecimal convertedTotalLimit = new BigDecimal(
					getCommonI18NService().convertCurrency(creditLimit.getCurrency().getConversion().doubleValue(),
							currentCurrency.getConversion().doubleValue(), creditLimit.getAmount().doubleValue()));

			final BigDecimal remainingBalance = convertedTotalLimit.subtract(orderTotal);

			response.append("B2BUnit ID: ").append(b2bunit.getUid()).append(": ").append(spent).append(' ')
					.append(currencyFormat.format(orderTotal)).append(' ').append(currentCurrency.getIsocode()).append(".  ")
					.append(remaining).append(' ').append(currencyFormat.format(remainingBalance)).append(' ')
					.append(currentCurrency.getIsocode()).append('\n');
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Action returning " + response);
		}

		return new ActionResult(ActionResult.OK, response.toString(), false, true);
	}



	private BigDecimal getOrderTotalForUnit(final B2BUnitModel unit, final CurrencyModel currency)
	{
		{
			BigDecimal result = BigDecimal.ZERO;
			final Set<B2BUnitModel> units = getB2bUnitService().getBranch(unit);
			final StandardDateRange dateRange = this.getDateRangeForCreditLimit(unit.getCreditLimit());

			for (final B2BUnitModel b2bUnit : units)
			{
				result = this.toMoney(getDefaultB2BOrderDao().findOrderTotalsByDateRangeAndCurrency(b2bUnit, dateRange.getStart(),
						dateRange.getEnd(), currency)).add(result);
			}
			return result;
		}

	}


	private StandardDateRange getDateRangeForCreditLimit(final B2BCreditLimitModel creditLimit)
	{

		if (creditLimit.getDateRange() != null)
		{
			return getB2bDateUtils().createDateRange(creditLimit.getDateRange());
		}
		else
		{
			return creditLimit.getDatePeriod();
		}
	}


	private BigDecimal toMoney(final Double orderTotal)
	{
		final BigDecimal totalForOrders = new BigDecimal(orderTotal.doubleValue(), new MathContext(16, RoundingMode.HALF_UP))
				.setScale(2, RoundingMode.HALF_UP);
		return totalForOrders;
	}


	@Override
	public boolean isActive(final ActionEvent actionEvent)
	{
		return getItem(actionEvent) != null;
	}



	protected B2BDateUtils getB2bDateUtils()
	{
		return b2bDateUtils;
	}


	@Required
	public void setB2bDateUtils(final B2BDateUtils b2bDateUtils)
	{
		this.b2bDateUtils = b2bDateUtils;
	}

}
