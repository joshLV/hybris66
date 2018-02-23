package de.hybris.platform.sap.c4c.quote.actions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.QuoteService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubHelper;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubResult;
import de.hybris.platform.task.RetryLaterException;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@UnitTest
public class SendQuoteToDatahubActionTest
{
	@InjectMocks
	private SendQuoteToDatahubAction sendQuoteToDatahubAction = new SendQuoteToDatahubAction();

	@Mock
	private QuoteService quoteService;
	
	@Mock
	private SendToDataHubHelper<QuoteModel> sendQuoteToDataHubHelper;
	
	@Before
   public void setUp() {
       MockitoAnnotations.initMocks(this);
   }
	
	 @Test
    public void testExecuteActionWithoutQuoteCode() throws RetryLaterException, Exception {
        QuoteProcessModel process = mock(QuoteProcessModel.class);
        SendToDataHubResult datahubResult = mock(SendToDataHubResult.class);
        QuoteModel quote = null;
        when(process.getQuoteCode()).thenReturn("12345");
        when(quoteService.getCurrentQuoteForCode(Mockito.anyString())).thenReturn(quote);
        when(sendQuoteToDataHubHelper.createAndSendRawItem(Mockito.anyObject())).thenReturn(datahubResult);
        when(datahubResult.isSuccess()).thenReturn(false);
        Transition result = sendQuoteToDatahubAction.executeAction(process);
        Assert.assertEquals(Transition.NOK, result);

    }
	 
	@Test
   public void testExecuteActionWithQuote() throws RetryLaterException, Exception {
		QuoteProcessModel process = mock(QuoteProcessModel.class);
		QuoteModel quote = mock(QuoteModel.class);
      SendToDataHubResult datahubResult = mock(SendToDataHubResult.class);
      when(process.getQuoteCode()).thenReturn("12345");
      when(quoteService.getCurrentQuoteForCode(Mockito.anyString())).thenReturn(quote);
      when(sendQuoteToDataHubHelper.createAndSendRawItem(quote)).thenReturn(datahubResult);
      when(datahubResult.isSuccess()).thenReturn(true);
      Transition result = sendQuoteToDatahubAction.executeAction(process);
      Assert.assertEquals(Transition.OK, result);
	}

}
