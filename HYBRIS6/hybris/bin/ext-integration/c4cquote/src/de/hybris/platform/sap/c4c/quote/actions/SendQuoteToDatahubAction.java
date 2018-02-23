package de.hybris.platform.sap.c4c.quote.actions;

import org.apache.log4j.Logger;

import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.QuoteService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubHelper;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubResult;


public class SendQuoteToDatahubAction extends AbstractSimpleDecisionAction<QuoteProcessModel>
{

	private static final Logger LOG = Logger.getLogger(SendQuoteToDatahubAction.class);

	private SendToDataHubHelper<QuoteModel> sendQuoteToDataHubHelper;

	private QuoteService quoteService;

	@Override
	public Transition executeAction(final QuoteProcessModel process)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Received SendQuoteToDatahubAction..");
		}

		Transition transitionResult = Transition.NOK;

		if (process.getQuoteCode() != null)
		{

			final QuoteModel model = quoteService.getCurrentQuoteForCode(process.getQuoteCode());
			final SendToDataHubResult result = sendQuoteToDataHubHelper.createAndSendRawItem(model);
			if (result.isSuccess())
			{
				transitionResult = Transition.OK;
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Completed SendQuoteToDatahubAction with transition " + transitionResult.toString());
		}

		return transitionResult;
	}

	public SendToDataHubHelper<QuoteModel> getSendQuoteToDataHubHelper()
	{
		return sendQuoteToDataHubHelper;
	}

	public void setSendQuoteToDataHubHelper(final SendToDataHubHelper<QuoteModel> sendQuoteToDataHubHelper)
	{
		this.sendQuoteToDataHubHelper = sendQuoteToDataHubHelper;
	}

	public QuoteService getQuoteService()
	{
		return quoteService;
	}

	public void setQuoteService(final QuoteService quoteService)
	{
		this.quoteService = quoteService;
	}
}
