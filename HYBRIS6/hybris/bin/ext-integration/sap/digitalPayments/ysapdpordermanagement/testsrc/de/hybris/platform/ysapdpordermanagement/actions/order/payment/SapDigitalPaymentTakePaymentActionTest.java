package de.hybris.platform.ysapdpordermanagement.actions.order.payment;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cissapdigitalpayment.exceptions.SapDigitalPaymentCaptureException;
import de.hybris.platform.cissapdigitalpayment.service.SapDigitalPaymentService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition;
import de.hybris.platform.servicelayer.model.ModelService;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SapDigitalPaymentTakePaymentActionTest
{
	
	OrderProcessModel orderProcessModel;
	OrderModel orderModel;
	PaymentTransactionModel paymentTransactionModel;
	PaymentTransactionEntryModel paymentTransactionEntryModel;
	
	@InjectMocks
	SapDigitalPaymentTakePaymentAction action = new SapDigitalPaymentTakePaymentAction();
	
	@Mock
	private SapDigitalPaymentService sapDigitalPaymentService;

	@Mock
	private ModelService modelService;
	
	
	@Before
	public void setup() throws SapDigitalPaymentCaptureException
	{
		paymentTransactionEntryModel = spy(new PaymentTransactionEntryModel());

		paymentTransactionModel = new PaymentTransactionModel();
		paymentTransactionModel.setPaymentProvider("PaymentProvider");
		final List<PaymentTransactionModel> paymentTransactions = new ArrayList<>();
		paymentTransactions.add(paymentTransactionModel);

		orderModel = new OrderModel();
		orderModel.setPaymentTransactions(paymentTransactions);

		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(orderModel);

		when(sapDigitalPaymentService.capture(paymentTransactionModel)).thenReturn(paymentTransactionEntryModel);
	}
	
	@Test
	public void checkTransitionStatusAndOrderStatusForAccepted()
	{
		
		when(paymentTransactionEntryModel.getTransactionStatus()).thenReturn(TransactionStatus.ACCEPTED.name());
		final Transition transition = action.executeAction(orderProcessModel);
		assertTrue(AbstractSimpleDecisionAction.Transition.OK.toString().equals(transition.toString()));
		assertTrue(orderModel.getStatus().toString().equals(OrderStatus.PAYMENT_CAPTURED.toString()));
	}
	
	@Test
	public void checkTransitionStatusAndOrderStatusForNotAccepted()
	{
		
		when(paymentTransactionEntryModel.getTransactionStatus()).thenReturn(TransactionStatus.ERROR.name());
		final Transition transition = action.executeAction(orderProcessModel);
		assertTrue(AbstractSimpleDecisionAction.Transition.NOK.toString().equals(transition.toString()));
		assertTrue(orderModel.getStatus().toString().equals(OrderStatus.PAYMENT_NOT_CAPTURED.toString()));
	}
	
		

}
