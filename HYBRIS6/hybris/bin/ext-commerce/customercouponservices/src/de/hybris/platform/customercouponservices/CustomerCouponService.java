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
package de.hybris.platform.customercouponservices;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.customercouponservices.impl.DefaultCustomerCouponService;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;

import java.util.List;
import java.util.Optional;


/**
 * Customer Coupon Service {@link DefaultCustomerCouponService}
 */
public interface CustomerCouponService extends CouponService
{
	/**
	 * Get all coupons of specific customer
	 *
	 * @param customer
	 *           the specific customer
	 * @param pageableData
	 *           the pageable data
	 * @return paged list of SingleCodeCouponModel
	 */
	SearchPageData<CustomerCouponModel> getCustomerCouponsForCustomer(final CustomerModel customer, final PageableData pageableData);

	/**
	 * Get promotion source rule for given coupon code
	 *
	 * @param couponCode
	 *           the given coupon code
	 * @return the Optional of PromotionSourceRuleModel
	 */
	List<PromotionSourceRuleModel> getPromotionSourceRuleForCouponCode(final String couponCode);

	/**
	 * Add custom to the coupon group.
	 *
	 * @param couponCode
	 *           coupon code
	 * @param customer
	 *           customer model
	 */
	void assignCouponToCustomer(final String couponCode, final CustomerModel customer);


	/**
	 * Get promotion source rules code list for product
	 *
	 * @param productCode
	 *           product code
	 * @return promotion source rules code list
	 *
	 */

	List<PromotionSourceRuleModel> getPromotionSourceRulesForProduct(final String productCode);

	/**
	 * Get excluded promotion source rules code list for product
	 *
	 * @param productCode
	 *           product code
	 * @return promotion source rules code list
	 *
	 */
	List<PromotionSourceRuleModel> getExclPromotionSourceRulesForProduct(final String productCode);

	/**
	 * Get promotion source rules code list for category
	 *
	 * @param categoryCode
	 *           category code
	 * @return promotion source rules code list
	 *
	 */

	List<PromotionSourceRuleModel> getPromotionSourceRulesForCategory(final String categoryCode);

	/**
	 * Get excluded promotion source rules code list for category
	 *
	 * @param categoryCode
	 *           category code
	 * @return promotion source rules code list
	 *
	 */
	List<PromotionSourceRuleModel> getExclPromotionSourceRulesForCategory(final String categoryCode);

	/**
	 * Get customer coupon code for promotion source rule
	 *
	 * @param code
	 *           coupon code
	 * @return coupon code list
	 */
	List<String> getCouponCodeForPromotionSourceRule(final String code);

	/**
	 * Find products is show all products, when coupon not asign with products and category condition will show all
	 * products.
	 *
	 * @param code
	 *           promotion code
	 * @return product and category number mapping with promotion souce rule.
	 */
	int countProductOrCategoryForPromotionSourceRule(final String code);


	/**
	 * Get the customer coupon with enddate >= now for coupon code
	 *
	 * @param couponCode
	 *           customer coupon code
	 * @return Optional of CustomerCouponModel
	 */
	Optional<CustomerCouponModel> getValidCustomerCouponByCode(final String couponCode);

	/**
	 * Save customer interest coupon message.
	 *
	 * @param couponCode
	 *           coupon code
	 */
	void saveCouponNotification(final String couponCode);

	/**
	 * Remove customer interest coupon message.
	 *
	 * @param couponCode
	 *           coupon code
	 */
	void removeCouponNotificationByCode(final String couponCode);

	/**
	 * Return coupon notification status by coupon code
	 *
	 * @param couponCode
	 *           coupon code
	 * @return The customer watch the coupon will return true.
	 */
	boolean getCouponNotificationStatus(final String couponCode);

	/**
	 * Remove a record from CustomerCoupon
	 *
	 * @param couponCode
	 *           the coupon code
	 * @param customer
	 *           the specific customer
	 */
	void removeCouponForCustomer(final String couponCode, final CustomerModel customer);

	/**
	 * Get all coupons of specific customer
	 *
	 * @param customer
	 *           the specific customer
	 * @return list of CustomerCouponModel
	 */
	List<CustomerCouponModel> getEffectiveCustomerCouponsForCustomer(final CustomerModel customer);

	/**
	 * get all assignable customer coupons can assign to given customer for search text
	 *
	 * @param customer
	 *           the given customer
	 * @param text
	 *           search text
	 * @return List<CustomerCouponModel> list of CustomerCouponModel
	 */
	List<CustomerCouponModel> getAssignableCustomerCoupons(final CustomerModel customer, final String text);

	/**
	 * get assigned coupons for given customer and search text
	 *
	 * @param customer
	 *           the given customer
	 * @param text
	 *           search text
	 * @returnList<CustomerCouponModel> list of CustomerCouponModel
	 */
	List<CustomerCouponModel> getAssignedCustomerCouponsForCustomer(final CustomerModel customer, final String text);

	/**
	 * Get customer Coupon for code
	 * 
	 * @param couponCode
	 *           coupon code
	 * @return CustomerCouponModel only return customer coupon,single and multi coupon will return null.
	 */
	Optional<CustomerCouponModel> getCustomerCouponForCode(final String couponCode);
}
