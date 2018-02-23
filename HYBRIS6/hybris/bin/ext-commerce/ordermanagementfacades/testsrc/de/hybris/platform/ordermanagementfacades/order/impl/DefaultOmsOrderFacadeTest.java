package de.hybris.platform.ordermanagementfacades.order.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordercancel.CancelDecision;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordermanagementfacades.BaseOrdermanagementFacadeTest;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelEntryData;
import de.hybris.platform.ordermanagementfacades.cancellation.data.OrderCancelRequestData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOmsOrderFacadeTest extends BaseOrdermanagementFacadeTest
{
	@InjectMocks
	private DefaultOmsOrderFacade omsOrderFacade;
	@Mock
	private OrderCancelRequestData orderCancelRequestData;
	@Mock
	private OrderCancelService orderCancelService;
	@Mock
	private CancelDecision cancelDecision;
	@Mock
	private OrderCancelEntryData orderCancelEntryData;
	@Mock
	private OrderCancelEntryData orderCancelEntryData2;
	@Mock
	private OrderEntryModel orderEntry;
	@Mock
	private OrderEntryModel orderEntry2;

	List<OrderCancelEntryData> orderCancelEntryDatas = new ArrayList<>();
	List<HybrisEnumValue> cancelReasons = new ArrayList<>();
	Map<AbstractOrderEntryModel, Long> orderEntryModelLongMap = new HashedMap<>();

	@Before
	public void setup()
	{
		omsOrderFacade.setImpersonationService(impersonationService);
		omsOrderFacade.setOrderService(orderService);
		omsOrderFacade.setBaseSiteService(baseSiteService);
		omsOrderFacade.setBaseStoreService(baseStoreService);

		omsOrderFacade.setCustomerReverseConverter(customerReverseConverter);
		omsOrderFacade.setOrderConverter(orderConverter);
		omsOrderFacade.setOrderRequestReverseConverter(orderRequestReverseConverter);
		//B2C Specific
		when(userService.getUserForUID(USER_UID)).thenReturn(userModel);
	}

	@Test
	public void submitOrder_Success()
	{
		//When
		prepareOrderRequestData();
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
		//Verify
		(orderService).submitOrder(any(OrderModel.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullOrderRequestData()
	{
		omsOrderFacade.submitOrder(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullStoreUid()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getStoreUid()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullSiteUid()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getSiteUid()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullDeliveryModeCode()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getDeliveryModeCode()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullLanguageIsoCode()
	{
		prepareOrderRequestData();
		//Then
		when(orderRequestData.getLanguageIsocode()).thenReturn(null);
		//Given
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullCurrencyIsoCode()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getCurrencyIsocode()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureIsNotCalculated()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.isCalculated()).thenReturn(false);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullExternalOrderCode()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getExternalOrderCode()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullUser()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getUser()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullUserUid()
	{
		//When
		prepareOrderRequestData();
		when(customerData.getUid()).thenReturn(null);

		//Given
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullAddressData()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getDeliveryAddress()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullAddressTown()
	{
		prepareOrderRequestData();
		//When
		when(addressData.getTown()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullAddressCountry()
	{
		prepareOrderRequestData();
		//When
		when(addressData.getCountry()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullAddressPostalCode()
	{
		prepareOrderRequestData();
		//When
		when(addressData.getPostalCode()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullAddressLine()
	{
		prepareOrderRequestData();
		//When
		when(addressData.getLine1()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureEmptyFirstName_NewUser()
	{
		prepareOrderRequestData();
		//When
		when(userService.isUserExisting(USER_UID)).thenReturn(false);
		when(customerData.getFirstName()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureEmptyLastName_NewUser()
	{
		prepareOrderRequestData();
		//When
		when(userService.isUserExisting(USER_UID)).thenReturn(false);
		when(customerData.getLastName()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureEmptyCompanyName()
	{
		prepareOrderRequestData();
		//When
		when(addressData.getFirstName()).thenReturn("");
		when(addressData.getLastName()).thenReturn("");
		when(addressData.getCompanyName()).thenReturn("");
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void submitOrder_FailureNullPaymentTransactions()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getPaymentTransactions()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void submitOrder_FailureNullPaymentTransactionsEntries()
	{
		prepareOrderRequestData();
		//When
		when(paymentTransactionData.getEntries()).thenReturn(null);
		//Given
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void submitOrder_FailureNullOrderEntryData()
	{
		prepareOrderRequestData();
		//When
		when(orderRequestData.getEntries()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullOrderEntryDataEntryNumber()
	{
		prepareOrderRequestData();
		//When
		when(orderEntryRequestData.getEntryNumber()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullOrderEntryDataUnitCode()
	{
		prepareOrderRequestData();
		//When
		when(orderEntryRequestData.getUnitCode()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}


	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureNullOrderEntryDataProductCode()
	{
		prepareOrderRequestData();
		//When
		when(orderEntryRequestData.getProductCode()).thenReturn(null);
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void submitOrder_FailureStoreNotInSite()
	{
		prepareOrderRequestData();
		//When
		when(baseSiteModel.getStores()).thenReturn(Collections.emptyList());
		//Then
		omsOrderFacade.submitOrder(orderRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void approveFraudulentOrder_FailureNullCode()
	{
		omsOrderFacade.approvePotentiallyFraudulentOrder(null);
	}

	@Test(expected = IllegalStateException.class)
	public void approveFraudulentOrder_FailureWrongStatus()
	{
		prepareOrderRequestData();
		//When
		when(orderModel.getStatus()).thenReturn(OrderStatus.CANCELLED);
		//Then
		omsOrderFacade.approvePotentiallyFraudulentOrder(ORDER_ID);
	}

	@Test
	public void approveFraudulentOrder_Success()
	{
		prepareOrderRequestData();
		//When
		omsOrderFacade.approvePotentiallyFraudulentOrder(ORDER_ID);
		//Verify
		verify(businessProcessService).triggerEvent(Matchers.<String>any());
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectFraudulentOrder_FailureNullCode()
	{
		omsOrderFacade.rejectPotentiallyFraudulentOrder(null);
	}

	@Test(expected = IllegalStateException.class)
	public void rejectFraudulentOrder_FailureWrongStatus()
	{
		prepareOrderRequestData();
		//Given
		when(orderModel.getStatus()).thenReturn(OrderStatus.CANCELLED);
		//When
		omsOrderFacade.rejectPotentiallyFraudulentOrder(ORDER_ID);
	}

	@Test
	public void rejectFraudulentOrder_Success()
	{
		prepareOrderRequestData();
		//When
		omsOrderFacade.rejectPotentiallyFraudulentOrder(ORDER_ID);
		//Verify
		verify(businessProcessService).triggerEvent(Matchers.<String>any());
	}

	@Test
	public void cancelOrder_SingleEntry_Success_PartialCancel() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(userModel, 100L, 1L, CancelReason.CUSTOMERREQUEST, null);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
		//Verify
		verify(orderCancelService).requestOrderCancel(any(OrderCancelRequest.class), any(UserModel.class));
	}

	@Test
	public void cancelOrder_MultiEntries_Success_PartialCancel() throws OrderCancelException
	{
		//give
		prepareCancellationTwoEntries(userModel, 100L, 1L, CancelReason.CUSTOMERREQUEST, null);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
		//Verify
		verify(orderCancelService).requestOrderCancel(any(OrderCancelRequest.class), any(UserModel.class));
	}

	@Test
	public void cancelOrder_MultiEntries_Success_PartialEntryCancel() throws OrderCancelException
	{
		//give
		prepareCancellationTwoEntries(userModel, 100L, 1L, CancelReason.CUSTOMERREQUEST, null);
		orderCancelEntryDatas.remove(orderCancelEntryData2);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
		//Verify
		verify(orderCancelService).requestOrderCancel(any(OrderCancelRequest.class), any(UserModel.class));
	}

	@Test
	public void cancelOrder_SingleEntry_Success_CompleteCancel_withNote() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(userModel, 1L, 1L, CancelReason.CUSTOMERREQUEST, ORDER_ID);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
		//Verify
		verify(orderCancelService).requestOrderCancel(any(OrderCancelRequest.class), any(UserModel.class));
		verify(orderCancelService).requestOrderCancel(any(OrderCancelRequest.class), any(UserModel.class));
	}

	@Test(expected = IllegalStateException.class)
	public void cancelOrder_Fail_CancelQuantityTooMuch() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(userModel, 1L, 2L, CancelReason.CUSTOMERREQUEST, null);

		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
	}

	@Test(expected = IllegalStateException.class)
	public void cancelOrder_Fail_WrongEntry() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(userModel, 1L, 2L, CancelReason.CUSTOMERREQUEST, null);
		when(orderEntry.getEntryNumber()).thenReturn(2);

		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void cancelOrder_Fail_WithoutReason() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(userModel, 1L, 2L, null, null);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void cancelOrder_Fail_WithoutUser() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(null, 1L, 2L, CancelReason.CUSTOMERREQUEST, null);
		when(orderCancelRequestData.getUserId()).thenReturn(null);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void cancelOrder_Fail_NullCancelQuantity() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(null, 1L, null, CancelReason.CUSTOMERREQUEST, null);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
	}

	@Test(expected = IllegalStateException.class)
	public void cancelOrder_Fail_CancelPossibleFail() throws OrderCancelException
	{
		//give
		prepareCancellationSingleEntry(userModel, 1L, 2L, CancelReason.CUSTOMERREQUEST, null);
		when(cancelDecision.isAllowed()).thenReturn(false);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void cancelOrder_MultiEntries_Fail_WithoutReason_PartialEntryCancel() throws OrderCancelException
	{
		//give
		prepareCancellationTwoEntries(userModel, 100L, 1L, CancelReason.CUSTOMERREQUEST, null);
		when(orderCancelEntryData2.getCancelReason()).thenReturn(null);
		//When
		omsOrderFacade.createRequestOrderCancel(orderCancelRequestData);
		//Verify
		verify(orderCancelService).requestOrderCancel(any(OrderCancelRequest.class), any(UserModel.class));
	}

	/**
	 * Prepares mock for cancel order with single entry
	 *
	 * @param userModel
	 * @param EntryQuantity
	 * @param CancelQuantity
	 * @param cancelReasonEntry
	 * @param entryNote
	 */
	protected void prepareCancellationSingleEntry(UserModel userModel, Long EntryQuantity, Long CancelQuantity,
			CancelReason cancelReasonEntry, String entryNote)
	{
		prepareOrderRequestData();

		//orderEntry
		when(orderEntry.getQuantity()).thenReturn(EntryQuantity);
		when(orderEntry.getEntryNumber()).thenReturn(0);
		when(orderService.getEntryForNumber(orderModel, orderEntry.getEntryNumber())).thenReturn(orderEntry);
		when(orderEntry.getOrder()).thenReturn(orderModel);

		//orderCancelEntryData
		when(orderCancelEntryData.getCancelReason()).thenReturn(cancelReasonEntry);
		when(orderCancelEntryData.getCancelQuantity()).thenReturn(CancelQuantity);
		when(orderCancelEntryData.getNotes()).thenReturn(entryNote);
		orderCancelEntryDatas.add(orderCancelEntryData);

		//orderCancelRequestData
		when(orderCancelRequestData.getEntries()).thenReturn(orderCancelEntryDatas);
		when(orderCancelRequestData.getUserId()).thenReturn(USER_UID);
		when(orderCancelRequestData.getOrderCode()).thenReturn(ORDER_ID);

		when(cancelDecision.isAllowed()).thenReturn(true);
		when(orderCancelService.isCancelPossible(any(OrderModel.class), any(UserModel.class), anyBoolean(), anyBoolean()))
				.thenReturn(cancelDecision);

		orderEntryModelLongMap.put(orderEntry, EntryQuantity);
		when(orderCancelService.getAllCancelableEntries(orderModel, userModel)).thenReturn(orderEntryModelLongMap);
		cancelReasons.add(CancelReason.CUSTOMERREQUEST);
		when(enumerationService.getEnumerationValues(CancelReason._TYPECODE)).thenReturn(cancelReasons);
	}

	/**
	 * Prepares mock for cancel order with 2 entries
	 *
	 * @param userModel
	 * @param EntryQuantity
	 * @param CancelQuantity
	 * @param cancelReasonEntry
	 * @param entryNote
	 */
	protected void prepareCancellationTwoEntries(UserModel userModel, Long EntryQuantity, Long CancelQuantity,
			CancelReason cancelReasonEntry, String entryNote)
	{
		prepareOrderRequestData();

		//orderEntry
		when(orderEntry.getQuantity()).thenReturn(EntryQuantity);
		when(orderEntry.getEntryNumber()).thenReturn(0);
		when(orderService.getEntryForNumber(orderModel, orderEntry.getEntryNumber())).thenReturn(orderEntry);
		when(orderEntry.getOrder()).thenReturn(orderModel);

		when(orderEntry2.getQuantity()).thenReturn(EntryQuantity);
		when(orderEntry2.getEntryNumber()).thenReturn(1);
		when(orderEntry2.getOrder()).thenReturn(orderModel);
		when(orderService.getEntryForNumber(orderModel, orderEntry2.getEntryNumber())).thenReturn(orderEntry2);

		//orderCancelEntryData
		when(orderCancelEntryData.getCancelReason()).thenReturn(cancelReasonEntry);
		when(orderCancelEntryData.getCancelQuantity()).thenReturn(CancelQuantity);
		when(orderCancelEntryData.getNotes()).thenReturn(entryNote);
		when(orderCancelEntryData.getOrderEntryNumber()).thenReturn(0);
		when(orderCancelEntryData2.getCancelReason()).thenReturn(cancelReasonEntry);
		when(orderCancelEntryData2.getCancelQuantity()).thenReturn(CancelQuantity);
		when(orderCancelEntryData2.getNotes()).thenReturn(entryNote);
		when(orderCancelEntryData2.getOrderEntryNumber()).thenReturn(1);
		orderCancelEntryDatas.add(orderCancelEntryData);
		orderCancelEntryDatas.add(orderCancelEntryData2);

		//orderCancelRequestData
		when(orderCancelRequestData.getEntries()).thenReturn(orderCancelEntryDatas);
		when(orderCancelRequestData.getUserId()).thenReturn(USER_UID);
		when(orderCancelRequestData.getOrderCode()).thenReturn(ORDER_ID);

		when(cancelDecision.isAllowed()).thenReturn(true);
		when(orderCancelService.isCancelPossible(eq(orderModel), eq(userModel), anyBoolean(), anyBoolean()))
				.thenReturn(cancelDecision);

		orderEntryModelLongMap.put(orderEntry, EntryQuantity);
		orderEntryModelLongMap.put(orderEntry2, EntryQuantity);
		when(orderCancelService.getAllCancelableEntries(orderModel, userModel)).thenReturn(orderEntryModelLongMap);
		cancelReasons.add(CancelReason.CUSTOMERREQUEST);
		when(enumerationService.getEnumerationValues(CancelReason._TYPECODE)).thenReturn(cancelReasons);
	}

}