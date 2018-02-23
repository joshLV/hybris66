package de.hybris.platform.sap.c4c.quote.decorators;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.sap.c4c.quote.inbound.InboundQuoteVersionControlHelper;
import de.hybris.platform.util.CSVCellDecorator;

/**
 * Decorator class for returning Quote PK
 */
public class QuotePkCellDecorator implements CSVCellDecorator
{

	private static final Logger LOG = LoggerFactory.getLogger(QuotePkCellDecorator.class);
	private InboundQuoteVersionControlHelper inboundQuoteVersionControlHelper = (InboundQuoteVersionControlHelper) Registry
			.getApplicationContext().getBean("inboundQuoteVersionControlHelper");
	
	@Override
	public String decorate(int position, Map<Integer, String> impexLine)
	{
		final String quoteId = impexLine.get(Integer.valueOf(position));
		QuoteModel quote = getInboundQuoteVersionControlHelper().getQuoteforCode(quoteId);
		StringBuilder result = new StringBuilder();
		if (quote != null)
		{
			result = result.append(quote.getPk());
		}
		else
		{
			LOG.error("No quote exist in system with quoteId= " + quoteId);
		}
		return result.toString();
	}

	public InboundQuoteVersionControlHelper getInboundQuoteVersionControlHelper()
	{
		return inboundQuoteVersionControlHelper;
	}

	public void setInboundQuoteVersionControlHelper(InboundQuoteVersionControlHelper inboundQuoteVersionControlHelper)
	{
		this.inboundQuoteVersionControlHelper = inboundQuoteVersionControlHelper;
	}

}
