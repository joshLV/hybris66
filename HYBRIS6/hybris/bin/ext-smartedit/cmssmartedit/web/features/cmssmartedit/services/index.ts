/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

import * as angular from 'angular';

import {HiddenComponentMenuService} from './hiddenComponentMenuService';
import {SlotContainerService} from './slotContainerService';

export {
	HiddenComponentMenuService,
	SlotContainerService,
};

export const CmsSmarteditServicesModule = angular
	/**
	 * @ngdoc overview
	 * @name cms.smartedit.services
	 * @description
	 * This module contains CMS services available in CmsSmartEdit application.  
	 */
	.module('cms.smartedit.services', ['yLoDashModule', 'experienceServiceModule', 'restServiceFactoryModule'])
	.service('slotContainerService', SlotContainerService)
	.service('hiddenComponentMenuService', HiddenComponentMenuService)
	.name; 
