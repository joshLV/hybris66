/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.yaasyprofileconnect.constants;

/**
 * Global class for all Yaasyprofileconnect constants. You can add global constants for your extension into this class.
 */
public final class YaasyprofileconnectConstants extends GeneratedYaasyprofileconnectConstants
{
	public static final String EXTENSIONNAME = "yaasyprofileconnect";

	public static final String NAMESPACE_CORE = "core";
	public static final String NAMESPACE_COMMERCE = "commerce";

	public static final String TYPE_IDENTITY = "Identity";
	public static final String TYPE_PROFILE = "Profile";
	public static final String TYPE_PRODUCT_AFFINITY = "ProductAffinity";
	public static final String SCHEMA_CORE_PROFILE = "/core/Profile";
	public static final String SCHEMA_COMMERCE_PRODUCT = "/commerce/Product";
	public static final String SCHEMA_COMMERCE_PRODUCT_AFFINITY = "/commerce/ProductAffinity";
	public static final String SCHEMA_COMMERCE_CATEGORY_AFFINITY = "/commerce/CategoryAffinity";
	public static final String SCHEMA_COMMERCE_KEYWORD_SEARCH_AFFINITY = "/commerce/KeywordSearchAffinity";
	public static final String SCHEMA_COMMERCE_DEVICE_AFFINITY = "/commerce/DeviceTypeAffinity";
	public static final String SCHEMA_COMMERCE_DEVICEFINGERPRINT_AFFINITY = "/commerce/DeviceFingerprintAffinity";
	public static final String PRODUCT_VIEW_COUNT_FIELD = "productViewCount";
	public static final String CATEGORY_VIEW_COUNT_FIELD = "categoryViewCount";
	public static final String AFFINITY_FIELD = "affinity";

	public static final String PRODUCT_ID_FIELD = "productId";
	public static final String CATEGORY_ID_FIELD = "categoryId";

	public static final String KEYWORD_SEARCHID_FIELD = "keywordSearchId";
	public static final String KEYWORD_SEARCH_COUNT_FIELD = "keywordSearchCount";

	public static final String DEVICE_TYPE_FIELD = "deviceType";
	public static final String DEVICE_USED_COUNT_FIELD = "usedCount";
	public static final String DEVICE_OS_FIELD = "operatingSystemNoVersion";
	public static final String DEVICE_BROWSER_FIELD = "browserNoVersion";


	public static final String UPDATED_FIELD = "updated";
	public static final String CREATED_FIELD = "created";

	public final class Loyalty
	{
		public static final String SCHEMA = "loyalty/";
		public static final String SCHEMA_LOYALTY_ACTIVITY_STATISTICS = SCHEMA + "LoyaltyActivityStatistics";
		public static final String SCHEMA_LOYALTY_POINTS_HISTORY = SCHEMA + "LoyaltyPointsHistory";

		public static final String CURRENT_LOYALTY_BALANCE = "currentLoyaltyBalance";
		public static final String NEXT_TIER = "NEXT_TIER";
		public static final String POINTS_TO_NEXT_TIER = "pointsToNextTier";
		public static final String TIER_NAME = "tierName";
		public static final String ACTIVITY_SCORE = "activityScore";
		public static final String REFERRAL_CODE = "referralCode";

		private Loyalty()
		{
			//private constructor
		}
	}

	private YaasyprofileconnectConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
