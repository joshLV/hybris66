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

package de.hybris.platform.timedaccesspromotionengineservices.daos;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;

import java.util.List;
import java.util.Optional;


/**
 * DAO to provide methods for querying.
 */
public interface FlashBuyDao
{

	/**
	 * Find FlashBuyCouponModel by given promotion code.
	 *
	 * @param promotionCode
	 *           promotion code
	 * @return Optional<FlashBuyCouponModel> Optional of FlashBuyCouponModel
	 */
	Optional<FlashBuyCouponModel> findFlashBuyCouponByPromotionCode(String promotionCode);

	/**
	 * Find promotion source rule by product
	 *
	 * @param productCode
	 *           product code
	 * @return Promotion souce rule model
	 */
	List<PromotionSourceRuleModel> findPromotionSourceRuleByProduct(String productCode);

	/**
	 * Find Product by given promotion
	 *
	 * @param promotion
	 *           the given promotion
	 * @return Optional<ProductModel> Optional of ProductModel
	 */
	Optional<ProductModel> findProductByPromotion(AbstractPromotionModel promotion);

	/**
	 * Find ProductForPromotionRule by given promotion source rule
	 *
	 * @param rule
	 * @return list of ProductForPromotionSourceRuleModel
	 */
	List<ProductForPromotionSourceRuleModel> findProductForPromotionSourceRules(final PromotionSourceRuleModel rule);

	/**
	 * Find all Products by given promotion source rule
	 *
	 * @param rule
	 *           promotion source rule
	 * @return List<ProductModel> list of ProductModel
	 */
	List<ProductModel> findAllProductsByPromotionSourceRule(final PromotionSourceRuleModel rule);

	/**
	 * Find flash buy coupon by product
	 *
	 * @param product
	 *           Product model
	 * @return List<FlashBuyCouponModel> list of FlashBuyCouponModel
	 */
	List<FlashBuyCouponModel> findFlashBuyCouponByProduct(final ProductModel product);

}
