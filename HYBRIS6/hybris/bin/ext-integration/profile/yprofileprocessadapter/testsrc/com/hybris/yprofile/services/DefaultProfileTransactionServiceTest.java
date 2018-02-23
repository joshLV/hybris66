package com.hybris.yprofile.services;

import com.hybris.yprofile.dto.Order;
import com.hybris.yprofile.dto.User;
import com.hybris.yprofile.rest.clients.ProfileClient;
import com.hybris.yprofile.rest.clients.ProfileResponse;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@UnitTest
public class DefaultProfileTransactionServiceTest {

    private static final String CONSENT_REFERENCE = "consent-reference-id";
    private static final String SITE_ID = "test";

    private DefaultProfileTransactionService defaultProfileService;

    @Mock
    private ProfileClient client;

    @Mock
    private ProfileResponse response;

    @Mock
    private ProfileConfigurationService profileConfigurationService;

    @Mock
    private RetrieveRestClientStrategy retrieveRestClientStrategy;

    @Mock
    private Converter<OrderModel, Order> profileOrderEventConverter;

    @Mock
    private Converter<ConsignmentModel, Order> profileConsignmentEventConverter;

    @Mock
    private Converter<ReturnRequestModel, Order> profileReturnEventConverter;

    @Mock
    private Converter<UserModel, User> profileUserEventConverter;

    @Mock
    private UserModel user;
    @Mock
    private OrderModel order;
    @Mock
    private ConsignmentModel consignment;
    @Mock
    private ReturnRequestModel returnRequest;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        defaultProfileService = new DefaultProfileTransactionService();
        defaultProfileService.setRetrieveRestClientStrategy(retrieveRestClientStrategy);
        defaultProfileService.setProfileConfigurationService(profileConfigurationService);
        defaultProfileService.setProfileConsignmentEventConverter(profileConsignmentEventConverter);
        defaultProfileService.setProfileOrderEventConverter(profileOrderEventConverter);
        defaultProfileService.setProfileReturnEventConverter(profileReturnEventConverter);
        defaultProfileService.setProfileUserEventConverter(profileUserEventConverter);

        when(order.getConsentReference()).thenReturn(CONSENT_REFERENCE);
        when(returnRequest.getOrder()).thenReturn(order);
        when(consignment.getOrder()).thenReturn(order);

        when(retrieveRestClientStrategy.getProfileRestClient()).thenReturn(client);
        Order orderDTO = mock(Order.class);
        when(orderDTO.getChannelRef()).thenReturn(SITE_ID);

        when(profileOrderEventConverter.convert(any(OrderModel.class))).thenReturn(orderDTO);
        when(profileConsignmentEventConverter.convert(any(ConsignmentModel.class))).thenReturn(orderDTO);
        when(profileReturnEventConverter.convert(any(ReturnRequestModel.class))).thenReturn(orderDTO);


        User userDTO = mock(User.class);
        when(userDTO.getChannelRef()).thenReturn(SITE_ID);

        when(profileUserEventConverter.convert(user)).thenReturn(userDTO);
    }

    @Test
    public void verifyOrderWithValidConsentReferenceIsSentToYProfile(){

        when(client.sendTransaction(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresentForBaseSiteId(SITE_ID)).thenReturn(true);

        defaultProfileService.sendSubmitOrderEvent(order);

        verify(client, times(1)).sendTransaction(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyDeliveryWithValidConsentReferenceIsSentToYProfile(){

        when(client.sendTransaction(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresentForBaseSiteId(SITE_ID)).thenReturn(true);

        defaultProfileService.sendConsignmentEvent(consignment);

        verify(client, times(1)).sendTransaction(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyReturnOrderWithValidConsentReferenceIsSentToYProfile(){

        when(client.sendTransaction(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresentForBaseSiteId(SITE_ID)).thenReturn(true);

        defaultProfileService.sendReturnOrderEvent(returnRequest);

        verify(client, times(1)).sendTransaction(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyDoNotSendEventToYaasWithInvalidYaaSConfiguration(){

        when(client.sendTransaction(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresentForBaseSiteId(SITE_ID)).thenReturn(false);

        defaultProfileService.sendSubmitOrderEvent(order);

        verify(client, times(0)).sendTransaction(anyString(), anyString(), anyObject());
    }
}