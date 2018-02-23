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

package de.hybris.platform.timedaccesspromotionengineservices;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;

import java.util.List;
import java.util.Optional;


/**
 * Service to provide related methods for flash buy.
 */
public interface FlashBuyService
{

	/**
	 * Get Product for given promotion
	 *
	 * @param promotion
	 *           the given promotion
	 * @return Optional<ProductModel> Optional of ProductModel
	 */
	Optional<ProductModel> getProductForPromotion(AbstractPromotionModel promotion);

	/**
	 * Get promotion source rules by product
	 *
	 * @param productCode
	 *           product code
	 * @return list of PromotionSourceRuleModel
	 */
	List<PromotionSourceRuleModel> getPromotionSourceRulesByProductCode(final String productCode);

	/**
	 * Get flash buy coupon by promotion source rule
	 *
	 * @param code
	 *           Promotion code
	 *
	 * @return FlashBuyCoupon Model
	 *
	 */
	Optional<FlashBuyCouponModel> getFlashBuyCouponByPromotionCode(final String code);


	/**
	 * Stop flash buy promotion by promotion
	 *
	 * @param promotionSourceRule
	 *           promotionSourceRule model
	 */
	void undeployFlashBuyPromotion(PromotionSourceRuleModel promotionSourceRule);

	/**
	 * Find Promotion by given promotion code
	 *
	 * @param promotionCode
	 *           the given promotion code
	 * @return AbstractPromotion Model
	 */
	AbstractPromotionModel getPromotionByCode(String promotionCode);

	/**
	 * Create or update cron job for flash buy coupon to reset max order quantity
	 *
	 * @param coupon
	 *           flash buy coupon model
	 */
	void createCronJobForFlashBuyCoupon(FlashBuyCouponModel coupon);

	/**
	 * perform cron job for flash buy directly
	 *
	 * @param coupon
	 *           flash buy coupon model
	 */
	void performFlashBuyCronJob(FlashBuyCouponModel coupon);

	/**
	 * get productforpromotionsourcerule based on promotionsourcerule
	 *
	 * @param promotionSourceRule
	 *           promotion source rule
	 * @return List of ProductForPromotionSourceRuleModel
	 */
	List<ProductForPromotionSourceRuleModel> getProductForPromotionSourceRule(PromotionSourceRuleModel promotionSourceRule);

	/**
	 * Find Product by product code
	 *
	 * @param productCode
	 *           the given productCode
	 * @return List<ProductModel> list of ProductModel
	 */
	List<ProductModel> getProductForCode(String productCode);


	/**
	 * Get all products by promotionsourcerule
	 *
	 * @param rule
	 *           promotion source rule
	 * @return List<ProductModel> list of ProductModel
	 */
	List<ProductModel> getAllProductsByPromotionSourceRule(final PromotionSourceRuleModel rule);

	/**
	 * Get flash buy coupon by product
	 *
	 * @param product
	 *           Product model
	 * @return List<FlashBuyCouponModel> list of FlashBuysCouponModel
	 */
	List<FlashBuyCouponModel> getFlashBuyCouponByProduct(final ProductModel product);

	/**
	 * delete set and reset max order quantity cronjob and trigger by flash buy coupon
	 * 
	 * @param coupon
	 *           flash buy coupon model
	 */
	void deleteCronJobAndTrigger(final FlashBuyCouponModel coupon);

}
