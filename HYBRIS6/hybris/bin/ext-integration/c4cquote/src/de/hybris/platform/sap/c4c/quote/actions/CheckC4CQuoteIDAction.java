package de.hybris.platform.sap.c4c.quote.actions;

import org.apache.log4j.Logger;

import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.QuoteService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;

public class CheckC4CQuoteIDAction extends AbstractSimpleDecisionAction<QuoteProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CheckC4CQuoteIDAction.class);

	private QuoteService quoteService;

	@Override
	public Transition executeAction(QuoteProcessModel processModel)
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Received CheckC4CQuoteIDAction..");
		}

		Transition result = Transition.NOK;
		if (processModel.getQuoteCode() != null)
		{
			QuoteModel quote = quoteService.getCurrentQuoteForCode(processModel.getQuoteCode());
			if (quote != null && quote.getC4cQuoteId() != null)
			{
				result = Transition.OK;
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Completed CheckC4CQuoteIDAction with transition " + result.toString());
		}

		return result;
	}

	public QuoteService getQuoteService()
	{
		return quoteService;
	}

	public void setQuoteService(QuoteService quoteService)
	{
		this.quoteService = quoteService;
	}
}
