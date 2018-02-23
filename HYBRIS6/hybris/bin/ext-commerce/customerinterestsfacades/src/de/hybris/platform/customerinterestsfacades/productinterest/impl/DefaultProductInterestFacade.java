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
package de.hybris.platform.customerinterestsfacades.productinterest.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestEntryData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestRelationData;
import de.hybris.platform.customerinterestsfacades.productinterest.ProductInterestFacade;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.customerinterestsservices.productinterest.ProductInterestService;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


public class DefaultProductInterestFacade implements ProductInterestFacade//NOSONAR
{
	private ProductInterestService productInterestService;
	private Converter<ProductInterestModel, ProductInterestData> productInterestConverter;
	private Converter<ProductInterestData, ProductInterestModel> productInterestReverseConverter;
	private ProductService productService;
	private UserService userService;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private Converter<Entry<ProductModel, List<ProductInterestEntryData>>, ProductInterestRelationData> productInterestRelationConverter;
	private Converter<Entry<NotificationType, Date>, ProductInterestEntryData> productInterestEntryConverter;

	@Override
	public void saveProductInterest(final ProductInterestData productInterest)
	{
		final ProductInterestModel modifiedproductInterest = getProductInterest(productInterest.getProduct().getCode(),
				productInterest.getNotificationType()).orElse(new ProductInterestModel());
		getProductInterestReverseConverter().convert(productInterest, modifiedproductInterest);
		getProductInterestService().saveProductInterest(modifiedproductInterest);
	}

	@Override
	public void removeProductInterest(final ProductInterestData productInterest)
	{
		getProductInterest(productInterest.getProduct().getCode(), productInterest.getNotificationType())
				.ifPresent(x -> getProductInterestService().removeProductInterest(x));
	}

	@Override
	public Optional<ProductInterestData> getProductInterestDataForCurrentCustomer(final String productcode,
			final NotificationType notificationType)
	{
		return getProductInterest(productcode, notificationType)
				.map(productInterestsInterestModel -> getProductInterestConverter().convert(productInterestsInterestModel));
	}


	@Override
	public void removeProductInterestByProduct(final String productCode)
	{
		getProductInterestService().removeProductInterestByProduct(productCode);
	}

	protected Optional<ProductInterestModel> getProductInterest(final String productcode, final NotificationType notificationType)
	{
		final ProductModel product = getProductService().getProductForCode(productcode);
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final BaseSiteModel baseSite = getBaseSiteService().getCurrentBaseSite();
		return getProductInterestService().getProductInterest(product, customer, notificationType, baseStore, baseSite);
	}

	@Override
	public List<ProductInterestRelationData> getProductsByCustomerInterests(final PageableData pageableData)
	{
		final Map<ProductModel, Map<NotificationType, Date>> productNotificationMap = productInterestService
				.getProductsByCustomerInterests(pageableData);

		final Map<ProductModel, List<ProductInterestEntryData>> productProductInterestMap = new HashMap<>();
		productNotificationMap.forEach((productModel, interstTypeMap) -> {
			final List<ProductInterestEntryData> productInterestEntrys = getProductInterestEntryConverter()
					.convertAll(interstTypeMap.entrySet());
			productProductInterestMap.put(productModel, productInterestEntrys);
		});

		final Set<Entry<ProductModel, List<ProductInterestEntryData>>> productNotifications = productProductInterestMap.entrySet();

		return getProductInterestRelationConverter().convertAll(productNotifications);
	}

	@Override
	public int getProductsCountByCustomerInterests(final PageableData pageableData)
	{

		return productInterestService.getProductsCountByCustomerInterests(pageableData);
	}


	protected ProductInterestService getProductInterestService()
	{
		return productInterestService;
	}

	@Required
	public void setProductInterestService(final ProductInterestService productInterestService)
	{
		this.productInterestService = productInterestService;
	}

	protected Converter<ProductInterestModel, ProductInterestData> getProductInterestConverter()
	{
		return productInterestConverter;
	}

	@Required
	public void setProductInterestConverter(final Converter<ProductInterestModel, ProductInterestData> productInterestConverter)
	{
		this.productInterestConverter = productInterestConverter;
	}

	protected Converter<ProductInterestData, ProductInterestModel> getProductInterestReverseConverter()
	{
		return productInterestReverseConverter;
	}

	@Required
	public void setProductInterestReverseConverter(
			final Converter<ProductInterestData, ProductInterestModel> productInterestReverseConverter)
	{
		this.productInterestReverseConverter = productInterestReverseConverter;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
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

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected Converter<Entry<ProductModel, List<ProductInterestEntryData>>, ProductInterestRelationData> getProductInterestRelationConverter()
	{
		return productInterestRelationConverter;
	}

	@Required
	public void setProductInterestRelationConverter(
			final Converter<Entry<ProductModel, List<ProductInterestEntryData>>, ProductInterestRelationData> productInterestRelationConverter)
	{
		this.productInterestRelationConverter = productInterestRelationConverter;
	}

	protected Converter<Entry<NotificationType, Date>, ProductInterestEntryData> getProductInterestEntryConverter()
	{
		return productInterestEntryConverter;
	}

	@Required
	public void setProductInterestEntryConverter(
			final Converter<Entry<NotificationType, Date>, ProductInterestEntryData> productInterestEntryConverter)
	{
		this.productInterestEntryConverter = productInterestEntryConverter;
	}

}
