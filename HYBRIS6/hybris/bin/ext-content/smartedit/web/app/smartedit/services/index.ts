
import * as angular from 'angular';

import {SharedDataService, SharedDataServiceModule} from './sharedDataService';

export {
	SharedDataService
};

export const SmarteditServicesModule = angular
	.module('smarteditServicesModule', [SharedDataServiceModule])
	.name;
