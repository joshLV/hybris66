package de.hybris.platform.sap.c4c.quote.inbound;

import de.hybris.platform.core.enums.QuoteState;

public interface InboundQuoteStateHelper {
	
	QuoteState getQuoteStateforCode(String code);

}
