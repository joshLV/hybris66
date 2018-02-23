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
package de.hybris.platform.personalizationfacades.customersegmentation.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.converters.ConfigurableConverter;
import de.hybris.platform.personalizationfacades.data.CustomerData;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationfacades.enums.CustomerSegmentationConversionOptions;
import de.hybris.platform.personalizationfacades.exceptions.AlreadyExistsException;
import de.hybris.platform.personalizationfacades.segmentation.SegmentationHelper;
import de.hybris.platform.personalizationfacades.segmentation.impl.DefaultSegmentationHelper;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCustomerSegmentationFacadeTest
{

	private static final String CUSTOMER_ID = "customer";
	private static final String NOTEXISTING_CUSTOMER_ID = "nonExistCustomer";
	private static final String NOTRELATED_CUSTOMER_ID = "notRelatedCustomer";
	private static final String SEGMENT_ID = "segment";
	private static final String NOTEXISTING_SEGMENT_ID = "nonExistSegment";
	private static final String NOTRELATED_SEGMENT_ID = "notRelatedSegment";

	private String SEGMENTATION_ID;


	private String NOTEXISTING_SEGMENTATION_ID;
	private String CREATED_SEGMENTATION_ID;
	private static final String INCORRECT_SEGMENTATION_ID = "incorrectId";
	private final DefaultCustomerSegmentationFacade customerSegmentationFacade = new DefaultCustomerSegmentationFacade();

	@Mock
	private ModelService modelService;
	@Mock
	private CxSegmentService segmentService;
	@Mock
	private UserService userService;
	@Mock
	Converter<CustomerSegmentationData, CxUserToSegmentModel> segmentationReverseConverter;
	@Mock
	private ConfigurableConverter<CxUserToSegmentModel, CustomerSegmentationData, CustomerSegmentationConversionOptions> segmentationConverter;
	private final SegmentationHelper segmentationHelper = new DefaultSegmentationHelper();
	private CxSegmentModel segment;


	private CustomerModel customer;
	private CxUserToSegmentModel customerToSegment;
	private List<CxUserToSegmentModel> customerToSegmentList;
	private CustomerData customerData;
	private SegmentData segmentData;
	private CustomerSegmentationData customerSegmentationData;
	private SearchPageData<CxUserToSegmentModel> customerSegmentPaginatedResponse;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		customerSegmentationFacade.setModelService(modelService);
		customerSegmentationFacade.setSegmentService(segmentService);
		customerSegmentationFacade.setUserService(userService);
		customerSegmentationFacade.setSegmentationConverter(segmentationConverter);
		customerSegmentationFacade.setSegmentationReverseConverter(segmentationReverseConverter);
		customerSegmentationFacade.setSegmentationHelper(segmentationHelper);

		segment = new CxSegmentModel();
		segment.setCode(SEGMENT_ID);
		customer = new CustomerModel();
		customer.setUid(CUSTOMER_ID);
		customer.setUserToSegments(Collections.emptyList());
		customerToSegment = new CxUserToSegmentModel();
		customerToSegment.setSegment(segment);
		customerToSegment.setUser(customer);

		customerToSegmentList = Collections.singletonList(customerToSegment);

		customerSegmentPaginatedResponse = createSearchPageData(customerToSegmentList, 1, 0, 1, 1);

		customerData = new CustomerData();
		customerData.setUid(CUSTOMER_ID);
		segmentData = new SegmentData();
		segmentData.setCode(SEGMENT_ID);


		SEGMENTATION_ID = segmentationHelper.getSegmentationCode(SEGMENT_ID, CUSTOMER_ID);
		customerSegmentationData = new CustomerSegmentationData();
		customerSegmentationData.setCode(SEGMENTATION_ID);
		customerSegmentationData.setCustomer(customerData);
		customerSegmentationData.setSegment(segmentData);

		NOTEXISTING_SEGMENTATION_ID = segmentationHelper.getSegmentationCode(NOTRELATED_SEGMENT_ID, NOTRELATED_CUSTOMER_ID);
		CREATED_SEGMENTATION_ID = segmentationHelper.getSegmentationCode(NOTRELATED_SEGMENT_ID, NOTRELATED_CUSTOMER_ID);

	}

	private <T> SearchPageData<T> createSearchPageData()
	{
		return createSearchPageData(Collections.emptyList(), 0, 0, 0, 0);
	}

	private <T> SearchPageData<T> createSearchPageDataSingle()
	{
		return createSearchPageData(Collections.emptyList(), 1, 0, 1, 1);
	}

	private <T> SearchPageData<T> createSearchPageData(final List<T> results, final int pageSize, final int currentPage,
			final int totalResults, final int totalPages)
	{
		final SearchPageData<T> page = new SearchPageData<>();
		page.setPagination(new PaginationData());
		page.getPagination().setPageSize(pageSize);
		page.getPagination().setCurrentPage(currentPage);
		page.getPagination().setNumberOfPages(totalPages);
		page.getPagination().setTotalNumberOfResults(totalResults);
		page.setResults(results);

		return page;
	}

	//Tests for getCustomerSegmentation
	@Test
	public void getCustomerSegmentationTest()
	{
		//given
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));
		Mockito.when(segmentService.getUserToSegmentModel(eq(customer), eq(segment), any()))
				.thenReturn(customerSegmentPaginatedResponse);
		Mockito.when(segmentationConverter.convert(any(), any(List.class))).thenReturn(customerSegmentationData);

		//when
		final CustomerSegmentationData result = customerSegmentationFacade.getCustomerSegmentation(SEGMENTATION_ID);

		//then
		Assert.assertNotNull(result);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertSame(customerData, result.getCustomer());
		Assert.assertSame(segmentData, result.getSegment());
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getNotExistingCustomerSegmentationTest()
	{
		final SearchPageData<CxUserToSegmentModel> emptySearchResult = createSearchPageData();

		//given
		Mockito.when(userService.getUserForUID(NOTRELATED_CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(NOTRELATED_SEGMENT_ID)).thenReturn(Optional.of(segment));
		Mockito.when(segmentService.getUserToSegmentModel(eq(customer), eq(segment), any())).thenReturn(emptySearchResult);

		//when
		customerSegmentationFacade.getCustomerSegmentation(NOTEXISTING_SEGMENTATION_ID);
	}


	@Test(expected = UnknownIdentifierException.class)
	public void getCustomerSegmentationForNotExistingCustomerTest()
	{
		//given
		Mockito.when(userService.getUserForUID(NOTEXISTING_CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));

		//when
		customerSegmentationFacade
				.getCustomerSegmentation(segmentationHelper.getSegmentationCode(SEGMENT_ID, NOTEXISTING_CUSTOMER_ID));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getCustomerSegmentationForNotExistingSegmentTest()
	{
		//given
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(NOTEXISTING_SEGMENT_ID)).thenReturn(Optional.empty());

		//when
		customerSegmentationFacade
				.getCustomerSegmentation(segmentationHelper.getSegmentationCode(NOTEXISTING_SEGMENT_ID, CUSTOMER_ID));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCustomerSegmentationForIncorrectIdTest()
	{
		//when
		customerSegmentationFacade.getCustomerSegmentation(INCORRECT_SEGMENTATION_ID);
	}

	//Tests for getCustomerSegmentations

	@Test
	public void getCustomerSegmentationsTest()
	{
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));
		Mockito.when(segmentService.getUserToSegmentModel(eq(customer), eq(segment), any()))
				.thenReturn(customerSegmentPaginatedResponse);
		Mockito
				.when(segmentationConverter.convertAll(eq(customerToSegmentList),
						Matchers.<CustomerSegmentationConversionOptions> anyVararg()))
				.thenReturn(Collections.singletonList(customerSegmentationData));

		//when
		final SearchPageData<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(CUSTOMER_ID,
				SEGMENT_ID, createSearchPageDataSingle());

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResults());
		Assert.assertEquals(1, resultList.getResults().size());
		final CustomerSegmentationData result = resultList.getResults().get(0);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertSame(customerData, result.getCustomer());
		Assert.assertSame(segmentData, result.getSegment());
	}

	@Test
	public void getCustomerSegmentationsForCustomerTest()
	{
		//given
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getUserToSegmentModel(eq(customer), any(), any())).thenReturn(customerSegmentPaginatedResponse);
		Mockito
				.when(segmentationConverter.convertAll(eq(customerToSegmentList),
						Matchers.<CustomerSegmentationConversionOptions> anyVararg()))
				.thenReturn(Collections.singletonList(customerSegmentationData));

		//when
		final SearchPageData<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(CUSTOMER_ID,
				null, createSearchPageDataSingle());

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResults());
		Assert.assertEquals(1, resultList.getResults().size());
		final CustomerSegmentationData result = resultList.getResults().get(0);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertSame(customerData, result.getCustomer());
		Assert.assertSame(segmentData, result.getSegment());
	}

	@Test
	public void getCustomerSegmentationsForSegmentTest()
	{
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));
		Mockito.when(segmentService.getUserToSegmentModel(any(), eq(segment), any())).thenReturn(customerSegmentPaginatedResponse);
		Mockito
				.when(segmentationConverter.convertAll(eq(customerToSegmentList),
						Matchers.<CustomerSegmentationConversionOptions> anyVararg()))
				.thenReturn(Collections.singletonList(customerSegmentationData));

		//when
		final SearchPageData<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(null,
				SEGMENT_ID, createSearchPageDataSingle());

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResults());
		Assert.assertEquals(1, resultList.getResults().size());
		final CustomerSegmentationData result = resultList.getResults().get(0);
		Assert.assertEquals(SEGMENTATION_ID, result.getCode());
		Assert.assertSame(customerData, result.getCustomer());
		Assert.assertSame(segmentData, result.getSegment());
	}

	@Test
	public void getCustomerSegmentationsForNotExistingCustomerTest()
	{
		//given
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));

		//when
		final SearchPageData<CustomerSegmentationData> resultList = customerSegmentationFacade
				.getCustomerSegmentations(NOTEXISTING_CUSTOMER_ID, SEGMENT_ID, createSearchPageDataSingle());

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResults());
		Assert.assertTrue(resultList.getResults().isEmpty());
	}

	@Test
	public void getCustomerSegmentationsForNotExistingSegmentTest()
	{
		//given
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(NOTEXISTING_SEGMENT_ID)).thenReturn(Optional.empty());

		//when
		final SearchPageData<CustomerSegmentationData> resultList = customerSegmentationFacade.getCustomerSegmentations(CUSTOMER_ID,
				NOTEXISTING_SEGMENT_ID, createSearchPageDataSingle());

		//then
		Assert.assertNotNull(resultList);
		Assert.assertNotNull(resultList.getResults());
		Assert.assertTrue(resultList.getResults().isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getCustomerSegmentationsForNullParametersTest()
	{
		//when
		customerSegmentationFacade.getCustomerSegmentations(null, null, createSearchPageDataSingle());
	}

	//Tests for create method

	@Test
	public void createCustomerSegmentationsTest()
	{
		//given
		customer.setUid(NOTRELATED_CUSTOMER_ID);
		segment.setCode(NOTRELATED_SEGMENT_ID);
		final CxUserToSegmentModel model = new CxUserToSegmentModel();
		customerData.setUid(NOTRELATED_CUSTOMER_ID);
		segmentData.setCode(NOTRELATED_SEGMENT_ID);
		customerSegmentationData.setCode(CREATED_SEGMENTATION_ID);

		Mockito.when(segmentService.getSegment(NOTRELATED_SEGMENT_ID)).thenReturn(Optional.of(segment));
		Mockito.when(userService.getUserForUID(NOTRELATED_CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentationReverseConverter.convert(customerSegmentationData)).thenReturn(model);
		Mockito.when(segmentationConverter.convert(any(), any(List.class))).thenReturn(customerSegmentationData);


		//when
		final CustomerSegmentationData result = customerSegmentationFacade.createCustomerSegmentation(customerSegmentationData);

		//then
		Assert.assertNotNull(result);
		Assert.assertEquals(CREATED_SEGMENTATION_ID, result.getCode());
		Assert.assertSame(customerData, result.getCustomer());
		Assert.assertSame(segmentData, result.getSegment());
	}

	@Test(expected = AlreadyExistsException.class)
	public void createAltreadyExistedCustomerSegmentationTest()
	{
		//given
		final CxUserToSegmentModel model = new CxUserToSegmentModel();
		model.setUser(customer);
		model.setSegment(segment);
		final Set<CxUserToSegmentModel> userToSegments = Collections.singleton(model);
		customer.setUserToSegments(userToSegments);

		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));
		Mockito.when(segmentService.getUserToSegmentForUser(customer)).thenReturn(userToSegments);
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);

		//when
		customerSegmentationFacade.createCustomerSegmentation(customerSegmentationData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createCustomerSegmentationWithNullSegmentTest()
	{
		//given
		customerSegmentationData.setSegment(null);

		//when
		customerSegmentationFacade.createCustomerSegmentation(customerSegmentationData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createCustomerSegmentationWithNullCustomerTest()
	{
		//given
		customerSegmentationData.setCustomer(null);

		//when
		customerSegmentationFacade.createCustomerSegmentation(customerSegmentationData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void createCustomerSegmentationForNotExistingCustomerTest()
	{
		//given
		customerSegmentationData.getCustomer().setUid(NOTEXISTING_CUSTOMER_ID);
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));

		//when
		customerSegmentationFacade.createCustomerSegmentation(customerSegmentationData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void createCustomerSegmentationsForNotExistingSegmentTest()
	{
		//given
		customerSegmentationData.getSegment().setCode(NOTEXISTING_SEGMENT_ID);
		Mockito.when(segmentService.getSegment(NOTEXISTING_SEGMENT_ID)).thenReturn(Optional.empty());

		//when
		customerSegmentationFacade.createCustomerSegmentation(customerSegmentationData);
	}

	//delete method tests

	@Test
	public void deleteCustomerSegmentationTest()
	{
		//given
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));
		Mockito.when(segmentService.getUserToSegmentModel(eq(customer), eq(segment), any()))
				.thenReturn(customerSegmentPaginatedResponse);

		//when
		customerSegmentationFacade.deleteCustomerSegmentation(SEGMENTATION_ID);

		//then
		Mockito.verify(modelService).remove(customerToSegment);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void deleteNotExistingCustomerSegmentationTest()
	{
		//given
		Mockito.when(userService.getUserForUID(NOTRELATED_CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(NOTRELATED_SEGMENT_ID)).thenReturn(Optional.of(segment));


		//when
		customerSegmentationFacade
				.deleteCustomerSegmentation(segmentationHelper.getSegmentationCode(NOTRELATED_SEGMENT_ID, NOTRELATED_CUSTOMER_ID));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void deleteCustomerSegmentationForNotExistingCustomerTest()
	{
		//given
		Mockito.when(segmentService.getSegment(SEGMENT_ID)).thenReturn(Optional.of(segment));

		//when
		customerSegmentationFacade
				.deleteCustomerSegmentation(segmentationHelper.getSegmentationCode(SEGMENT_ID, NOTEXISTING_CUSTOMER_ID));
	}

	@Test(expected = UnknownIdentifierException.class)
	public void deleteCustomerSegmentationForNotExistingSegmentTest()
	{
		//given
		Mockito.when(userService.getUserForUID(CUSTOMER_ID)).thenReturn(customer);
		Mockito.when(segmentService.getSegment(NOTEXISTING_SEGMENT_ID)).thenReturn(Optional.empty());

		//when
		customerSegmentationFacade
				.deleteCustomerSegmentation(segmentationHelper.getSegmentationCode(NOTEXISTING_SEGMENT_ID, CUSTOMER_ID));
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteCustomerSegmentationWithIncorrectIdTest()
	{
		//when
		customerSegmentationFacade.deleteCustomerSegmentation(INCORRECT_SEGMENTATION_ID);
	}
}
