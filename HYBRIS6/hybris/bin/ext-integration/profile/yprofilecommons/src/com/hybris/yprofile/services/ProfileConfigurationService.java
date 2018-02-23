/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.hybris.yprofile.services;

/**
 * Focuses on methods to retrieve yaas and profile configuration items
 */
public interface ProfileConfigurationService {

    /**
     * Cookie and Session attribute key
     */
    String PROFILE_TRACKING_PAUSE = "profile.tracking.pause";


    /**
     * checks whether the profile tracking is enabled
     *
     * @return
     */
    boolean isProfileTrackingPaused();


    /**
     * Stores in session if profile tracking is paused
     *
     * @param isProfileTrackingPause
     */
    void setProfileTrackingPauseValue(final boolean isProfileTrackingPause);

    /**
     * Checks if the Yaas configuration is present
     *
     * @param siteId base site identifier
     * @return true or false
     */
    boolean isYaaSConfigurationPresentForBaseSiteId(final String siteId);

    /**
     * Returns the project identifier in Yaas, alias "tenant"
     * @param siteId base site identifier
     * @return tenant identifier
     */
    String getYaaSTenant(final String siteId);

}
