import * as angular from 'angular';

import {SharedDataService, SharedDataServiceModule} from './sharedDataService';

export {
	SharedDataService
};

export const SmarteditContainerServicesModule = angular
	.module('SmarteditContainerServicesModule', [
		SharedDataServiceModule
	])
	.name;
