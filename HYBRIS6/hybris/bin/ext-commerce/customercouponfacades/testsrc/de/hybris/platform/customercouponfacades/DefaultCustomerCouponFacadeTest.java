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
package de.hybris.platform.customercouponfacades;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.ProductSearchService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.impl.DefaultCustomerCouponFacade;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * Unit test for {@link DefaultCustomerCouponFacade}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(
{ Converters.class })
@UnitTest
public class DefaultCustomerCouponFacadeTest
{
	private static final String COUPON_ID = "testid";
	private static final String SEARCH_TEXT = "search-text";

	private static final String SUCCESS = "text.coupon.check.success";
	private static final String EXIST = "text.coupon.check.exist";
	private static final String ERROR = "text.coupon.check.error";

	private DefaultCustomerCouponFacade facade;

	@Mock
	private UserService userService;
	@Mock
	private CustomerCouponService customerCouponService;
	@Mock
	private Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter;

	@Mock
	private CustomerModel customer;
	@Mock
	private PageableData pageableData;
	@Mock
	private SearchPageData<CustomerCouponModel> pagedCouponModels;
	@Mock
	private CustomerCouponModel couponModel;
	@Mock
	private List<CustomerCouponModel> couponModels;
	@Mock
	private List<CustomerCouponData> coupons;
	@Mock
	private CustomerCouponData coupon;
	@Mock
	private PaginationData pagination;
	@Mock
	private List<SortData> sorts;
	@Mock
	private Converter<ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>, ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>> productCategorySearchPageConverter;
	@Mock
	private ProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>> productSearchService;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(Converters.class);

		facade = new DefaultCustomerCouponFacade();
		facade.setUserService(userService);
		facade.setCustomerCouponService(customerCouponService);
		facade.setCustomerCouponConverter(customerCouponConverter);

		Mockito.when(userService.getCurrentUser()).thenReturn(customer);
		Mockito.when(customerCouponService.getCustomerCouponsForCustomer(customer, pageableData)).thenReturn(pagedCouponModels);
		Mockito.when(customerCouponService.getEffectiveCustomerCouponsForCustomer(customer)).thenReturn(couponModels);
		Mockito.when(pagedCouponModels.getResults()).thenReturn(couponModels);
		PowerMockito.when(Converters.convertAll(couponModels, customerCouponConverter)).thenReturn(coupons);
		Mockito.when(pagedCouponModels.getPagination()).thenReturn(pagination);
		Mockito.when(pagedCouponModels.getSorts()).thenReturn(sorts);
	}

	@Test
	public void testGetPagedCouponsData()
	{
		final SearchPageData<CustomerCouponData> result = facade.getPagedCouponsData(pageableData);

		Assert.assertEquals(coupons, result.getResults());
		Assert.assertEquals(pagination, result.getPagination());
		Assert.assertEquals(sorts, result.getSorts());
	}

	@Test
	public void testGetCouponsData()
	{
		final List<CustomerCouponData> result = facade.getCouponsData();

		Assert.assertEquals(coupons, result);
	}

	@Test
	public void testGrantCouponAccessForCurrentUserSuccess()
	{
		couponModel = new CustomerCouponModel();
		couponModel.setCouponId(COUPON_ID);
		final Optional<CustomerCouponModel> optional = Optional.of(couponModel);
		Mockito.when(customerCouponService.getValidCustomerCouponByCode(Mockito.anyString())).thenReturn(optional);
		final String result = facade.grantCouponAccessForCurrentUser(COUPON_ID);
		Assert.assertEquals(SUCCESS, result);
	}

	@Test
	public void testGrantCouponAccessForCurrentUserExist()
	{
		couponModel = new CustomerCouponModel();
		couponModel.setCouponId(COUPON_ID);
		couponModel.setCustomers(Collections.singleton(customer));
		final Optional<CustomerCouponModel> optional = Optional.of(couponModel);
		Mockito.when(customerCouponService.getValidCustomerCouponByCode(Mockito.anyString())).thenReturn(optional);
		final String result = facade.grantCouponAccessForCurrentUser(COUPON_ID);
		Assert.assertEquals(EXIST, result);
	}

	@Test
	public void testGrantCouponAccessForCurrentUserError()
	{
		final Optional<CustomerCouponModel> optional = Optional.empty();
		Mockito.when(customerCouponService.getValidCustomerCouponByCode(Mockito.anyString())).thenReturn(optional);
		final String result = facade.grantCouponAccessForCurrentUser(COUPON_ID);
		Assert.assertEquals(ERROR, result);
	}

	@Test
	public void testGetAssignableCustomerCoupons()
	{
		Mockito.when(customerCouponService.getAssignableCustomerCoupons(customer, SEARCH_TEXT)).thenReturn(couponModels);
		final List<CustomerCouponData> result = facade.getAssignableCustomerCoupons(SEARCH_TEXT);

		Assert.assertEquals(coupons, result);
	}

	@Test
	public void testGetAssignedCustomerCoupons()
	{
		Mockito.when(customerCouponService.getAssignedCustomerCouponsForCustomer(customer, SEARCH_TEXT)).thenReturn(couponModels);
		final List<CustomerCouponData> result = facade.getAssignedCustomerCoupons(SEARCH_TEXT);

		Assert.assertEquals(coupons, result);
	}

	@Test
	public void testGetCustomerCouponForCode()
	{
		Mockito.when(customerCouponService.getCustomerCouponForCode(COUPON_ID)).thenReturn(Optional.of(couponModel));
		PowerMockito.when(customerCouponConverter.convert(couponModel)).thenReturn(coupon);
		final CustomerCouponData result = facade.getCustomerCouponForCode(COUPON_ID);
		Assert.assertEquals(coupon, result);
	}
}
