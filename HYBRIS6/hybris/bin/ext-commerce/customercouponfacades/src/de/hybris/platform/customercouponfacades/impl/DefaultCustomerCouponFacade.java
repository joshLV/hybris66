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
package de.hybris.platform.customercouponfacades.impl;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.CustomerCouponFacade;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.strategies.CustomerCouponRemovableStrategy;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * An implementation of {@link CustomerCouponFacade}
 */
public class DefaultCustomerCouponFacade implements CustomerCouponFacade
{

	private static final String SUCCESS = "text.coupon.check.success";
	private static final String EXIST = "text.coupon.check.exist";
	private static final String ERROR = "text.coupon.check.error";

	private UserService userService;
	private CustomerCouponService customerCouponService;
	private Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter;
	private VoucherFacade voucherFacade;
	private CartFacade cartFacade;
	private CustomerCouponRemovableStrategy customerCouponRemovableStrategy;


	@Override
	public SearchPageData<CustomerCouponData> getPagedCouponsData(final PageableData pageableData)
	{
		final SearchPageData<CustomerCouponData> pagedCouponsData = new SearchPageData<>();
		List<CustomerCouponData> couponsData = new ArrayList<>(0);

		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();

		final SearchPageData<CustomerCouponModel> pagedCouponModels = getCustomerCouponService().getCustomerCouponsForCustomer(
				customer, pageableData);
		if (pagedCouponModels != null)
		{
			couponsData = Converters.convertAll(pagedCouponModels.getResults(), getCustomerCouponConverter());
			pagedCouponsData.setPagination(pagedCouponModels.getPagination());
			pagedCouponsData.setSorts(pagedCouponModels.getSorts());
		}

		pagedCouponsData.setResults(couponsData);

		return pagedCouponsData;
	}

	@Override
	public List<CustomerCouponData> getCouponsData()
	{
		List<CustomerCouponData> couponData = new ArrayList<>(0);

		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();

		final List<CustomerCouponModel> couponModels = getCustomerCouponService().getEffectiveCustomerCouponsForCustomer(customer);
		if (CollectionUtils.isNotEmpty(couponModels))
		{
			couponData = Converters.convertAll(couponModels, getCustomerCouponConverter());
		}

		return couponData;
	}

	@Override
	public String grantCouponAccessForCurrentUser(final String couponCode)
	{
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();

		final Optional<CustomerCouponModel> coupon = getCustomerCouponService().getValidCustomerCouponByCode(couponCode);

		if (coupon.isPresent())
		{
			final Collection<CustomerModel> customers = coupon.get().getCustomers();
			if (CollectionUtils.isEmpty(customers) || !customers.contains(customer)) 
			{
				getCustomerCouponService().assignCouponToCustomer(couponCode, customer);
				return SUCCESS;
			}
			else
			{
				return EXIST;
			}
		}
		return ERROR;
	}

	@Override
	public void saveCouponNotification(final String couponCode)
	{
		getCustomerCouponService().saveCouponNotification(couponCode);
	}

	@Override
	public void removeCouponNotificationByCode(final String couponCode)
	{
		getCustomerCouponService().removeCouponNotificationByCode(couponCode);
	}

	@Override
	public List<CustomerCouponData> getAssignableCustomerCoupons(final String text)
	{
		return Converters.convertAll(
				getCustomerCouponService().getAssignableCustomerCoupons((CustomerModel) getUserService().getCurrentUser(), text),
				getCustomerCouponConverter());
	}

	@Override
	public List<CustomerCouponData> getAssignedCustomerCoupons(final String text)
	{
		return Converters.convertAll(
				getCustomerCouponService().getAssignedCustomerCouponsForCustomer((CustomerModel) getUserService().getCurrentUser(),
						text), getCustomerCouponConverter());
	}

	@Override
	public void releaseCoupon(final String couponCode) throws VoucherOperationException
	{
		if (getCustomerCouponRemovableStrategy().checkRemovable(couponCode))
		{
			getCustomerCouponService().removeCouponForCustomer(couponCode, (CustomerModel) getUserService().getCurrentUser());
			getCustomerCouponService().removeCouponNotificationByCode(couponCode);

			final CartData cart = getCartFacade().getSessionCart();
			if (cart != null && cart.getAppliedVouchers().contains(couponCode))
			{
				getVoucherFacade().releaseVoucher(couponCode);
			}
		}
	}

	@Override
	public CustomerCouponData getCustomerCouponForCode(final String couponCode)
	{
		return getCustomerCouponService().getCustomerCouponForCode(couponCode)
				.map(coupon -> getCustomerCouponConverter().convert(coupon)).orElse(null);
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected CustomerCouponService getCustomerCouponService()
	{
		return customerCouponService;
	}

	@Required
	public void setCustomerCouponService(final CustomerCouponService customerCouponService)
	{
		this.customerCouponService = customerCouponService;
	}


	protected Converter<CustomerCouponModel, CustomerCouponData> getCustomerCouponConverter()
	{
		return customerCouponConverter;
	}

	@Required
	public void setCustomerCouponConverter(final Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter)
	{
		this.customerCouponConverter = customerCouponConverter;
	}

	protected VoucherFacade getVoucherFacade()
	{
		return voucherFacade;
	}

	@Required
	public void setVoucherFacade(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	protected CustomerCouponRemovableStrategy getCustomerCouponRemovableStrategy()
	{
		return customerCouponRemovableStrategy;
	}

	@Required
	public void setCustomerCouponRemovableStrategy(final CustomerCouponRemovableStrategy customerCouponRemovableStrategy)
	{
		this.customerCouponRemovableStrategy = customerCouponRemovableStrategy;
	}

}
